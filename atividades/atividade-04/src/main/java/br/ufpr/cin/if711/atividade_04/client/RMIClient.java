package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.common.ITime;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) throws Throwable {
        Registry registry = LocateRegistry.getRegistry();
        ITime stub = (ITime) registry.lookup("time");
        for (int i = 0; i < 1_000_000; ++i) {
            long start = System.nanoTime();
            long lag = stub.lag(System.nanoTime());
            System.out.println(System.nanoTime() - start);
        }
    }
}
