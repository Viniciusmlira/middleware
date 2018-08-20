package br.ufpe.cin.if711.atividade_01;

import lombok.Value;

import java.util.concurrent.TimeUnit;

@Value
public class FilosofoGuloso implements Runnable {
    String nome;
    Hashi esquerda;
    Hashi direita;

    @Override
    public void run() {
        try {
            esquerda.pegar();
            System.out.println(String.format("O filósofo %s pegou o hashi a sua esquerda", nome));
            direita.pegar();
            System.out.println(String.format("O filósofo %s pegou o hashi a sua direita", nome));
            comer();
            esquerda.devolver();
            System.out.println(String.format("O filósofo %s devolveu o hashi a sua esquerda", nome));
            direita.devolver();
            System.out.println(String.format("O filósofo %s devolveu o hashi a sua direita", nome));
        } catch (InterruptedException interruped) { }
    }

    private void comer() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
        System.out.println(String.format("O filósofo %s terminou de comer", nome));
    }
}
