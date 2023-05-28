package org.alex;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IntervalTimer {

    long beginDateTime;

    public void start() {
        beginDateTime = System.currentTimeMillis();
    }

    public long stop() {
        return System.currentTimeMillis() - beginDateTime;
    }

    public long stopWithMessage() {
        long period = System.currentTimeMillis() - beginDateTime;
        System.out.println("Period: " + period + " ms.    ( " + LocalTime.ofNanoOfDay(period * 1000000).toString() + " )");
        return period;
    }
}
