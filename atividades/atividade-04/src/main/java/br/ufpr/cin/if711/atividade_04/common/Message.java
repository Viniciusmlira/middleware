package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private MessageHeader header;
  private MessageBody body;
}

