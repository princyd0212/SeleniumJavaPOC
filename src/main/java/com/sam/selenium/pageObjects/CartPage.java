package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.io.IOException;
import java.util.List;

public class CartPage extends AbstractComponent {
    private WebDriver driver;
    private PropertyFileReader propertyFileReader;


    public CartPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;

        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

    public Boolean VerifyProductDisplay(String productName){
        List<WebElement> cartProducts = driver.findElements(propertyFileReader.getLocator("cartPage.cartProducts"));
        Boolean match = cartProducts.stream().anyMatch(product -> product.getText().equalsIgnoreCase(productName));
        return match;
    }

    public CheckoutPage goToCheckout() throws IOException {
        clickElement(propertyFileReader.getLocator("cartPage.checkoutButton"));
        return new CheckoutPage(driver);
    }

}
