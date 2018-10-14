package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReplyHeader implements Serializable {
  private static final long serialVersionUID = 1L;
  private String serviceContext;
  private  int requestId;
  private  int replyStatus;
}
