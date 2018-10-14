package br.ufpr.cin.if711.atividade_04.naming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamingRepository {
  private Map<String, NamingRecord> namingRecords = new HashMap<>();

  public void add(NamingRecord namingRecord) {
    namingRecords.put(namingRecord.getServiceName(), namingRecord);
  }

  public NamingRecord find(String serviceName) {
    return namingRecords.get(serviceName);
  }

  public List<String> listServiceNames() {
    return new ArrayList<>(namingRecords.keySet());
  }
}

