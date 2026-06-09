package com.pakn.dev;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//implemented as a singleton since we only ever want one ActionHandler handling key/mouse clicks
public final class ActionHandler extends Thread {
    private static ActionHandler instance = null;
    private static ArrayList<Action> actionList = new ArrayList<>();
    private static boolean isRunning = false;
    private static Robot robot;

    ActionHandler() {
        try {
            System.setProperty("java.awt.headless","false");
            robot = new Robot();
        }catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void addAction(Action action) {
        actionList.add(action);
        if (action instanceof KeyClick keyAction) {
            robot.keyPress(keyAction.getKey());
        }else if (action instanceof MouseClick mouseAction) {
            robot.mousePress(mouseAction.getMouseEvent());
        }
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            for (Action action:actionList) {
                if (action.getEndTime() >= System.currentTimeMillis()) {
                    if (action instanceof KeyClick keyAction) {
                        robot.keyRelease(keyAction.getKey());
                    }else if (action instanceof MouseClick mouseAction) {
                        robot.mouseRelease(mouseAction.getMouseEvent());
                    }
                }
            }
        }
    }

    public void stopHandler() {
        isRunning = false;
    }

    public static ActionHandler getInstance() {
        if (instance==null) instance = new ActionHandler();
        return instance;
    }
}
