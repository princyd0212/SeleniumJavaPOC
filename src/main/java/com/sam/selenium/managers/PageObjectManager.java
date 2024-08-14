package com.sam.selenium.managers;

import com.sam.selenium.pageObjects.*;
import org.openqa.selenium.WebDriver;

public class PageObjectManager {
    private WebDriver driver;

    private LandingPage landingPage;
    private ProductCatalogue ProductCatalogue;
    private CartPage cartPage;
    private CheckoutPage CheckoutPage;
    private ConfirmationPage ConfirmationPage;
    private OrderPage OrderPage;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    // Singleton pattern for LandingPage
    public LandingPage getLandingPage(){
        if(landingPage == null){
            landingPage =new LandingPage(driver);
        }
        return landingPage;
    }

    // Singleton pattern for ProductCatalogue
    public ProductCatalogue gProductCataloguePage(){
        if(ProductCatalogue == null){
            ProductCatalogue =new ProductCatalogue(driver);
        }
        return ProductCatalogue;
    }

    // Singleton pattern for CartPage
    public CartPage getCartPage(){
        if(cartPage == null){
            cartPage =new CartPage(driver);
        }
        return cartPage;
    }

    // Singleton pattern for PlaceOrder
    public CheckoutPage CheckoutPage(){
        if(CheckoutPage == null){
            CheckoutPage =new CheckoutPage(driver);
        }
        return CheckoutPage;
    }

    // Singleton pattern for ConfirmationPage
    public ConfirmationPage getOrdersHistoryPage(){
        if(ConfirmationPage == null){
            ConfirmationPage =new ConfirmationPage(driver);
        }
        return ConfirmationPage;
    }

    // Singleton pattern for ConfirmationPage
    public OrderPage OrderPagePage(){
        if(OrderPage == null){
            OrderPage =new OrderPage(driver);
        }
        return OrderPage;
    }
}
