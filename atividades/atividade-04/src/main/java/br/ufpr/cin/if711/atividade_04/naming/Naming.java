package br.ufpr.cin.if711.atividade_04.naming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public class Naming implements INaming {
  private NamingRepository repository = new NamingRepository();

  public void bind(String serviceName, ClientProxy clientProxy) throws UnknownHostException, IOException, Throwable {
    NamingRecord record = NamingRecord.builder().clientProxy(clientProxy).serviceName(serviceName).build();
    repository.add(record);
  }

  public ClientProxy lookup(String serviceName) throws UnknownHostException, IOException, Throwable {
    NamingRecord record = repository.find(serviceName);
    if(record != null) {
      return record.getClientProxy();
    }
    else {
      return null;
    }
  }

  public List<String> list() throws UnknownHostException, IOException, Throwable {
    return repository.listServiceNames();
  }
}