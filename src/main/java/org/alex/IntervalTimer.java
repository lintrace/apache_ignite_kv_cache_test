package org.alex;

public class IntervalTimer {

    long beginDateTime;
    public void Start() {
        beginDateTime = System.currentTimeMillis();
    }
    public long Stop() {
        long period = System.currentTimeMillis() - beginDateTime;
        System.out.println("Period: " + period);
        return period;
    }
}
