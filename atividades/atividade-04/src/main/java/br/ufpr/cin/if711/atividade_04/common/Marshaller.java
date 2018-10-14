package br.ufpr.cin.if711.atividade_04.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Marshaller {
  public byte[] marshall(Message msg) throws IOException, InterruptedException {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
    objectStream.writeObject(msg);

    return byteStream.toByteArray();
  }

  public Message unmarshall(byte[] msg) throws IOException, InterruptedException, ClassNotFoundException {
    ByteArrayInputStream byteStream = new ByteArrayInputStream(msg);
    ObjectInputStream objectStream = new ObjectInputStream(byteStream);

    return (Message) objectStream.readObject();
  }
}
