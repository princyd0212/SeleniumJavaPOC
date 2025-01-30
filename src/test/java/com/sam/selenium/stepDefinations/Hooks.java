package com.sam.selenium.stepDefinations;

import com.sam.selenium.base.BaseTest;
import com.sam.selenium.TestComponents.Listeners;
import com.sam.selenium.tests.EmailUtility;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Hooks extends BaseTest {

    // Create an instance of the Listeners class to manage test results
    private static final Listeners listeners = new Listeners();

    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        WebDriver driver = getDriver();

        String testCaseName = scenario.getName();
        String status = scenario.isFailed() ? "Fail" : "Pass";
        String screenshotPath = "";

        if (scenario.isFailed() && driver != null) {
            try {
                // Capture screenshot for failed scenarios
                screenshotPath = getScreenshot(scenario.getName(), driver);
                FileInputStream fis = new FileInputStream(screenshotPath);
                Allure.addAttachment("Screenshot", new ByteArrayInputStream(fis.readAllBytes()));
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Log test results to Listeners, without Test Case ID and Error Message
        listeners.getTestResults().add(
                testCaseName + ";" + status + ";;" + screenshotPath
        );

        // Quit the driver after each scenario
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    public static void sendEmailReport() {
        // Get test results from Listeners
        List<String> testResults = listeners.getTestResults();

        // Send email if results are available
        if (!testResults.isEmpty()) {
            EmailUtility.sendConsolidatedEmail(testResults, "Cucumber Test Suite Execution Report");
        }
    }
}
