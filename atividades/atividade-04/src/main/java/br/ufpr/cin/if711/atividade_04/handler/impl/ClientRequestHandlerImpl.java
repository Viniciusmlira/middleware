package br.ufpr.cin.if711.atividade_04.handler.impl;

import br.ufpr.cin.if711.atividade_04.handler.ClientRequestHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientRequestHandlerImpl implements ClientRequestHandler {
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public ClientRequestHandlerImpl(int port) throws IOException {
        this.socket = new Socket("localhost", port);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public byte[] receive() throws Exception {
        int size = dataInputStream.readInt();
        byte[] bytes = new byte[size];
        dataInputStream.readFully(bytes, 0, size);
        return bytes;
    }

    @Override
    public void send(byte[] message) throws Exception {
        dataOutputStream.writeInt(message.length);
        dataOutputStream.write(message, 0, message.length);
        dataOutputStream.flush();
    }
}
