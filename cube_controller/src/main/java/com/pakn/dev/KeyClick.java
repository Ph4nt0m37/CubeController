package com.pakn.dev;

import java.awt.event.KeyEvent;

public class KeyClick extends Action{
    private int key;
    
    KeyClick(String key, long timeMs) {
        super(timeMs);
        this.key = getKeyEventFromString(key);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public static int getKeyEventFromString(String key) {
        switch (key) {
        case "a": return KeyEvent.VK_A; 
        case "b": return KeyEvent.VK_B; 
        case "c": return KeyEvent.VK_C; 
        case "d": return KeyEvent.VK_D; 
        case "e": return KeyEvent.VK_E; 
        case "f": return KeyEvent.VK_F; 
        case "g": return KeyEvent.VK_G; 
        case "h": return KeyEvent.VK_H; 
        case "i": return KeyEvent.VK_I; 
        case "j": return KeyEvent.VK_J; 
        case "k": return KeyEvent.VK_K; 
        case "l": return KeyEvent.VK_L; 
        case "m": return KeyEvent.VK_M; 
        case "n": return KeyEvent.VK_N; 
        case "o": return KeyEvent.VK_O; 
        case "p": return KeyEvent.VK_P; 
        case "q": return KeyEvent.VK_Q; 
        case "r": return KeyEvent.VK_R; 
        case "s": return KeyEvent.VK_S; 
        case "t": return KeyEvent.VK_T; 
        case "u": return KeyEvent.VK_U; 
        case "v": return KeyEvent.VK_V; 
        case "w": return KeyEvent.VK_W; 
        case "x": return KeyEvent.VK_X; 
        case "y": return KeyEvent.VK_Y; 
        case "z": return KeyEvent.VK_Z; 
        case "`": return KeyEvent.VK_BACK_QUOTE; 
        case "0": return KeyEvent.VK_0; 
        case "1": return KeyEvent.VK_1; 
        case "2": return KeyEvent.VK_2; 
        case "3": return KeyEvent.VK_3; 
        case "4": return KeyEvent.VK_4; 
        case "5": return KeyEvent.VK_5; 
        case "6": return KeyEvent.VK_6; 
        case "7": return KeyEvent.VK_7; 
        case "8": return KeyEvent.VK_8; 
        case "9": return KeyEvent.VK_9; 
        case "-": return KeyEvent.VK_MINUS; 
        case "=": return KeyEvent.VK_EQUALS; 
        case "!": return KeyEvent.VK_EXCLAMATION_MARK; 
        case "@": return KeyEvent.VK_AT; 
        case "#": return KeyEvent.VK_NUMBER_SIGN; 
        case "$": return KeyEvent.VK_DOLLAR; 
        case "^": return KeyEvent.VK_CIRCUMFLEX; 
        case "&": return KeyEvent.VK_AMPERSAND; 
        case "*": return KeyEvent.VK_ASTERISK; 
        case "(": return KeyEvent.VK_LEFT_PARENTHESIS; 
        case ")": return KeyEvent.VK_RIGHT_PARENTHESIS; 
        case "_": return KeyEvent.VK_UNDERSCORE; 
        case "+": return KeyEvent.VK_PLUS; 
        case "\t": return KeyEvent.VK_TAB; 
        case "\n": return KeyEvent.VK_ENTER; 
        case "[": return KeyEvent.VK_OPEN_BRACKET; 
        case "]": return KeyEvent.VK_CLOSE_BRACKET; 
        case "\\": return KeyEvent.VK_BACK_SLASH; 
        case ";": return KeyEvent.VK_SEMICOLON; 
        case ":": return KeyEvent.VK_COLON; 
        case "\'": return KeyEvent.VK_QUOTE; 
        case "\"": return KeyEvent.VK_QUOTEDBL; 
        case ",": return KeyEvent.VK_COMMA; 
        case ".": return KeyEvent.VK_PERIOD; 
        case "/": return KeyEvent.VK_SLASH; 
        case " ": return KeyEvent.VK_SPACE; 
        case "esc": return KeyEvent.VK_ESCAPE;
        case "tab": return KeyEvent.VK_TAB;
        case "enter": return KeyEvent.VK_ENTER;
        case "arrowup": return KeyEvent.VK_UP;
        case "arrowdown": return KeyEvent.VK_DOWN;
        case "arrowleft": return KeyEvent.VK_LEFT;
        case "arrowright": return KeyEvent.VK_RIGHT;
        case "meta": return KeyEvent.VK_META;
        case "windows": return KeyEvent.VK_WINDOWS;
        case "ctrl": return KeyEvent.VK_CONTROL;
        case "control": return KeyEvent.VK_CONTROL;
        case "shft": return KeyEvent.VK_SHIFT;
        case "shift": return KeyEvent.VK_SHIFT;
        case "space": return KeyEvent.VK_SPACE;
        default:
            return -1;
        }
    }

    @Override
    public boolean equals(Object other) {
        return this==other || key==((KeyClick)other).key;
    }

    @Override
    public String toString() {
        return "KeyClick [key=" + key + ", getEndTime()=" + getEndTime() + "]";
    }

    
}
