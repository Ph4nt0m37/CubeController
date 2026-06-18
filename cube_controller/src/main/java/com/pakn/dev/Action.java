package com.pakn.dev;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Action {
    private long timeMs;
    
    @JsonIgnore
    private long endTime;
    
    private String actionString;

    Action(long timeMs, String actionString) {
        this.timeMs = timeMs;
        endTime = System.currentTimeMillis()+timeMs;
        this.actionString = actionString;
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
    
    public String getActionString() {
        return actionString;
    }

    public void setActionString(String actionString) {
        this.actionString = actionString;
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
