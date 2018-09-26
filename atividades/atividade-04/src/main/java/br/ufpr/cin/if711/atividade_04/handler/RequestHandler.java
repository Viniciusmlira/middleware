package br.ufpr.cin.if711.atividade_04.handler;

public interface RequestHandler {
    byte[] receive() throws Exception;
    void send(byte[] message) throws Exception;
}
