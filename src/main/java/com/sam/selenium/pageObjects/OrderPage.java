package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;

public class OrderPage extends AbstractComponent {
    private final WebDriver driver;
    private final PropertyFileReader propertyFileReader;

//    @FindBy(css=".totalRow button")
//    WebElement checkoutEle;
//
//    @FindBy(css="tr td:nth-child(3)")
//    private List<WebElement> ProductsNames;


    public OrderPage(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
//        PageFactory.initElements(driver, this);
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");

    }

    public Boolean VerifyOrderDisplay(String productName){
        List<WebElement> products = driver.findElements(propertyFileReader.getLocator("OrderPage.ProductsNames"));
        Boolean match = products.stream().anyMatch(product -> product.getText().equalsIgnoreCase(productName));
        return match;
    }

}
