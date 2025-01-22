package com.sam.selenium.stepDefinations;

import com.sam.selenium.tests.EmailUtility;
import com.sam.selenium.base.BaseTest;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import com.sam.selenium.utils.PropertyFileReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Hooks extends BaseTest {

    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = getDriver();
            if (driver != null) {
                try {
                    // Capture screenshot on failure
                    String screenshotPath = getScreenshot(scenario.getName(), driver);

                    // Prepare email details
                    String testCaseId = "TC_" + scenario.getName();
                    String testCaseName = scenario.getName();
                    String failedStep = "Step description not available";  // You may want to get this dynamically if possible
                    String expectedResult = "Expected result description";
                    String actualResult = "Actual result description";
                    String errorMessage = scenario.getStatus().toString();
                    String testSteps = "Steps leading to failure";  // Similar to failedStep, get dynamically if needed
                    String severity = "Critical";
                    String testExecutionDate = "2024-12-18";  // You can use the current date here if needed
                    String testEnvironment = "Chrome, Windows 10";  // Adjust based on your setup

                    // Create an instance of PropertyFileReader
                    PropertyFileReader propertyReader = new PropertyFileReader();

                    // Get email recipients from config file
                    String recipientsString = propertyReader.getProperty("email.recipients");
                    List<String> recipients = Arrays.asList(recipientsString.split(","));

                    // Send the failure email with the required information
                    EmailUtility.sendEmail(
                            recipients,
                            testCaseId,
                            testCaseName,
                            failedStep,
                            expectedResult,
                            actualResult,
                            errorMessage,
                            testSteps,
                            severity,
                            testExecutionDate,
                            testEnvironment,
                            screenshotPath
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Quit the driver after the test
            driver.quit();
        }
    }
}
