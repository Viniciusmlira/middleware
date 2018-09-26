package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.client.handler.ClientRequestHandler;
import br.ufpr.cin.if711.atividade_04.client.handler.ClientRequestHandlerImpl;
import br.ufpr.cin.if711.atividade_04.handler.types.HandlerType;
import br.ufpr.cin.if711.atividade_04.utils.secret.Secrets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Client {
    public static void main(String[] args) throws Exception {
        List<ClientRequestHandler> handlers = new ArrayList<>(HandlerType.values().length);
        for (HandlerType handlerType: HandlerType.values())
            handlers.add(new ClientRequestHandlerImpl(handlerType));
        byte[] message = new byte[1024];
        ThreadLocalRandom.current().nextBytes(message);

        System.out.println(Arrays.stream(HandlerType.values())
                .map(Objects::toString)
                .collect(Collectors.joining(",")));
        while (true) {
            for (int i = 0; i < handlers.size(); ++i) {
                long startTime = System.currentTimeMillis();
                handlers.get(i).send(message);
                byte[] received = handlers.get(i).receive();
                if (i > 0) System.out.print(',');
                System.out.print(System.currentTimeMillis() - startTime);
                System.out.flush();
            }
            System.out.println();
        }
    }
}
