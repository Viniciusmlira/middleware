package br.ufpr.cin.if711.atividade_04.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequestBody implements Serializable {
  private static final long serialVersionUID = 1L;
  List<Object> parameters;
}
