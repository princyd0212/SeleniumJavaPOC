package com.sam.selenium.tests;

import com.sam.selenium.pageObjects.*;
import com.sam.selenium.retry.Retry;
import org.testng.Assert;
import java.io.IOException;

import com.sam.selenium.base.BaseTest;
import org.testng.annotations.Test;

public class SubmitOrderTest extends BaseTest {
    //    String productName = "ZARA COAT 3";
    //Add Test = dataProvider = "getData",
    @Test(groups = {"PurchaseOrder"}, retryAnalyzer = Retry.class)
    public void submitOrder() throws IOException {

        ProductCatalogue ProductCatalogue = landingPage.LoginApplication("tadmin@admin.com", "Admin@123");

        ProductCatalogue.addProductToCart("ZARA COAT 3");
        CartPage cartPage = ProductCatalogue.goToCartPage();

        Boolean match = cartPage.VerifyProductDisplay("ZARA COAT 3");
        Assert.assertTrue(match);
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();
        String confMsg = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confMsg.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
    }


    @Test(dependsOnMethods = {"submitOrder"}, retryAnalyzer = Retry.class)
    public void OrderHistoryTest() throws IOException {
        ProductCatalogue ProductCatalogue = landingPage.LoginApplication("tadmin@admin.com", "Admin@123");
        OrderPage orderPage = ProductCatalogue.goToOrdersPage();
        Assert.assertTrue(orderPage.VerifyOrderDisplay("ZARA COAT 3"));
    }
}
