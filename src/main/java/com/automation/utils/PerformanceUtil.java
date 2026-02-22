package com.automation.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class PerformanceUtil {

    public static long getPageLoadTime(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // Get the exact millisecond the browser started navigating
        Number navigationStart = (Number) js.executeScript("return window.performance.timing.navigationStart;");
        
        // Get the exact millisecond the page finished loading completely
        Number loadEventEnd = (Number) js.executeScript("return window.performance.timing.loadEventEnd;");
        
        // Calculate total load time
        if (loadEventEnd != null && navigationStart != null) {
            return loadEventEnd.longValue() - navigationStart.longValue();
        }
        return 0;
    }
}