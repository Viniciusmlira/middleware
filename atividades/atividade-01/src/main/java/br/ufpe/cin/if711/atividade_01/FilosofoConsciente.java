package br.ufpe.cin.if711.atividade_01;

import lombok.Value;

import java.util.concurrent.TimeUnit;

@Value
public class FilosofoConsciente implements Runnable {
    String nome;
    Hashi esquerda;
    Hashi direita;

    @Override
    public void run() {
        try {
            boolean pegouOsDois = false;
            do {
                esquerda.pegar();
                System.out.println(String.format("O filósofo %s pegou o hashi a sua esquerda", nome));
                pegouOsDois |= direita.tentarPegar();
                if (pegouOsDois) {
                    System.out.println(String.format("O filósofo %s pegou o hashi a sua direita", nome));
                    comer();
                    esquerda.devolver();
                    System.out.println(String.format("O filósofo %s devolveu o hashi a sua esquerda", nome));
                    direita.devolver();
                    System.out.println(String.format("O filósofo %s devolveu o hashi a sua direita", nome));
                } else {
                    esquerda.devolver();
                    System.out.println(String.format("O filósofo %s devolveu o hashi a sua esquerda", nome));
                }
            } while (pegouOsDois);
        } catch (InterruptedException interruped) {}
    }

    private void comer() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
        System.out.println(String.format("O filósofo %s terminou de comer", nome));
    }
}
