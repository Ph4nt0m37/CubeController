package com.pakn.dev;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class WindowHandler extends Thread {

	private WebDriver driver = new ChromeDriver();
	private ActionHandler actionHandler = ActionHandler.getInstance();

	public void openControllerWindow() {
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
				String lastMove = driver.findElement(By.id("last-move")).getText();
				WebElement moveElement = driver.findElement(By.className(lastMove));
				actionHandler.addAction(new KeyClick(moveElement.getAttribute("key"), Long.valueOf(moveElement.getAttribute("time"))));
			}catch (UnreachableBrowserException | NoSuchWindowException e) {
				actionHandler.stopHandler();
				System.exit(0);
			}
		}
	}
}
