package com.sam.selenium.AbstractComponents;

import com.sam.selenium.pageObjects.CartPage;
import com.sam.selenium.pageObjects.OrderPage;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class AbstractComponent {
    WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public AbstractComponent(WebDriver driver) throws IOException {
        this.driver = driver;
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

//    @FindBy(xpath = "//button[@routerlink='/dashboard/cart']")
//    WebElement cartHeader;

//    @FindBy(xpath = "//button[@routerlink='/dashboard/myorders']")
//    WebElement OrdersButton;

    public void waitForWebElementToAppear(WebElement findBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(findBy));
    }

    public void waitForElementToAppear(By findBy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }

    public CartPage goToCartPage() throws IOException {
        driver.findElement(propertyFileReader.getLocator("AbstractComponent.cartHeader")).click();
        CartPage cartPage = new CartPage(driver);
        return cartPage;
    }

    public OrderPage goToOrdersPage() throws IOException {
        driver.findElement(propertyFileReader.getLocator("AbstractComponent.OrdersButton")).click();
        OrderPage OrderPage = new OrderPage(driver);
        return OrderPage;
    }

    public void waitForElementToDisappear(By findBy){
        WebDriverWait waitForElementDisappear = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForElementDisappear.until(ExpectedConditions.invisibilityOfElementLocated(findBy));
    }

}
