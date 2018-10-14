package br.ufpr.cin.if711.atividade_04.server;

import br.ufpr.cin.if711.atividade_04.common.ITime;

public class Time implements ITime {

  @Override
  public long lag(long time) {
    return System.nanoTime() - time;
  }
}
