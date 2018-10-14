package br.ufpr.cin.if711.atividade_04.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITime extends Remote {
  long lag(long time) throws Throwable, RemoteException;
}
