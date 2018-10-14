package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestHeader implements Serializable {
  private static final long serialVersionUID = 1L;
  private String context;
  private int requestId;
  private boolean responseExpected;
  private int objectKey;
  private String operation;
}
