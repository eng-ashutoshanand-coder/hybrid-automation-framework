package com.automation.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryAnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Automatically applies the Retry logic to all tests
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}