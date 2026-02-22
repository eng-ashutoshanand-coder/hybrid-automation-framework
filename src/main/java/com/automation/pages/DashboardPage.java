package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
    private By quickLaunchCards = By.cssSelector(".orangehrm-quick-launch-card");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Actions
    public boolean isDashboardLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeader));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getQuickLaunchItemCount() {
        // Wait until at least one quick launch card is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(quickLaunchCards));
        List<WebElement> cards = driver.findElements(quickLaunchCards);
        return cards.size();
    }
}