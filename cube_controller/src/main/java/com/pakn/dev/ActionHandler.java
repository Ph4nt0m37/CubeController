package com.pakn.dev;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

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
        actionList.remove(action);
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
        try {
            while (isRunning) {
                //System.out.println(actionList.size());
                for (int i=0;i<actionList.size();i++) {
                    Action action = actionList.get(i);
                    System.out.println(action.getEndTime());
                    if (System.currentTimeMillis() >= action.getEndTime()) {
                        if (action instanceof KeyClick keyAction) {
                            robot.keyRelease(keyAction.getKey());
                            actionList.remove(action);
                            i--;
                        }else if (action instanceof MouseClick mouseAction) {
                            robot.mouseRelease(mouseAction.getMouseEvent());
                            actionList.remove(action);
                            i--;
                        }
                    }
                }
                Thread.sleep(1);
            }
        }catch (InterruptedException e) {
            System.exit(0);
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
