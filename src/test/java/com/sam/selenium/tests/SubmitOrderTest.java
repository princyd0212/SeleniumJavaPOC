package com.sam.selenium.tests;

import com.sam.selenium.pageObjects.*;
import pageObjects.*;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.sam.selenium.TestComponents.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SubmitOrderTest extends BaseTest{
//    String productName = "ZARA COAT 3";
    @Test(dataProvider = "getData", groups = {"PurchaseOrder"})
    public void submitOrder(HashMap<String, String> input) throws IOException {

        ProductCatalogue ProductCatalogue = landingPage.LoginApplication(input.get("email"), input.get("password"));

        List<WebElement> Products = ProductCatalogue.getProductList();
        ProductCatalogue.addProductToCart(input.get("product"));
        CartPage cartPage = ProductCatalogue.goToCartPage();

        Boolean match = cartPage.VerifyProductDisplay(input.get("product"));
        Assert.assertTrue(match);
        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.selectCountry("india");
        ConfirmationPage confirmationPage = checkoutPage.submitOrder();
        String confMsg = confirmationPage.getConfirmationMessage();
        Assert.assertTrue(confMsg.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
    }


    @Test(dataProvider = "getData",dependsOnMethods = {"submitOrder"})
    public void OrderHistoryTest(HashMap<String, String> input){
        ProductCatalogue ProductCatalogue = landingPage.LoginApplication(input.get("email"), input.get("password"));
        OrderPage orderPage = ProductCatalogue.goToOrdersPage();
        Assert.assertTrue(orderPage.VerifyOrderDisplay(input.get("product")));
    }

    @DataProvider
    public Object[][] getData() throws IOException {

        List<HashMap<String,String>> data = getJsonDataToMap(System.getProperty("user.dir")+"\\src\\test\\java\\Data\\PurchaseOrder.json");
        return new Object[][] { {data.get(0)},{data.get(1)} };
    }
}

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