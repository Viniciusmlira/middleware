package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.common.NamingProxy;

public class Client {
    public static void main(String[] args) throws Throwable {
        NamingProxy namingProxy = new NamingProxy();
        namingProxy.setHost("localhost");
        namingProxy.setPort(11111);

        TimeProxy timeProxy = (TimeProxy) namingProxy.lookup("time");

        if(timeProxy == null) {
            System.out.println("Couldn't find service");
            return;
        }

        for (int i = 0; i < 1_000_000; ++i) {
            long start = System.nanoTime();
            timeProxy.lag(System.nanoTime());
            System.out.println(System.nanoTime() - start);
        }
    }
}
