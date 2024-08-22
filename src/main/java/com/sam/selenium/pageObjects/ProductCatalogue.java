package com.sam.selenium.pageObjects;

import com.sam.selenium.AbstractComponents.AbstractComponent;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.List;

public class ProductCatalogue extends AbstractComponent {
    private WebDriver driver;
    private PropertyFileReader propertyFileReader;

    public ProductCatalogue(WebDriver driver) throws IOException {
        super(driver);
        this.driver = driver;
//        PageFactory.initElements(driver, this);
        this.propertyFileReader = new PropertyFileReader(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\Xpath.properties");
    }

    //    List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
    //PageFactory
//    @FindBy(css = ".mb-3")
//    List<WebElement> products;
//
//    @FindBy(css = ".ng-animating")
//    WebElement spinner;
//
//    By productBy = By.cssSelector(".mb-3");
//    By addToCart = By.cssSelector(".card-body button:last-of-type");
//    By toastMessage = By.cssSelector("#toast-container");

    public List<WebElement> getProductList() {
        waitForElementToAppear(propertyFileReader.getLocator("productCatalogue.products"));
        return driver.findElements(propertyFileReader.getLocator("productCatalogue.products"));
    }

    public WebElement getProductByName(String productName) {
        WebElement prod = getProductList().stream().filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst().orElse(null);
        return prod;
    }

    public void addProductToCart(String productName) {
        WebElement prod = getProductByName(productName);
        prod.findElement(propertyFileReader.getLocator("productCatalogue.addToCart")).click();
        waitForElementToAppear(propertyFileReader.getLocator("productCatalogue.toastMessage"));
        waitForElementToDisappear(propertyFileReader.getLocator("productCatalogue.spinner"));
    }


}
