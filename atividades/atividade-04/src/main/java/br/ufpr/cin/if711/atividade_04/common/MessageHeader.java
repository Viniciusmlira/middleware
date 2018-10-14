package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MessageHeader implements Serializable {
  private static final long serialVersionUID = 1L;
  private String magic;
  private int version;
  private boolean byteOrder;
  private int messageType;
  private long messageSize;
}
