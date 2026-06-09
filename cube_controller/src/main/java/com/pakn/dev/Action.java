package com.pakn.dev;

public class Action {
    private long timeMs;
    private long endTime;

    Action(long timeMs) {
        this.timeMs = timeMs;
        endTime = System.currentTimeMillis()+timeMs;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof KeyClick otherKeyAction && this instanceof KeyClick keyAction) return keyAction.equals(otherKeyAction);
        if (other instanceof MouseClick otherMouseAction && this instanceof MouseClick mouseAction) return mouseAction.equals(otherMouseAction);
        return this==other;
    }

    @Override
    public String toString() {
        return "Action [endTime=" + endTime + "]";
    }
    
}
