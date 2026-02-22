package com.automation.core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

	public static WebDriver initializeDriver(String platform, String appPath, String appiumUrl, String gridUrl, boolean runOnGrid) throws MalformedURLException {
        WebDriver driver = null;

        switch (platform.toLowerCase()) {
            case "web":
                ChromeOptions webOptions = new ChromeOptions();
                if (runOnGrid) {
                    driver = new RemoteWebDriver(new URL(gridUrl), webOptions);
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(webOptions);
                }
                break;

            case "mobile_view":
                ChromeOptions mobileOptions = new ChromeOptions();
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "iPhone 12 Pro");
                mobileOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                
                if (runOnGrid) {
                    driver = new RemoteWebDriver(new URL(gridUrl), mobileOptions);
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(mobileOptions);
                }
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