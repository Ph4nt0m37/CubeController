package com.pakn.dev;

public class Action implements {
    private long endTime;

    Action(long timeMs) {
        endTime = System.currentTimeMillis()+timeMs;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object other) {
        return this==other;
    }
    
}
