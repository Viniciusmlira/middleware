package br.ufpe.cin.if711.atividade_01;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainComDeadlock {
    private static final int NUMERO_FILOSOFOS = 2;

    public static void main(String[] args) {
        final List<Hashi> hashis = IntStream.range(0, NUMERO_FILOSOFOS)
                .mapToObj(i -> new Hashi())
                .collect(Collectors.toList());
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(NUMERO_FILOSOFOS);
        IntStream.range(0, NUMERO_FILOSOFOS)
                .mapToObj(id -> construirFilosofo(id, hashis))
                .forEach(filosofo -> scheduledExecutor.scheduleAtFixedRate(filosofo, 0L, 5L, TimeUnit.SECONDS));
    }

    private static Runnable construirFilosofo(int id, List<Hashi> hashis) {
        return new FilosofoGuloso(String.format("%03d", id), hashiDaEsquerda(hashis, id), hashiDaDireita(hashis, id));
    }

    private static Hashi hashiDaEsquerda(List<Hashi> hashis, int id) {
        return hashis.get(((id - 1) % NUMERO_FILOSOFOS + NUMERO_FILOSOFOS) % NUMERO_FILOSOFOS);
    }

    private static Hashi hashiDaDireita(List<Hashi> hashis, int id) {
        return hashis.get((id + 1) % NUMERO_FILOSOFOS);
    }
}
