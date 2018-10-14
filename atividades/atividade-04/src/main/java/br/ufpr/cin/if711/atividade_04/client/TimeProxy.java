package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.common.ITime;
import br.ufpr.cin.if711.atividade_04.naming.ClientProxy;
import java.util.ArrayList;

public class TimeProxy extends ClientProxy implements ITime {

  @Override
  public long lag(long time) throws Throwable {
    ArrayList<Object> parameters = new ArrayList<Object>();
    class Local {};
    Requestor requestor = new Requestor();

    String methodName = Local.class.getEnclosingMethod().getName();
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
}
