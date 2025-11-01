package org.example.utils;

public class Metrics {
    private long startTime;
    private long endTime;
    private int operations;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public void addOp() {
        operations++;
    }

    public int getOps() {
        return operations;
    }

    public double getTimeMs() {
        return (endTime - startTime) / 1_000_000.0;
    }
}
