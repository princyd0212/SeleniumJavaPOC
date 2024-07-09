package Create_Maven_Project_18;

import Page_Object_Factory_19.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class StandAloneTest {
    public static void main(String[] args) throws InterruptedException {
        String productName = "ZARA COAT 3";
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://rahulshettyacademy.com/client");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        LandingPage landingPage = new LandingPage(driver);
        landingPage.GoTo();
        ProductCatalogue ProductCatalogue = landingPage.LoginApplication("tadmin@admin.com", "Admin@123");
        List<WebElement> Products = ProductCatalogue.getProductList();
        ProductCatalogue.addProductToCart(productName);
        CartPage cartPage = ProductCatalogue.goToCartPage();
        Boolean match = cartPage.VerifyProductDisplay(productName);
        Assert.assertTrue(match);
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();
        String confMsg = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confMsg.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
        driver.close();
    }
}
