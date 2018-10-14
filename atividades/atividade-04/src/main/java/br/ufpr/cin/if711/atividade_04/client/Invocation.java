package br.ufpr.cin.if711.atividade_04.client;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Invocation {
  private String ipAddress;
  private int portNumber;
  private String operationName;
  private List<Object> parameters;
  private int objectId;
}
