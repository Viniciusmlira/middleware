package br.ufpr.cin.if711.atividade_04.naming;

public class Server {
  public static void main(String[] args) throws Throwable{
    Invoker invoker = new Invoker();

    System.out.println("Naming server running");
    invoker.invoke(11111);
  }
}
