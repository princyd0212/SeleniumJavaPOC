package com.sam.selenium.stepDefinations;

import com.sam.selenium.tests.EmailUtility;
import com.sam.selenium.base.BaseTest;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import utils.ConfigReader;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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
                    String screenshotPath = getScreenshot(scenario.getName(), driver);

                    // Prepare email details
                    String testCaseId = "TC_" + scenario.getName();
                    String testCaseName = scenario.getName();
                    String failedStep = "Step description not available";
                    String expectedResult = "Expected result description";
                    String actualResult = "Actual result description";
                    String errorMessage = scenario.getStatus().toString();
                    String testSteps = "Steps leading to failure";
                    String severity = "Critical";
                    String testExecutionDate = "2024-12-18";
                    String testEnvironment = "Chrome, Windows 10";

                    // List of recipients
                    // Get recipients from the config file
                    String recipientsString = ConfigReader.getProperty("email.recipients");
                    List<String> recipients = Arrays.asList(recipientsString.split(","));

                    // Send email
                    EmailUtility.sendEmail(recipients,testCaseId, testCaseName, failedStep, expectedResult,
                            actualResult, errorMessage, testSteps, severity, testExecutionDate, testEnvironment, screenshotPath);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            driver.quit();
        }
    }
}