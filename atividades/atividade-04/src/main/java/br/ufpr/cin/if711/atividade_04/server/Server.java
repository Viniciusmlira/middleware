package br.ufpr.cin.if711.atividade_04.server;

import br.ufpr.cin.if711.atividade_04.client.TimeProxy;
import br.ufpr.cin.if711.atividade_04.common.NamingProxy;
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

    public static void main(String[] args) throws Throwable {
        Invoker invoker = new Invoker();

        TimeProxy timeProxy = new TimeProxy();
        timeProxy.setHost("localhost");
        timeProxy.setPort(33333);

        NamingProxy namingProxy = new NamingProxy();
        namingProxy.setHost("localhost");
        namingProxy.setPort(11111);
        namingProxy.setObjectId(1);

        namingProxy.bind("time", timeProxy);

        System.out.println("Time server running");
        invoker.invoke(timeProxy);
    }
}
