package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import java.io.IOException;

public class LandingPage extends AbstractComponent {
    private WebDriver driver;
     PropertyFileReader propertyFileReader;

    public LandingPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

    public ProductCatalogue LoginApplication(String email, String password) throws IOException {
        enterText(propertyFileReader.getLocator("user_email_selector"), email);
        enterText(propertyFileReader.getLocator("user_password_selector"), password);
        clickElement(propertyFileReader.getLocator("login_button_selector"));
        ProductCatalogue ProductCatalogue = new ProductCatalogue(driver);
        return ProductCatalogue;
    }
    public String getErrorMessage(){
        waitForElementVisible(propertyFileReader.getLocator("error_message_selector"), 5);
//        waitForWebElementToAppear(driver.findElement(propertyFileReader.getLocator("error_message_selector")));
        return driver.findElement(propertyFileReader.getLocator("error_message_selector")).getText();
    }

    public void GoTo (){
        driver.get("https://rahulshettyacademy.com/client");
    }

}
