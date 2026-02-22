package com.automation.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int count = 0;
    // Set how many extra times you want the test to run before giving up
    private static final int maxTry = 2; 

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) { // If the test failed
            if (count < maxTry) {
                count++;
                System.out.println("Retrying test " + iTestResult.getName() + " for the " + count + " time(s).");
                return true; // Returning true tells TestNG to run it again
            }
        }
        return false; // Returning false tells TestNG to accept the failure
    }
}