## Selenium-Java Automation Testing Framework ##
## 1. Overview
   This document outlines the structure, setup, and configuration of a Selenium-Java Automation Testing Framework that leverages Selenium, Java, Cucumber, TestNG, Maven, and ThreadSafe implementation to support parallel test execution. The framework integrates with Jenkins for CI/CD and Allure for detailed reporting.
________________________________________
## 2. Problem Statement
   Automation frameworks need to be scalable, reusable, and efficient across various environments and browsers. To support parallel execution, multithreading, and efficient reporting, the framework must address challenges like resource management, stability, and performance.
________________________________________
## 3. Project Goals
   •	Automate functional test cases for web applications.
   •	Support parallel test execution using ThreadSafe WebDriver management.
   •	Integrate Cucumber for BDD, TestNG for test management, and Maven for dependency management.
   •	Enable Allure reporting for detailed test results and Jenkins integration for CI/CD.
   •	Ensure scalability and maintainability of the test code.
________________________________________
## 4. Requirements
   Software
   •	Java (JDK): Java Development Kit.
   •	IDE: IntelliJ IDEA or any Java-supported IDE.
   •	Maven: Dependency management.
   •	Selenium WebDriver: For browser automation.
   •	Cucumber: For BDD-style testing.
   •	TestNG: For test execution.
   •	Allure: For reporting.
   •	Jenkins: For CI/CD.
________________________________________
## 5. Project Setup and Timeline
   Week 1: Environment Setup
   •	Configure Java, Maven, IntelliJ IDEA.
   •	Set up initial project structure in GitHub.
   Week 2: Basic Test Implementation
   •	Develop basic test scripts.
   •	Implement ThreadSafe WebDriver using ThreadLocal for parallel execution.
   •	Integrate TestNG for test management.
   Week 3: Reporting and Retry Mechanism
   •	Integrate Allure reports.
   •	Implement retry mechanisms using IRetryAnalyzer.
   •	Develop common components and utility classes.
   Week 4: CI/CD Pipeline
   •	Set up Jenkins for CI/CD.
   •	Run tests on multiple browsers using Maven profiles.
   Week 5: Testing and Refinement
   •	Perform code reviews, ensure compatibility across browsers.
   •	Refine test suites and reporting features.
________________________________________
## 6. Project Structure
   Selenium-Java-Automation-Framework/
   ├── src/
   │   ├── main/
   │   │   ├── java/
   │   │   │   ├── base/                 # Base classes for setup
   │   │   │   ├── components/           # Page components
   │   │   │   ├── pageobjects/          # Page Object Model classes
   │   │   │   └── utils/                # Common utilities
   │   │
   │   └── test/
   │       ├── java/
   │       │   ├── feature/              # Cucumber feature files
   │       │   ├── retry/                # Retry logic for failed tests
   │       │   ├── runner/               # Cucumber test runner
   │       │   ├── stepdefinitions/      # Step definition mappings
   │       │   ├── locators/             # Locator paths for UI elements
   │       │   └── tests/                # Test classes
   │       └── resources/
   │           ├── config/               # Configuration files
   │           └── utils/                # Utility files
   │
   ├── pom.xml                           # Maven dependencies
   └── testng.xml                        # TestNG configuration
________________________________________
## 7. Key Components
   7.1 ThreadSafe WebDriver Management
   •	ThreadLocal is used to ensure WebDriver instances are thread-safe during parallel test execution.
   7.2 Retry Mechanism
   •	Implemented via IRetryAnalyzer to retry failed tests, improving overall stability.
   7.3 Dynamic Test Configuration
   •	IAnnotationTransformer dynamically modifies test annotations at runtime, useful for altering parameters and test logic without hard-coding.
________________________________________
## 8. CI/CD Integration
   8.1 Jenkins Setup
1.	Install Jenkins and ensure it has access to Maven and JDK.
2.	Configure Source Code Management using Git.
3.	Define Build Triggers for periodic builds or Git-based triggers.
4.	Use Maven goals like clean test -P{profile} -D{browserName} in build steps.
5.	Configure Post-build Actions:
      o	Publish Allure test results.
      o	Optional: Add email notifications for test summaries.
________________________________________
## 9. Test Execution
   9.1 Running Tests
   •	Execute tests using TestNG XML files, enabling parallel execution across browsers.
   •	Use Maven profiles to select browser-specific test execution (-Pchrome, -Pfirefox, etc.).
   9.2 Allure Report Generation
   •	After test execution, an allure-results folder will be generated.
   •	Use the command allure serve allure-results to generate and view the report.
________________________________________
## 10. Test Reporting
    10.1 Allure Reports
    •	Overall Report: Provides a summary of the test execution.
    •	Detailed Report: Offers in-depth details of each test case, including screenshots, logs, and exceptions.
    •	Failure Report: Highlights failed tests with reasons and logs.
________________________________________
## 11. Hooks and Listeners
    11.1 Cucumber Hooks
    •	Before Hook: Runs before each test scenario, useful for setup.
    •	After Hook: Runs after each test scenario, useful for cleanup and teardown.
    11.2 TestNG Listeners
    •	ITestListener: Custom listeners to handle test events, such as logging or reporting.
________________________________________
## 12. Running Commands
    12.1 Running All Tests
    ```sh
        mvn clean test
    ```
    12.2 Running Tests with Specific Profiles
    •	Chrome:
    ```sh
        mvn clean test -Pchrome
    ```
    •	Firefox:
    ```sh
    mvn clean test -Pfirefox
    ```
    •	Edge:
    ```sh
    	mvn clean test -Pedge
    ```
    12.3 Running Tests in Parallel
    ```sh
    	mvn clean test
    ```
    12.4 Running Tests with Specific Browser Configurations
    •	Chrome with a Specific Profile:
    ```sh
        mvn clean test -Pchrome -DbrowserName=chrome
    ```
    •	Firefox with a Specific Profile:
    ```sh
        mvn clean test -Pfirefox -DbrowserName=firefox
    ```
    12.5 Allure Report Commands
    •	Generate Report:
    ```sh
        mvn allure:report
    ```
    •	Serve Report Locally:
    ```sh
        allure serve allure-results
    ```
    12.6 Running Tests on Jenkins
    ```sh
    clean test -P{profile} -D{browserName}
    ```
