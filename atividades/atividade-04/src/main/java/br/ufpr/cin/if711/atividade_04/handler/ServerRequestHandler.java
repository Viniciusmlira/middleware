package br.ufpr.cin.if711.atividade_04.handler;

import br.ufpr.cin.if711.atividade_04.handler.RequestHandler;

import java.io.IOException;

public interface ServerRequestHandler extends RequestHandler {
    void connect() throws IOException;
    boolean isConnected();
    void disconnect() throws IOException;
}
