package br.ufpr.cin.if711.atividade_04.server.handler;

import br.ufpr.cin.if711.atividade_04.handler.types.HandlerType;
import br.ufpr.cin.if711.atividade_04.utils.configs.Config;
import br.ufpr.cin.if711.atividade_04.utils.secret.Secrets;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class ServerRequestHandlerImpl2 implements ServerRequestHandler {
    private final ServerRequestHandler innerHandler;

    public ServerRequestHandlerImpl2(HandlerType handlerType) throws Exception {
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

    private class TCPServerRequestHandler implements ServerRequestHandler {
        private final ServerSocket serverSocket;

        private Socket socket;
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;

        TCPServerRequestHandler(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
        }

        @Override
        public byte[] receive() throws Exception {
            connect();
            final int msgSize = dataInputStream.readInt();
            byte[] msg = new byte[msgSize];
            dataInputStream.readFully(msg, 0, msgSize);
            disconnect();
            return msg;
        }

        @Override
        public void send(byte[] message) throws Exception {
            connect();
            this.dataOutputStream.writeInt(message.length);
            this.dataOutputStream.write(message, 0, message.length);
            this.dataOutputStream.flush();
            disconnect();
        }

        private void connect() throws IOException {
            this.socket = serverSocket.accept();
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }

        private void disconnect() throws IOException {
            this.socket.close();
            this.dataInputStream = null;
            this.dataOutputStream = null;
            this.socket = null;
        }
    }

    private class UDPServerRequestHandler implements ServerRequestHandler {
        private final DatagramSocket datagramSocket;

        public UDPServerRequestHandler(int port) throws IOException {
            this.datagramSocket = new DatagramSocket(port);
            byte[] handShakeBytes = new byte[Secrets.HAND_SHAKE.length];
            do {
                DatagramPacket datagramPacket = new DatagramPacket(handShakeBytes, handShakeBytes.length);
                this.datagramSocket.receive(datagramPacket);
                if (Arrays.equals(Secrets.HAND_SHAKE, handShakeBytes))
                    datagramSocket.connect(datagramPacket.getSocketAddress());
            } while (!datagramSocket.isConnected());
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

    private class MiddlewareRequestHandler implements ServerRequestHandler {
        private final String clientTopic;
        private final KafkaConsumer<byte[], byte[]> consumer;
        private final KafkaProducer<byte[], byte[]> producer;

        public MiddlewareRequestHandler(String serverTopic) throws Exception {
            Properties properties = Config.getKafkaProperties();
            properties.setProperty("group.id", "server");
            this.consumer =
                    new KafkaConsumer<>(properties, Config.DESERIALIZER, Config.DESERIALIZER);
            this.producer = new KafkaProducer<>(Config.getKafkaProperties(), Config.SERIALIZER, Config.SERIALIZER);
            this.consumer.subscribe(Collections.singleton(serverTopic));
            this.clientTopic = new String(receive());
        }

        @Override
        public byte[] receive() throws Exception {
            while (true) {
                ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMinutes(30));
                consumer.commitSync();
                if(records.count() > 0) return records.iterator().next().value();
            }
        }



        @Override
        public void send(byte[] message) throws Exception {
            ProducerRecord<byte[], byte[]> record = new ProducerRecord<>(clientTopic, null, message);
            producer.send(record);
            producer.flush();
        }
    }

    private ServerRequestHandler buildInnerRequestHandler(HandlerType handlerType) throws Exception {
        switch (handlerType) {
            case TCP: return new TCPServerRequestHandler(Config.TCP_PORT);
            case UDP: return new UDPServerRequestHandler(Config.UDP_PORT);
            case MIDDLEWARE: return new MiddlewareRequestHandler(Config.SERVER_TOPIC);
        }
        throw new IllegalArgumentException(String.format("Invalid handler type %s", handlerType));
    }
}
