package br.ufpr.cin.if711.atividade_04.server;

import br.ufpr.cin.if711.atividade_04.common.Marshaller;
import br.ufpr.cin.if711.atividade_04.common.Message;
import br.ufpr.cin.if711.atividade_04.common.MessageBody;
import br.ufpr.cin.if711.atividade_04.common.MessageHeader;
import br.ufpr.cin.if711.atividade_04.common.ReplyBody;
import br.ufpr.cin.if711.atividade_04.common.ReplyHeader;
import br.ufpr.cin.if711.atividade_04.naming.ClientProxy;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandler;
import br.ufpr.cin.if711.atividade_04.server.handler.ServerRequestHandlerImpl;
import java.io.IOException;

public class Invoker {
  private Time rObj = new Time();
  private Marshaller marshaller = new Marshaller();

  public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
    ServerRequestHandler srh = new ServerRequestHandlerImpl(clientProxy.getHost(), clientProxy.getPort());
    byte[] msgToBeUnmarshalled = null;
    byte[] msgMarshalled = null;
    Message msgUnmarshalled = null;

    while(true) {
      msgToBeUnmarshalled = srh.receive();
      msgUnmarshalled = marshaller.unmarshall(msgToBeUnmarshalled);

      switch(msgUnmarshalled.getBody().getRequestHeader().getObjectKey()) {
        case 7777:
          long requestTime = (long) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
          final long lag = rObj.lag(requestTime);

          Message addMsgToBeMarshalled = Message.builder()
              .header(MessageHeader.builder()
                  .magic("protocolo")
                  .version(0)
                  .byteOrder(false)
                  .messageType(0)
                  .build())
              .body(MessageBody.builder()
                  .replyBody(new ReplyBody(lag))
                  .replyHeader(ReplyHeader.builder()
                      .replyStatus(0)
                      .serviceContext("")
                      .requestId(0)
                      .build())
                  .build())
              .build();

          msgMarshalled = marshaller.marshall(addMsgToBeMarshalled);

          srh.send(msgMarshalled);
          break;
      }
    }
  }
}