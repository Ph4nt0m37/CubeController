package com.pakn.dev;

import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class WindowHandler extends Thread {

	private WebDriver driver;
	private ActionHandler actionHandler = ActionHandler.getInstance();
    private static WindowHandler instance = null;

	public void openControllerWindow() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--enable-experimental-web-platform-features");
        options.addArguments("--enable-web-bluetooth-new-permissions-backend");
        options.addArguments("--disable-popup-blocking");
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability(ChromeOptions.LOGGING_PREFS, logPrefs);

        driver = new ChromeDriver(options);

		this.start();
	}

    public HashMap<String, Action> getActionMap() {
        HashMap<String, Action> actionMap = new HashMap<>();
        String[] moveArr = {"U", "U'", "D", "D'", "F", "F'", "B", "B'", "R", "R'", "L", "L'"};

        for (String move:moveArr) {
            WebElement moveElement = driver.findElement(By.className(move));

            String key = moveElement.getAttribute("key");
            String mouse = moveElement.getAttribute("mouse");
            try {
                if (key!=null) {
                    actionMap.put(move, new KeyClick(key, Long.valueOf(moveElement.getAttribute("time"))));
                }else if (mouse!=null) {
                    actionMap.put(move, new MouseClick(mouse, Long.valueOf(moveElement.getAttribute("time"))));
                }
            }catch (NumberFormatException e) {
                continue;
            }
        }

        return actionMap;
    }

    @Override
	public void run() {
        try {
            Thread.sleep(1000);   
        }catch (InterruptedException e) {
            return;
        }
        driver.get("http://127.0.0.1:8040");
		while (true) {
			try {
				driver.getTitle(); //detect window closed
                LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
                for (LogEntry entry:logs) {
                    if (entry.getMessage().contains("MOVE_DONE")) { //if MOVE_DONE log was printed, run thing
                        String lastMove = entry.getMessage().replace("\"","").split("\\|")[1];
                        WebElement moveElement = driver.findElement(By.className(lastMove));

                        String key = moveElement.getAttribute("key");
                        String mouse = moveElement.getAttribute("mouse");
                        if (key!=null) {
                            actionHandler.addAction(new KeyClick(key, Long.valueOf(moveElement.getAttribute("time"))));
                        }else if (mouse!=null) {
                            actionHandler.addAction(new MouseClick(mouse, Long.valueOf(moveElement.getAttribute("time"))));
                        }
                        //break;
                    }
                }
                driver.switchTo().alert(); //ignore alert
			}catch (UnreachableBrowserException | NoSuchWindowException e) {
				actionHandler.stopHandler();
				System.exit(0);
			}catch (org.openqa.selenium.NoSuchElementException | NumberFormatException | NoAlertPresentException | UnhandledAlertException e) {
				continue;
			}
		}
	}

    public static WindowHandler getInstance() {
        if (instance==null) instance = new WindowHandler();
        return instance;
    }
}
