package br.ufpr.cin.if711.atividade_04.utils.configs;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Properties;

public class Config {
    public static final int TCP_PORT = 10200;
    public static final int UDP_PORT = 10300;
    public static final String SERVER_TOPIC = "server-topic";
    public static final String CLIENT_TOPIC = "client-topic";
    public static final Deserializer<byte[]> DESERIALIZER = Serdes.ByteArray().deserializer();
    public static final Serializer<byte[]> SERIALIZER = Serdes.ByteArray().serializer();

    public static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        return properties;
    }
}
