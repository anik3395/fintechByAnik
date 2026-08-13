package org.example.fintect.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class AppiumTest {
    private static AppiumDriver driver;

    public static void main(String[] args) {

        try {
            test();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeDriver();
        }
    }

    public static void test() throws Exception {

        System.out.println("Starting Appium test...");

        DesiredCapabilities caps = new DesiredCapabilities();

        // =========================
        // Platform
        // =========================

        caps.setCapability("platformName", "Android");

        caps.setCapability("appium:automationName", "UiAutomator2");

        // =========================
        // Device
        // =========================

        caps.setCapability("appium:deviceName", "redminote 10");

        caps.setCapability("appium:udid", "R4BX3001YKR");

        // Temporarily remove platformVersion
        // caps.setCapability("appium:platformVersion", "16");

        // =========================
        // Application
        // =========================

        caps.setCapability(
                "appium:appPackage",
                "com.cardselling.agent"
        );

        caps.setCapability(
                "appium:appActivity",
                "com.newroztech.cardselling.presentstion.activity.CardsellingActivity"
        );

        caps.setCapability("appium:appWaitActivity", "*");

        // =========================
        // Appium options
        // =========================

        caps.setCapability(
                "appium:autoGrantPermissions",
                true
        );

        caps.setCapability(
                "appium:noReset",
                true
        );

        caps.setCapability(
                "appium:newCommandTimeout",
                120
        );

        // =========================
        // Start Appium Driver
        // =========================

        System.out.println("Creating Appium driver...");

        driver = new AndroidDriver(
                new URL("http://127.0.0.1:4723"),
                caps
        );

        System.out.println("App launched successfully!");

        Thread.sleep(5000);


    }

    public static void closeDriver() {

        if (driver != null) {
            driver.quit();
            System.out.println("Appium driver closed.");
        }
    }

    
}
