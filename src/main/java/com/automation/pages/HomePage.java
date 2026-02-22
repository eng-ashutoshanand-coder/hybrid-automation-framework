package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;

    // Locators
    private By searchBox = By.name("q");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
    public void enterSearch(String text) {
        driver.findElement(searchBox).sendKeys(text);
        driver.findElement(searchBox).submit();
    }
}