package br.ufpr.cin.if711.atividade_04.common;

import br.ufpr.cin.if711.atividade_04.client.Invocation;
import br.ufpr.cin.if711.atividade_04.client.Requestor;
import br.ufpr.cin.if711.atividade_04.naming.ClientProxy;
import br.ufpr.cin.if711.atividade_04.naming.INaming;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NamingProxy extends ClientProxy implements INaming {
  private static final long serialVersionUID = 1L;

  public void bind(String serviceName, ClientProxy clientProxy) throws UnknownHostException, IOException, Throwable {
    ArrayList<Object> parameters = new ArrayList<Object>();
    class Local {};
    Requestor requestor = new Requestor(this.getPort());

    String methodName = Local.class.getEnclosingMethod().getName();
    parameters.add(serviceName);
    parameters.add(clientProxy);

    Invocation inv = Invocation.builder()
        .ipAddress(this.getHost())
        .portNumber(this.getPort())
        .objectId(this.getObjectId())
        .operationName(methodName)
        .parameters(parameters)
        .build();

    requestor.invoke(inv);
  }

  public ClientProxy lookup(String serviceName) throws Throwable {
    ArrayList<Object> parameters = new ArrayList<Object>();
    Requestor requestor = new Requestor(this.getPort());

    String methodName = "lookup";
    parameters.add(serviceName);

    Invocation inv = Invocation.builder()
        .ipAddress(this.getHost())
        .portNumber(this.getPort())
        .objectId(this.getObjectId())
        .operationName(methodName)
        .parameters(parameters)
        .build();
    return (ClientProxy) requestor.invoke(inv);
  }

  public List<String> list() throws UnknownHostException, IOException, Throwable {
    Requestor requestor = new Requestor(this.getPort());

    String methodName = "list";

    Invocation inv = Invocation.builder()
        .ipAddress(this.getHost())
        .portNumber(this.getPort())
        .objectId(this.getObjectId())
        .operationName(methodName)
        .build();

    return (List<String>) requestor.invoke(inv);
  }
}
