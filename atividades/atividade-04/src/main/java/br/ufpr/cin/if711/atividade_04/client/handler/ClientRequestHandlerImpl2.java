package br.ufpr.cin.if711.atividade_04.client.handler;

import br.ufpr.cin.if711.atividade_04.handler.types.HandlerType;
import br.ufpr.cin.if711.atividade_04.utils.configs.Config;
import br.ufpr.cin.if711.atividade_04.utils.secret.Secrets;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ClientRequestHandlerImpl2 implements ClientRequestHandler {
    private static final Deserializer<byte[]> deserializer = Serdes.ByteArray().deserializer();
    private static final Serializer<byte[]> serializer = Serdes.ByteArray().serializer();
    private final ClientRequestHandler innerHandler;

    public ClientRequestHandlerImpl2(HandlerType handlerType) throws Exception {
        this.innerHandler = buildInnerRequestHandler(handlerType);
    }

    @Override
    public byte[] receive() throws Exception {
        return innerHandler.receive();
    }

    @Override
    public void send(byte[] message) throws Exception {
        innerHandler.send(message);
    }

    private class TCPClientRequestHandler implements ClientRequestHandler {
        private final int port;

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        TCPClientRequestHandler(int port) throws IOException {
            this.port = port;
        }

        @Override
        public byte[] receive() throws Exception {
            connect();
            int size = dataInputStream.readInt();
            byte[] bytes = new byte[size];
            dataInputStream.readFully(bytes, 0, size);
            disconnect();
            return bytes;
        }

        @Override
        public void send(byte[] message) throws Exception {
            connect();
            dataOutputStream.writeInt(message.length);
            dataOutputStream.write(message, 0, message.length);
            dataOutputStream.flush();
            disconnect();
        }

        private void connect() throws IOException {
            this.socket = new Socket("localhost", port);
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }

        private void disconnect() throws IOException {
            this.socket.close();
            this.socket = null;
            this.dataOutputStream = null;
            this.dataInputStream = null;
        }
    }

    private class UDPClientRequestHandler implements ClientRequestHandler {
        private final DatagramSocket datagramSocket;

        UDPClientRequestHandler(int port) throws IOException {
            this.datagramSocket = new DatagramSocket();
            datagramSocket.connect(new InetSocketAddress("localhost", port));
            DatagramPacket datagramPacket = new DatagramPacket(Secrets.HAND_SHAKE, Secrets.HAND_SHAKE.length);
            datagramPacket.setSocketAddress(datagramSocket.getRemoteSocketAddress());
            datagramSocket.send(datagramPacket);
        }

        @Override
        public byte[] receive() throws Exception {
            int size = receiveInt();
            return receiveMessage(size);
        }

        private int receiveInt() throws IOException {
            byte[] bytes = receiveMessage(Integer.BYTES);
            return ByteBuffer.wrap(bytes).getInt();
        }

        private byte[] receiveMessage(int size) throws IOException {
            byte[] bytes = new byte[size];
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            datagramSocket.receive(packet);
            assert size == packet.getLength();
            return bytes;
        }

        @Override
        public void send(byte[] message) throws Exception {
            sendInt(message.length);
            sendMessage(message);
        }

        private void sendInt(int message) throws IOException {
            sendMessage(ByteBuffer.allocate(Integer.BYTES).putInt(message).array());
        }

        private void sendMessage(byte[] message) throws IOException {
            DatagramPacket packet = new DatagramPacket(message, message.length);
            packet.setSocketAddress(datagramSocket.getRemoteSocketAddress());
            datagramSocket.send(packet);
        }
    }

    private class MiddlewareClientRequestHandler implements ClientRequestHandler {
        private final String serverTopic;
        private final KafkaConsumer<byte[], byte[]> consumer;
        private final KafkaProducer<byte[], byte[]> producer;

        public MiddlewareClientRequestHandler(String clientTopic, String serverTopic) throws Exception {
            Properties properties = Config.getKafkaProperties();
            properties.setProperty("group.id", "client");
            this.serverTopic = serverTopic;
            this.consumer = new KafkaConsumer<>(properties, Config.DESERIALIZER, Config.DESERIALIZER);
            this.producer = new KafkaProducer<>(Config.getKafkaProperties(), Config.SERIALIZER, Config.SERIALIZER);
            consumer.subscribe(Collections.singleton(clientTopic));
            send(clientTopic.getBytes());
        }

        @Override
        public byte[] receive() throws Exception {
            while (true) {
                ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMinutes(30));
                consumer.commitSync();
                if(records.count() > 0){
                    return records.iterator().next().value();
                }
            }
        }

        @Override
        public void send(byte[] message) throws Exception {
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(serverTopic, null, message);
            producer.send(record);
            producer.flush();
        }
    }

    private ClientRequestHandler buildInnerRequestHandler(HandlerType handlerType) throws Exception {
        switch (handlerType) {
            case TCP: return new TCPClientRequestHandler(Config.TCP_PORT);
            case UDP: return new UDPClientRequestHandler(Config.UDP_PORT);
            case MIDDLEWARE: return new MiddlewareClientRequestHandler(Config.CLIENT_TOPIC, Config.SERVER_TOPIC);
        }
        throw new IllegalArgumentException(String.format("Invalid handler type %s", handlerType));
    }
}
