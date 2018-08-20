package br.ufpe.cin.if711.atividade_01;

import java.util.concurrent.locks.ReentrantLock;

public class Hashi {
    private final ReentrantLock reentrantLock;


    public Hashi() {
        this.reentrantLock = new ReentrantLock();
    }

    public void pegar() {
        reentrantLock.lock();
    }

    public boolean tentarPegar() {
        return reentrantLock.tryLock();
    }

    public void devolver() {
        reentrantLock.unlock();
    }
}
