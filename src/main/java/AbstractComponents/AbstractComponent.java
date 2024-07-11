package AbstractComponents;

import Page_Object_Factory_19.CartPage;
import Page_Object_Factory_19.OrderPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AbstractComponent {
WebDriver driver;

    public AbstractComponent(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(xpath="//button[@routerlink='/dashboard/cart']")
    WebElement cartHeader;

    @FindBy(xpath ="//button[@routerlink='/dashboard/myorders']")
    WebElement OrdersButton;

    public void waitForWebElementToAppear(WebElement findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(findBy));
    }

    public void waitForElementToAppear(By findBy){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
    }

    public CartPage goToCartPage(){
        cartHeader.click();
        CartPage cartPage = new CartPage(driver);
        return cartPage;
    }

    public OrderPage goToOrdersPage(){
        OrdersButton.click();
        OrderPage OrderPage = new OrderPage(driver);
        return OrderPage;
    }

    public void waitForElementToDisappear(WebElement ele){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.invisibilityOf(ele));
    }

}
