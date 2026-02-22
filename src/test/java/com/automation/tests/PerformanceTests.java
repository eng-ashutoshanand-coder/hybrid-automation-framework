package com.automation.tests;

import com.automation.core.DriverFactory;
import com.automation.utils.PerformanceUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class PerformanceTests extends BaseTest {

    // --- 1. WEB FRONTEND PERFORMANCE TEST ---
    @Test(priority = 1)
    public void testWebPageLoadPerformance() throws MalformedURLException {
        if (!Boolean.parseBoolean(prop.getProperty("run_performance"))) {
            throw new org.testng.SkipException("Performance testing disabled in config.");
        }

        long maxLoadTime = Long.parseLong(prop.getProperty("web_max_load_time"));
        log.info("Starting Web Performance Test. SLA limit: " + maxLoadTime + "ms");

        driver = DriverFactory.initializeDriver("web", "", "");
        driver.get(prop.getProperty("base_url"));

        // Capture performance metrics
        long actualLoadTime = PerformanceUtil.getPageLoadTime(driver);
        
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

        RestAssured.baseURI = "https://reqres.in/api";
        
        // Execute request and capture response
        Response response = RestAssured.given().get("/users?page=2");
        
        long actualResponseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        log.info("Actual API Response Time: " + actualResponseTime + "ms");
        test.info("API Response Time: <b>" + actualResponseTime + " ms</b> (SLA: " + maxResponseTime + " ms)");

        // Assert response time using REST Assured's built-in Matchers
        response.then().time(Matchers.lessThan(maxResponseTime));
        
        test.pass("API performance met SLA requirements.");
    }
}