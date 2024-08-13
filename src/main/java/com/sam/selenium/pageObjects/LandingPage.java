package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage extends AbstractComponent {
    WebDriver driver;

    public LandingPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
//    WebElement userEmail = driver.findElement(By.id("userEmail"));

    //PageFactory
    @FindBy(id="userEmail")
    WebElement userEmail;

    @FindBy(id="userPassword")
    WebElement passwordEle;

    @FindBy(id="login")
    WebElement submit;

    @FindBy(xpath="//div[@aria-label='Incorrect email or password.']")
    WebElement errorMessage;

    public ProductCatalogue LoginApplication(String email, String password){
        userEmail.sendKeys(email);
        passwordEle.sendKeys(password);
        submit.click();
        ProductCatalogue ProductCatalogue = new ProductCatalogue(driver);
        return ProductCatalogue;
    }
    public String getErrorMessage(){
        waitForWebElementToAppear(errorMessage);
//        System.out.println(errorMessage.getText());
        return errorMessage.getText();
    }

    public void GoTo (){
        driver.get("https://rahulshettyacademy.com/client");
    }

}
