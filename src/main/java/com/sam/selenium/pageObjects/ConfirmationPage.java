package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class
ConfirmationPage extends AbstractComponent {
    private WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public ConfirmationPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
//        PageFactory.initElements(driver, this);
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

//    @FindBy(css=".hero-primary")
//    WebElement confirmationMessage;

    public String getConfirmationMessage(){
        return getElementText(propertyFileReader.getLocator("ConfirmationPage.getConfiMsg"));
    }

}
