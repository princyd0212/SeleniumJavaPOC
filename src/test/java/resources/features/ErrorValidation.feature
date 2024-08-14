@SmokeTest
Feature: Error Validation
  I want to use this template for my feature file

  @ErrorValidation
  Scenario Outline: Error Validation
    Given I landed on Ecommerce Page
    When Logged in with username <name> and password <password>
    Then <errorMessage> message is displayed

    Examples:
      | name      | password  |  errorMessage |
      | email_3   | password_3 |  errorMessage_3 |
