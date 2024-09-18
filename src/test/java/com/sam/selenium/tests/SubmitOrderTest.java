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

//        List<WebElement> Products = ProductCatalogue.getProductList();
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
//    @DataProvider
//    public Object[][] getData() throws IOException {
//
//        List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"\\src\\test\\java\\Data\\PurchaseOrder.json");
//        return new Object[][] { {data.get(0)},{data.get(1)} };
//    }


//@DataProvider
//public Object[][] getData(){
//    return new Object[][] { {"tadmin@admin.com","Admin@123", "ZARA COAT 3"},{"tadmin@admin.com", "Admin@123", "ADIDAS ORIGINAL"} };
//}
//        HashMap <String, String> map = new HashMap<String, String>();
//        map.put("email" , "tadmin@admin.com");
//        map.put("password" , "Admin@123");
//        map.put("product" , "ZARA COAT 3");
//
//        HashMap <String, String> map1 = new HashMap<String, String>();
//        map1.put("email" , "tadmin@admin.com");
//        map1.put("password" , "Admin@123");
//        map1.put("product" , "ADIDAS ORIGINAL");
