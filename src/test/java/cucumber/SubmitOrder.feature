@RegressionTest
Feature: Purchase the order from Ecommerce Website
  I want to use this template for my feature file

  Background:
    Given I landed on Ecommerce Page

  @Regression
  Scenario Outline: Positive Test of Submitting the order
    Given Logged in with username <email> and password <password>
    When I add product <productName> to Cart
    And Checkout <productName> and submit the order
    Then <confirmMessage> message is displayed in ConfirmationPage

    Examples:
      | email   | password   | productName   | confirmMessage   |
      | email_1 | password_1 | productName_1 | confirmMessage_1 |
      | email_2 | password_2 | productName_2 | confirmMessage_2 |

