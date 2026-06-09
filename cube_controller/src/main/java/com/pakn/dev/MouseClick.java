package com.pakn.dev;

import java.awt.event.InputEvent;

public class MouseClick extends Action{
    private int mouseEvent;

    MouseClick(String mouseEvent, long timeMs) {
        super(timeMs);
        this.mouseEvent = getMouseEventFromString(mouseEvent);
    }

    public int getMouseEvent() {
        return mouseEvent;
    }

    public void setMouseEvent(int mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    public static int getMouseEventFromString(String key) {
        switch (key) {
            case "LEFT_MOUSE_DOWN": return InputEvent.BUTTON1_DOWN_MASK;
            case "RIGHT_MOUSE_DOWN": return InputEvent.BUTTON2_DOWN_MASK;
            case "MIDDLE_MOUSE_DOWN": return InputEvent.BUTTON3_DOWN_MASK;
        
            default:
                return -1;
        }
    }

    @Override
    public boolean equals(Object other) {
        return this==other || mouseEvent==((MouseClick)other).mouseEvent;
    }
}
