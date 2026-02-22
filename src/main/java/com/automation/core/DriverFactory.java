package com.automation.core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver initializeDriver(String platform, String appPath, String appiumUrl) throws MalformedURLException {
        WebDriver driver = null;

        switch (platform.toLowerCase()) {
            case "web":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;

            case "mobile_view":
                WebDriverManager.chromedriver().setup();
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "iPhone 12 Pro"); // Emulate iPhone on Chrome
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("mobileEmulation", mobileEmulation);
                driver = new ChromeDriver(options);
                break;

            case "android":
                UiAutomator2Options androidOptions = new UiAutomator2Options();
                androidOptions.setDeviceName("Android Emulator");
                androidOptions.setApp(appPath);
                driver = new AndroidDriver(new URL(appiumUrl), androidOptions);
                break;

            case "ios":
                XCUITestOptions iosOptions = new XCUITestOptions();
                iosOptions.setDeviceName("iPhone 14");
                iosOptions.setApp(appPath);
                driver = new IOSDriver(new URL(appiumUrl), iosOptions);
                break;
        }
        return driver;
    }
}