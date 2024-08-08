package stepDefinations;

import Data.PropertyFileReader;
import Page_Object_Factory_19.*;
import TestComponent.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

public class StepDefinitionImpl extends BaseTest {

    private final PropertyFileReader propertyFileReader;
    public LandingPage landingPage;
    public ProductCatalogue productCatalogue;
    public ConfirmationPage confirmationPage;

    public StepDefinitionImpl() throws IOException {
        propertyFileReader = new PropertyFileReader(System.getProperty("user.dir")+"//src//test//java/data//config.properties");
    }

    @Given("I landed on Ecommerce Page")
    public void I_landed_on_Ecommerce_Page() throws IOException {
        landingPage = lunchApplication();
    }

    @Given("^Logged in with username (.+) and password (.+)$")
    public void Logged_in_with_username_and_password(String emailKey, String passwordKey) {
        String email = propertyFileReader.getProperty(emailKey);
        String password = propertyFileReader.getProperty(passwordKey);
        productCatalogue = landingPage.LoginApplication(email, password);
    }

    @When("^I add product (.+) to Cart$")
    public void I_add_product_to_Cart(String productKey) {
        String product = propertyFileReader.getProperty(productKey);
        List<WebElement> products = productCatalogue.getProductList();
        productCatalogue.addProductToCart(product);
    }

    @And("^Checkout (.+) and submit the order$")
    public void Checkout_and_submit_the_order(String productKey) {
        String product = propertyFileReader.getProperty(productKey);
        CartPage cartPage = productCatalogue.goToCartPage();
        Boolean match = cartPage.VerifyProductDisplay(productKey);
        Assert.assertTrue(match);
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        confirmationPage = checkoutPage.submitOrder();
    }

    @Then("^(.+) message is displayed in ConfirmationPage$")
    public void message_is_displayed_in_ConfirmationPage(String confirmationMessageKey) {
        String confirmationMessage = propertyFileReader.getProperty(confirmationMessageKey);
        String confMsg = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confMsg.equalsIgnoreCase(confirmationMessage));
        driver.close();
    }

    @Then("^(.+) message is displayed$")
    public void message_is_displayed(String errorMessageKey) {
        String errorMessage = propertyFileReader.getProperty(errorMessageKey);
        Assert.assertEquals(errorMessage, landingPage.getErrorMessage());
        driver.close();
    }
}
