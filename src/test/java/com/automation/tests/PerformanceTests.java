package com.automation.tests;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.core.DriverFactory;
import com.automation.utils.PerformanceUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PerformanceTests extends BaseTest {

    // --- 1. WEB FRONTEND PERFORMANCE TEST ---
    @Test(priority = 1)
    public void testWebPageLoadPerformance() throws MalformedURLException {
        if (!Boolean.parseBoolean(prop.getProperty("run_performance"))) {
            throw new org.testng.SkipException("Performance testing disabled in config.");
        }

        long maxLoadTime = Long.parseLong(prop.getProperty("web_max_load_time"));
        log.info("Starting Web Performance Test. SLA limit: " + maxLoadTime + "ms");

        // 1. SET THE DRIVER inside the ThreadLocal container
        boolean runOnGrid = Boolean.parseBoolean(prop.getProperty("run_on_grid", "false"));
        driver.set(DriverFactory.initializeDriver("web", "", "", prop.getProperty("grid_url"), runOnGrid));
        
        // 2. USE getDriver() to navigate to the URL
        getDriver().get(prop.getProperty("base_url"));

        // 3. USE getDriver() when passing the driver to your utility method
        long actualLoadTime = PerformanceUtil.getPageLoadTime(getDriver());
        
        log.info("Actual Page Load Time: " + actualLoadTime + "ms");
        test.info("Page Load Time: <b>" + actualLoadTime + " ms</b> (SLA: " + maxLoadTime + " ms)");

        // Assert that the page loaded faster than our allowed SLA
        Assert.assertTrue(actualLoadTime < maxLoadTime, 
            "Page load time (" + actualLoadTime + "ms) exceeded SLA of " + maxLoadTime + "ms!");
            
        test.pass("Web performance met SLA requirements.");
    }
    

    // --- 2. API BACKEND PERFORMANCE TEST ---
    @Test(priority = 2)
    public void testApiResponsePerformance() {
        if (!Boolean.parseBoolean(prop.getProperty("run_performance"))) {
            throw new org.testng.SkipException("Performance testing disabled in config.");
        }

        long maxResponseTime = Long.parseLong(prop.getProperty("api_max_response_time"));
        log.info("Starting API Performance Test. SLA limit: " + maxResponseTime + "ms");

        // Notice there is no getDriver() here! REST Assured works independently.
        RestAssured.baseURI = prop.getProperty("api_base_url");
        
        // Execute request and capture response
        Response response = RestAssured.given().get("/users?page=2");
        
        long actualResponseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
    }
}
