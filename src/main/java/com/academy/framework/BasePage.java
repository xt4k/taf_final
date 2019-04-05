package com.academy.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    private final int EXPLICITLY_WAIT_TIMEOUT = 5;

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected void enterText(WebElement textField, String value) {
        textField.click();
        textField.clear();
        textField.sendKeys(value);
    }

    protected void waitUntilTextChanged(String locator, String oldMessage){
        WebDriverWait webDriverWait = new WebDriverWait(driver, EXPLICITLY_WAIT_TIMEOUT);

        webDriverWait.until(ExpectedConditions.not(
                ExpectedConditions.textToBe(By.cssSelector(locator), oldMessage)));
    }
}
