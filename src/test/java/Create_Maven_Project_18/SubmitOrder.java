package Create_Maven_Project_18;

import Page_Object_Factory_19.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import TestComponent.BaseTest;
import org.testng.annotations.Test;

public class SubmitOrder extends BaseTest{
    @Test
    public void submitOrder() throws IOException {
        String productName = "ZARA COAT 3";
        LandingPage landingPage = lunchApplication();
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
