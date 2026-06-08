package com.pakn.dev;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        WindowHandler windowHandler = new WindowHandler();
        windowHandler.openControllerWindow();

        ActionHandler actionHandler = ActionHandler.getInstance();
        actionHandler.start();
    }
}