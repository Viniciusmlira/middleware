package br.ufpr.cin.if711.atividade_04.handler.impl;

import br.ufpr.cin.if711.atividade_04.handler.ServerRequestHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRequestHandlerImpl implements ServerRequestHandler {
    private final ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ServerRequestHandlerImpl(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public synchronized void connect() throws IOException {
        this.socket = serverSocket.accept();
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public synchronized byte[] receive() throws Exception {
        final int msgSize = dataInputStream.readInt();
        byte[] msg = new byte[msgSize];
        dataInputStream.readFully(msg, 0, msgSize);
        return msg;
    }

    @Override
    public synchronized void send(byte[] message) throws Exception {
        this.dataOutputStream.writeInt(message.length);
        this.dataOutputStream.write(message, 0, message.length);
        this.dataOutputStream.flush();
    }

    @Override
    public synchronized void disconnect() throws IOException {
        this.dataOutputStream.flush();
        this.socket.close();
    }
}
