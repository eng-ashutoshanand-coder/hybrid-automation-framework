package com.automation.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.logging.log4j.LogManager; // NEW IMPORT
import org.apache.logging.log4j.Logger;     // NEW IMPORT
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.automation.utils.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class BaseTest {
    protected WebDriver driver;
    protected static Properties prop;
    protected static ExtentReports report;
    protected ExtentTest test;
    
    // Initialize the Logger
    protected Logger log = LogManager.getLogger(this.getClass());

    @BeforeSuite
    public void setupSuite() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        report = ExtentManager.getInstance();
        log.info("Test Suite Execution Started. Configuration loaded.");
    }

    @BeforeMethod
    public void startTest(Method method) {
        String testName = method.getName();
        test = report.createTest(testName);
        log.info("========== Starting Test: " + testName + " ==========");
        checkToggle(testName);
    }

    private void checkToggle(String testName) {
        if (testName.toLowerCase().contains("api") && !Boolean.parseBoolean(prop.getProperty("run_api"))) {
            log.warn("Skipping " + testName + " - API testing is disabled in config.");
            throw new SkipException("API Testing is disabled in config.");
        }
        if (testName.toLowerCase().contains("android") && !Boolean.parseBoolean(prop.getProperty("run_android"))) {
            log.warn("Skipping " + testName + " - Android testing is disabled in config.");
            throw new SkipException("Android Testing is disabled in config.");
        }
        if (testName.toLowerCase().contains("ios") && !Boolean.parseBoolean(prop.getProperty("run_ios"))) {
            log.warn("Skipping " + testName + " - iOS testing is disabled in config.");
            throw new SkipException("iOS Testing is disabled in config.");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                log.error("Test Failed: " + result.getThrowable().getMessage()); // Log to file
                test.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
                
                if (driver != null) {
                    String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                    test.fail("Screenshot of failure:", 
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                    log.info("Screenshot captured and attached to Extent Report.");
                }
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                log.info("Test Passed Successfully.");
                test.log(Status.PASS, "Test Passed");
            } else if (result.getStatus() == ITestResult.SKIP) {
                log.info("Test Skipped: " + result.getThrowable().getMessage());
                test.log(Status.SKIP, "Test Skipped: " + result.getThrowable().getMessage());
            }
        }

        if (driver != null) {
            driver.quit();
            log.info("Browser/Driver closed successfully.");
        }
        log.info("========== Ending Test: " + result.getMethod().getMethodName() + " ==========\n");
    }

    @AfterSuite
    public void generateReport() {
        report.flush(); 
        log.info("Extent Report generated successfully.");
    }
}