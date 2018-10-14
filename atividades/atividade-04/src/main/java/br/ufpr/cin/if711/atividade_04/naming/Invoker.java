package br.ufpr.cin.if711.atividade_04.naming;


import br.ufpr.cin.if711.atividade_04.common.Marshaller;
import br.ufpr.cin.if711.atividade_04.common.Message;
import br.ufpr.cin.if711.atividade_04.common.MessageBody;
import br.ufpr.cin.if711.atividade_04.common.MessageHeader;
import br.ufpr.cin.if711.atividade_04.common.ReplyBody;
import br.ufpr.cin.if711.atividade_04.common.ReplyHeader;
import br.ufpr.cin.if711.atividade_04.handler.ServerRequestHandler;
import br.ufpr.cin.if711.atividade_04.handler.impl.ServerRequestHandlerImpl;
import java.io.IOException;

public class Invoker {
  private Naming rObj;

  public Invoker() {
    this.rObj = new Naming();
  }

  public void invoke(int port) throws IOException, Throwable {
    ServerRequestHandler srh = new ServerRequestHandlerImpl(port);
    Marshaller marshaller = new Marshaller();

    while(true) {
      srh.connect();
      Message resultMessage = Message.builder().build();
      try {
        byte[] msgToBeUnmarshalled = srh.receive();
        Message message = marshaller.unmarshall(msgToBeUnmarshalled);
        switch(message.getBody().getRequestHeader().getOperation()) {
          case "bind":
            resultMessage = bind(message);
            break;
          case "lookup":
            resultMessage = lookup(message);
            break;
          case "list":
            resultMessage = list(message);
            break;
        }
        byte[] msgMarshalled = marshaller.marshall(resultMessage);
        srh.send(msgMarshalled);
      } finally {
        srh.disconnect();
      }
    }
  }

  private Message bind(Message message) throws Throwable {
    String serviceNameBind = (String) message.getBody().getRequestBody().getParameters().get(0);
    ClientProxy clientProxyArg = (ClientProxy) message.getBody().getRequestBody().getParameters().get(1);
    rObj.bind(serviceNameBind, clientProxyArg);
    System.out.printf("Bound %s to %s:%d%n", serviceNameBind, clientProxyArg.host, clientProxyArg.port);

    return Message.builder()
            .header(MessageHeader.builder()
                    .magic("protocolo")
                    .messageSize(0)
                    .messageType(0)
                    .byteOrder(false)
                    .build())
            .body(MessageBody.builder()
                    .replyHeader(ReplyHeader.builder()
                            .requestId(0)
                            .serviceContext("")
                            .replyStatus(0)
                            .build()).replyBody(new ReplyBody(null))
                    .build())
            .build();
  }

  private Message lookup(Message message) throws Throwable {
    String serviceName = (String) message.getBody().getRequestBody().getParameters().get(0);
    System.out.printf("Looking up %s%n", serviceName);

    return Message.builder()
            .header(MessageHeader.builder()
                    .magic("protocolo")
                    .messageSize(0)
                    .messageType(0)
                    .byteOrder(false)
                    .build())
            .body(MessageBody.builder()
                    .replyHeader(ReplyHeader.builder()
                            .requestId(0)
                            .serviceContext("")
                            .replyStatus(0)
                            .build())
                    .replyBody(new ReplyBody(rObj.lookup(serviceName)))
                    .build())
            .build();
  }

  private Message list(Message message) throws Throwable {
    return Message.builder()
            .header(MessageHeader.builder()
                    .magic("protocolo")
                    .messageSize(0)
                    .messageType(0)
                    .byteOrder(false)
                    .build())
            .body(MessageBody.builder()
                    .replyHeader(ReplyHeader.builder()
                            .requestId(0)
                            .serviceContext("")
                            .replyStatus(0)
                            .build())
                    .replyBody(new ReplyBody(rObj.list()))
                    .build())
            .build();
  }
}