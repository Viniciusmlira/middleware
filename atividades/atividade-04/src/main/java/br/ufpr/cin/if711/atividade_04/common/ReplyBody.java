package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import lombok.Value;

@Value
public class ReplyBody implements Serializable {
  private static final long serialVersionUID = 1L;
  private Object operationResult;
}
