package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import java.io.IOException;

public class CheckoutPage extends AbstractComponent {
    private WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public CheckoutPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");

    }


    public void selectCountry(String countryName) {
        Actions a = new Actions(driver);
        a.sendKeys(driver.findElement(propertyFileReader.getLocator("checkoutPage.country")), countryName).build().perform();
        waitForElementToAppear(propertyFileReader.getLocator("checkoutPage.results"));
        clickElement(propertyFileReader.getLocator("checkoutPage.selectAnyCountry"));

    }

    public ConfirmationPage submitOrder() throws IOException {
        By submitButton = propertyFileReader.getLocator("checkoutPage.submitButton");
        scrollToElement(submitButton);
        waitForElementToAppear(submitButton);
        clickElement(submitButton);
        return new ConfirmationPage(driver);
    }


}
