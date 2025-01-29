Feature: API Testing using Cucumber

  Scenario: Validate POST request
    Given I have the API data for "post"
    When I send a POST request
    Then the response status code should be from JSON
    And the response body should contain from JSON

  Scenario: Validate GET request
    Given I have the API data for "get"
    When I send a GET request
    Then the response status code should be from JSON
    And the response body should contain from JSON

  Scenario: Validate PUT request
    Given I have the API data for "put"
    When I send a PUT request
    Then the response status code should be from JSON
    And the response body should contain from JSON

  Scenario: Validate DELETE request
    Given I have the API data for "delete"
    When I send a DELETE request
    Then the response status code should be from JSON
