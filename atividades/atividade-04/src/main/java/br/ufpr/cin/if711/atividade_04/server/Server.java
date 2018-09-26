package br.ufpr.cin.if711.atividade_04.server;

import br.ufpr.cin.if711.atividade_04.handler.types.HandlerType;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandler;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandlerImpl;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandlerImpl2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args) throws Exception {
        List<ServerRequestHandler> handlers = new ArrayList<>(HandlerType.values().length);
        for (HandlerType handlerType: HandlerType.values())
            handlers.add(new ServerRequestHandlerImpl(handlerType));
        byte[] message = new byte[1024];
        ThreadLocalRandom.current().nextBytes(message);
        while (true) {
            for (ServerRequestHandler handler: handlers) {
                byte[] received = handler.receive();
                handler.send(message);
            }
        }
    }
}
