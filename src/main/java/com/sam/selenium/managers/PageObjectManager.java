package com.sam.selenium.managers;

import com.sam.selenium.pageObjects.*;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class PageObjectManager{
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

    public LandingPage getLandingPage() throws IOException {
        if(landingPage == null){
            landingPage =new LandingPage(driver);
        }
        return landingPage;
    }

    public ProductCatalogue getProductCataloguePage() throws IOException {
        if(ProductCatalogue == null){
            ProductCatalogue =new ProductCatalogue(driver);
        }
        return ProductCatalogue;
    }

    public CartPage getCartPage() throws IOException {
        if(cartPage == null){
            cartPage =new CartPage(driver);
        }
        return cartPage;
    }

    public CheckoutPage CheckoutPage() throws IOException {
        if(CheckoutPage == null){
            CheckoutPage =new CheckoutPage(driver);
        }
        return CheckoutPage;
    }

    public ConfirmationPage getOrdersHistoryPage() throws IOException {
        if(ConfirmationPage == null){
            ConfirmationPage =new ConfirmationPage(driver);
        }
        return ConfirmationPage;
    }

    public OrderPage OrderPagePage() throws IOException {
        if(OrderPage == null){
            OrderPage =new OrderPage(driver);
        }
        return OrderPage;
    }
}
