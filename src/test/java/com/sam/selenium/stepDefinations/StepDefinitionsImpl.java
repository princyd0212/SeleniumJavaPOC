package com.sam.selenium.stepDefinations;

import com.sam.selenium.utils.ExcelReader;
import com.sam.selenium.base.BaseTest;
import com.sam.selenium.managers.PageObjectManager;
import com.sam.selenium.pageObjects.*;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

public class StepDefinitionsImpl extends BaseTest {

    private PageObjectManager pageObjectManager;
    public LandingPage landingPage;
    public ProductCatalogue productCatalogue;
    public ConfirmationPage confirmationPage;
    public CartPage cartPage;
    public CheckoutPage checkoutPage;
    public OrderPage orderPage;
    private ExcelReader excelReader;

    public StepDefinitionsImpl() throws IOException {
        String excelFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config\\TestData.xlsx";
        excelReader = new ExcelReader(excelFilePath);
    }

    @Before
    public void setUp() throws IOException {
        driver = initializeDriver();  // Initialize WebDriver
        pageObjectManager = new PageObjectManager(driver);  // Pass WebDriver to PageObjectManager
        landingPage = pageObjectManager.getLandingPage();  // Use PageObjectManager to get page objects
    }

    @Given("I landed on Ecommerce Page")
    public void I_landed_on_Ecommerce_Page() throws IOException {
        landingPage.GoTo();
    }

    @Given("^Logged in with username (.+) and password (.+)$")
    public void Logged_in_with_username_and_password(String emailKey, String passwordKey) throws IOException {
        String email = excelReader.getCellData(emailKey);
        String password = excelReader.getCellData(passwordKey);
        landingPage.LoginApplication(email, password);
    }

    @When("^I add product (.+) to Cart$")
    public void I_add_product_to_Cart(String productKey) throws IOException {
        String product = excelReader.getCellData(productKey);
        productCatalogue = pageObjectManager.getProductCataloguePage();
        List<WebElement> products = productCatalogue.getProductList();
        productCatalogue.addProductToCart(product);
    }

    @And("^Checkout (.+) and submit the order$")
    public void Checkout_and_submit_the_order(String productKey) throws IOException {
        String product = excelReader.getCellData(productKey);
        productCatalogue.goToCartPage();
        cartPage = pageObjectManager.getCartPage();
        Boolean match = cartPage.VerifyProductDisplay(product);
        Assert.assertTrue(match);
        cartPage.goToCheckout();
        checkoutPage = pageObjectManager.CheckoutPage();
        checkoutPage.selectCountry("india");
        checkoutPage.submitOrder();
    }

    @Then("^(.+) message is displayed in ConfirmationPage$")
    public void message_is_displayed_in_ConfirmationPage(String confirmationMessageKey) throws IOException {
        String confirmationMessage = excelReader.getCellData(confirmationMessageKey);
        confirmationPage = pageObjectManager.getOrdersHistoryPage();
        String confMsg = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confMsg.equalsIgnoreCase(confirmationMessage));
        driver.close();
    }

    @Then("^(.+) message is displayed$")
    public void message_is_displayed(String errorMessageKey) {
        String errorMessage = excelReader.getCellData(errorMessageKey);
        Assert.assertEquals(errorMessage, landingPage.getErrorMessage());
        driver.close();
    }

}
