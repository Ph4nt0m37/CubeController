package com.pakn.dev;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class WindowHandler extends Thread {

	private WebDriver driver;
	private ActionHandler actionHandler = ActionHandler.getInstance();

	public void openControllerWindow() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--enable-experimental-web-platform-features");
        options.addArguments("--enable-web-bluetooth-new-permissions-backend");

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability(ChromeOptions.LOGGING_PREFS, logPrefs);

        driver = new ChromeDriver(options);

		this.start();
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
                            actionHandler.addAction(new KeyClick(mouse, Long.valueOf(moveElement.getAttribute("time"))));
                        }
                        //break;
                    }
                }
			}catch (UnreachableBrowserException | NoSuchWindowException e) {
				actionHandler.stopHandler();
				System.exit(0);
			}catch (org.openqa.selenium.NoSuchElementException | NumberFormatException e) {
				continue;
			}
		}
	}
}
