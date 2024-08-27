package com.sam.selenium.CommonMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class CommonMethod {
    WebDriver driver;

    public CommonMethod(WebDriver driver) {
        this.driver = driver;
    }

    public void clickElement(By locator) {
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public void enterText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getElementText(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getText();
    }
}
