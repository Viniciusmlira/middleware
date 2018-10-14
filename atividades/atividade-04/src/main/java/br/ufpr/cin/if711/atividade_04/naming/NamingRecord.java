package br.ufpr.cin.if711.atividade_04.naming;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NamingRecord {
  private String serviceName;
  private ClientProxy clientProxy;
}
