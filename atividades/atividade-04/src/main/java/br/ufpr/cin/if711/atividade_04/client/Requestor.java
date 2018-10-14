package br.ufpr.cin.if711.atividade_04.client;

import br.ufpr.cin.if711.atividade_04.handler.ClientRequestHandler;
import br.ufpr.cin.if711.atividade_04.handler.impl.ClientRequestHandlerImpl;
import br.ufpr.cin.if711.atividade_04.common.Marshaller;
import br.ufpr.cin.if711.atividade_04.common.Message;
import br.ufpr.cin.if711.atividade_04.common.MessageBody;
import br.ufpr.cin.if711.atividade_04.common.MessageHeader;
import br.ufpr.cin.if711.atividade_04.common.ReplyBody;
import br.ufpr.cin.if711.atividade_04.common.RequestBody;
import br.ufpr.cin.if711.atividade_04.common.RequestHeader;

import java.io.IOException;


public class Requestor {
  private final ClientRequestHandler crh;

  public Requestor(int port) throws IOException {
    this.crh = new ClientRequestHandlerImpl(port);
  }

  public Object invoke(Invocation inv) throws Exception {
    Marshaller marshaller = new Marshaller();

    RequestHeader requestHeader = RequestHeader.builder().context("").requestId(0).responseExpected(true).objectKey(inv.getObjectId()).operation(inv.getOperationName()).build();
    RequestBody requestBody = RequestBody.builder().parameters(inv.getParameters()).build();
    MessageHeader messageHeader = MessageHeader.builder().magic("MIOP").version(0).byteOrder(false).messageType(0).messageSize(0).build();
    MessageBody messageBody = MessageBody.builder().requestHeader(requestHeader).requestBody(requestBody).build();
    Message msgToBeMarshalled = Message.builder().header(messageHeader).body(messageBody).build();

    byte[] msgMarshalled = marshaller.marshall(msgToBeMarshalled);
    crh.send(msgMarshalled);

    byte[] msgToBeUnmarshalled = crh.receive();
    Message msgUnmarshalled = marshaller.unmarshall(msgToBeUnmarshalled);

    final ReplyBody replyBody = msgUnmarshalled.getBody().getReplyBody();
    return replyBody.getOperationResult();
  }
}