package br.ufpr.cin.if711.atividade_04.naming;

import java.util.List;

public interface INaming {
  void bind(String serviceName, ClientProxy clientProxy) throws Throwable;

  ClientProxy lookup(String serviceName) throws Throwable;

  List<String> list() throws Throwable;
}
