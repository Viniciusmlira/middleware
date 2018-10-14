package br.ufpr.cin.if711.atividade_04.naming;

import java.io.Serializable;
import lombok.Data;

@Data
public class ClientProxy implements Serializable {
  private static final long serialVersionUID = 1L;
  protected String host;
  protected int port;
  protected int objectId;
}
