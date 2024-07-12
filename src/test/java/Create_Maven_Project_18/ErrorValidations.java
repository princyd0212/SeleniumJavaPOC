package Create_Maven_Project_18;

import Page_Object_Factory_19.CartPage;
import Page_Object_Factory_19.ProductCatalogue;
import TestComponent.BaseTest;
import TestComponent.Retry;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class ErrorValidations extends BaseTest {

    @Test(groups = {"ErrorHandling"}, retryAnalyzer = Retry.class)

    public void LoginErrorValidation() throws IOException {
        landingPage.LoginApplication("tadmin1@admin.com", "Admin@1123");
        Assert.assertEquals("Incorrect email password.", landingPage.getErrorMessage());
    }

    @Test(groups = {"ErrorHandling"} )
    public void ProductErrorValidation() throws IOException {
        String productName = "ZARA COAT 3";
        ProductCatalogue ProductCatalogue = landingPage.LoginApplication("tadmin@admin.com", "Admin@123");

        List<WebElement> Products = ProductCatalogue.getProductList();
        ProductCatalogue.addProductToCart(productName);
        CartPage cartPage = ProductCatalogue.goToCartPage();

        Boolean match = cartPage.VerifyProductDisplay("ZARA COAT 3");
        Assert.assertTrue(match);
    }
}
