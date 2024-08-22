package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class CheckoutPage extends AbstractComponent {
    private WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public CheckoutPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
//        PageFactory.initElements(driver, this);
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");

    }

//    @FindBy(xpath="//input[@placeholder='Select Country']")
//    WebElement country;
//
//    @FindBy(css=".ta-item:nth-of-type(2)")
//    WebElement selectCountry;
//
//    @FindBy(css=".action__submit")
//    WebElement submit;

//    By results = By.cssSelector(".ta-results");

    public void selectCountry(String countryName) {
        Actions a = new Actions(driver);
        a.sendKeys(driver.findElement(propertyFileReader.getLocator("checkoutPage.country")), countryName).build().perform();
        waitForElementToAppear(propertyFileReader.getLocator("checkoutPage.results"));
        driver.findElement(propertyFileReader.getLocator("checkoutPage.selectAnyCountry")).click();
    }

    public ConfirmationPage submitOrder() throws IOException {
        driver.findElement(propertyFileReader.getLocator("checkoutPage.submitButton")).click();
        return new ConfirmationPage(driver);
    }
}
