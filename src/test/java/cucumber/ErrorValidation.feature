Feature: Purchase the order from Ecommerce Website
  I want to use this template for my feature file

  @ErrorValidation
  Scenario Outline: Error Validation
    Given I landed on Ecommerce Page
    When Logged in with username <name> and password <password>
    Then "Incorrect email or password." message is displayed

    Examples:
      | name               | password  |
      | tadmin@admin.com   | Admin@12a |
