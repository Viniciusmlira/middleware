package br.ufpr.cin.if711.atividade_04.naming;


import br.ufpr.cin.if711.atividade_04.common.Marshaller;
import br.ufpr.cin.if711.atividade_04.common.Message;
import br.ufpr.cin.if711.atividade_04.common.MessageBody;
import br.ufpr.cin.if711.atividade_04.common.MessageHeader;
import br.ufpr.cin.if711.atividade_04.common.ReplyBody;
import br.ufpr.cin.if711.atividade_04.common.ReplyHeader;
import br.ufpr.cin.if711.atividade_04.common.Termination;
import br.ufpr.cin.if711.atividade_04.handler.types.HandlerType;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandler;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandlerImpl;
import java.io.IOException;

public class Invoker {
  public void invoke(int port) throws IOException, Throwable {
    ServerRequestHandler srh = new ServerRequestHandlerImpl("localhost", port);
    byte[] msgToBeUnmarshalled = null;
    byte[] msgMarshalled = null;
    Message msgUnmarshalled = null;
    Marshaller marshaller = new Marshaller();

    Naming rObj = new Naming();

    while(true) {
      msgToBeUnmarshalled = srh.receive();
      msgUnmarshalled = marshaller.unmarshall(msgToBeUnmarshalled);

      switch(msgUnmarshalled.getBody().getRequestHeader().getOperation()) {
        case "bind":
          String serviceNameBind = (String) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
          ClientProxy clientProxyArg = (ClientProxy) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);
          rObj.bind(serviceNameBind, clientProxyArg);

          Message bindMsgToBeMarshalled = Message.builder()
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
                  .build())
              .build();
          msgMarshalled = marshaller.marshall(bindMsgToBeMarshalled);

          srh.send(msgMarshalled);
          break;

        case "lookup":
          String serviceName = (String) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);

          Message lookupMsgToBeMarshalled = Message.builder()
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

          msgMarshalled = marshaller.marshall(lookupMsgToBeMarshalled);

          srh.send(msgMarshalled);
          break;

        case "list":

          Message listMsgToBeMarshalled = Message.builder()
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

          msgMarshalled = marshaller.marshall(listMsgToBeMarshalled);

          srh.send(msgMarshalled);
          break;
      }
    }
  }
}