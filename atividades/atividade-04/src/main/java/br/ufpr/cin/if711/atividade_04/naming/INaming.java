package br.ufpr.cin.if711.atividade_04.naming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface INaming {
  public void bind(String serviceName, ClientProxy clientProxy) throws UnknownHostException, IOException, Throwable;

  public ClientProxy lookup(String serviceName) throws UnknownHostException, IOException, Throwable;

  public List<String> list() throws UnknownHostException, IOException, Throwable;
}
