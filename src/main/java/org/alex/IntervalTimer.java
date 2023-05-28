package org.alex;

public class IntervalTimer {

    long beginDateTime;
    public void start() {
        beginDateTime = System.currentTimeMillis();
    }

    public long stop() {
        long period = System.currentTimeMillis() - beginDateTime;
        return period;
    }
    public long stopWithMessage() {
        long period = System.currentTimeMillis() - beginDateTime;
        System.out.println("Period: " + period + " ms.");
        return period;
    }
}
