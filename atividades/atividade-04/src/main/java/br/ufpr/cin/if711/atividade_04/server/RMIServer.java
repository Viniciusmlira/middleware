package br.ufpr.cin.if711.atividade_04.server;

import br.ufpr.cin.if711.atividade_04.common.ITime;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer {
    private static Registry registry;
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Time time = new Time();
        ITime stub = (ITime) UnicastRemoteObject.exportObject(time, 33333);

        registry = LocateRegistry.createRegistry(1099);
        registry.bind("time", stub);

        System.out.println("Time server running");
    }
}
