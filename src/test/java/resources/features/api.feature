Feature: API Tests

  Background:
    Given I authenticate via API

  Scenario: Get All Products API Test
    When I request all products

  Scenario: Product Details API Test
    When I request product details


  Scenario: Add To Cart API Test
    When I add a product to the cart

  Scenario: Checkout API Test
    When I proceed to checkout