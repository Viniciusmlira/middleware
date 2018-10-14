package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.common.ITime;
import br.ufpr.cin.if711.atividade_04.naming.ClientProxy;

import java.io.IOException;
import java.util.ArrayList;

public class TimeProxy extends ClientProxy implements ITime {
  private Requestor requestor;

  @Override
  public long lag(long time) throws Throwable {
    Requestor requestor = getRequestor();
    ArrayList<Object> parameters = new ArrayList<Object>();

    String methodName = "lag";
    parameters.add(time);

    Invocation inv = Invocation.builder()
        .objectId(7777)
        .ipAddress(this.getHost())
        .portNumber(this.getPort())
        .operationName(methodName)
        .parameters(parameters)
        .build();

    return (long) requestor.invoke(inv);
  }

  private Requestor getRequestor() throws IOException {
    if (requestor == null)
      requestor = new Requestor(this.getPort());
    return requestor;
  }
}
