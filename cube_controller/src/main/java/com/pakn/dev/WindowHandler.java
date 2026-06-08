package com.pakn.dev;

import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class WindowHandler extends Thread {

	private WebDriver driver = new ChromeDriver();

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
			}catch (UnreachableBrowserException | NoSuchWindowException e) {
				System.exit(0);
			}
		}
	}
}
