package com.sam.selenium.AbstractComponents;

import com.sam.selenium.CommonMethods.CommonMethod;
import com.sam.selenium.pageObjects.CartPage;
import com.sam.selenium.pageObjects.OrderPage;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class AbstractComponent extends CommonMethod {
    WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public AbstractComponent(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

//    @FindBy(xpath = "//button[@routerlink='/dashboard/cart']")
//    WebElement cartHeader;

//    @FindBy(xpath = "//button[@routerlink='/dashboard/myorders']")
//    WebElement OrdersButton;

//    public void clickElement(By locator) {
//        WebElement element = driver.findElement(locator);
//        element.click();
//    }

//    public void enterText(By locator, String text) {
//        WebElement element = driver.findElement(locator);
//        element.clear();
//        element.sendKeys(text);
//    }
//
//    public String getElementText(By locator) {
//        WebElement element = driver.findElement(locator);
//        return element.getText();
//    }





    public void waitForWebElementToAppear(WebElement findBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(findBy));
    }

    public void waitForElementToAppear(By findBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }





    public void waitForElementToDisappear(By findBy){
        WebDriverWait waitForElementDisappear = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForElementDisappear.until(ExpectedConditions.invisibilityOfElementLocated(findBy));
    }

}
