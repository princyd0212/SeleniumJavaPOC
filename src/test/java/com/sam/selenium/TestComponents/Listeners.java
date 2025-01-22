package com.sam.selenium.TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.sam.selenium.base.BaseTest;
import com.sam.selenium.tests.EmailUtility;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.sam.selenium.utils.ExtentReporterNG;
import com.sam.selenium.utils.PropertyFileReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Listeners extends BaseTest implements ITestListener {
    ExtentTest test;
    ExtentReports extent = ExtentReporterNG.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Store results for all tests
    private List<String> testResults = new ArrayList<>();

    private PropertyFileReader propertyReader;

    public Listeners() {
        try {
            propertyReader = new PropertyFileReader("D:/Automation Project POC/SeleniumJavaPOC/src/test/java/resources/config/testdata.properties");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize PropertyFileReader");
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");

        String testCaseId = "TC_" + result.getMethod().getMethodName();
        String testCaseName = result.getMethod().getMethodName();
        String status = "Pass";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";
        String errorMessage = "";

        testResults.add(testCaseId + ";" + testCaseName + ";" + status + ";" + duration + ";" + errorMessage);
        System.out.println(testCaseId + " Passed.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().fail(result.getThrowable());
        String testMethodName = result.getMethod().getMethodName();

        try {
            driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        String filePath = null;
        try {
            filePath = getScreenshot(testMethodName, driver);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        extentTest.get().addScreenCaptureFromPath(filePath, testMethodName);

        String testCaseId = "TC_" + testMethodName;
        String testCaseName = result.getMethod().getMethodName();
        String status = "Fail";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";
        String errorMessage = result.getThrowable().getMessage();

        // Add result with filePath (screenshot path)
        testResults.add(testCaseId + ";" + testCaseName + ";" + status + ";" + duration + ";" + errorMessage + ";" + filePath);
        System.out.println(testCaseId + " Failed.");

        String failedStep = "Step description not available";
        String expectedResult = "Expected result description";
        String actualResult = "Actual result description";
        String testSteps = "Steps leading to failure";
        String severity = "Critical";
        String testExecutionDate = "2024-12-18";
        String testEnvironment = "Chrome, Windows 10";

        // Get recipients from the config file
        String recipientsString = propertyReader.getProperty("email.recipients");
        List<String> recipients = Arrays.asList(recipientsString.split(","));

        // Send email
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
                filePath
        );
        String failureMessage = "Test " + result.getMethod().getMethodName() + " failed: " + result.getThrowable().getMessage();
        sendFailureNotification(failureMessage);
    }

    private void sendConsolidatedEmail() {
        String subject = "Test Suite Execution Report";
        StringBuilder body = new StringBuilder("Test Suite Results:\n\n");

        // Loop through the testResults and append each result
        for (String result : testResults) {
            String[] resultDetails = result.split(";");

            // Declare variables to hold file path and error message
            String filePath = "No screenshot available"; // Default value
            String errorMessage = "No error message";    // Default value

            // Check if the array has enough elements
            if (resultDetails.length > 5) {
                filePath = resultDetails[5];  // Screenshot file path is the 6th element (index 5)
            }

            if (resultDetails.length > 4) {
                errorMessage = resultDetails[4];  // Error message is the 5th element (index 4)
            }

            body.append("Test Case ID: ").append(resultDetails[0])
                    .append("\nTest Case Name: ").append(resultDetails[1])
                    .append("\nStatus: ").append(resultDetails[2])
                    .append("\nDuration: ").append(resultDetails[3])
                    .append("\nError Message: ").append(errorMessage)
                    .append("\nScreenshot: ").append(filePath).append("\n\n");
        }

        String recipientsString = propertyReader.getProperty("email.recipients");
        List<String> recipients = Arrays.asList(recipientsString.split(","));

        // Assuming you don't have any attachments to send
        List<String> attachments = new ArrayList<>();
        // Add screenshot paths as attachments (if needed)
        for (String result : testResults) {
            String[] resultDetails = result.split(";");

            // Only add screenshot to attachments if it's available
            if (resultDetails.length > 5) {
                attachments.add(resultDetails[5]);  // Add screenshot path from test result
            }
        }

        // Now calling the sendConsolidatedEmail method with all required parameters
        EmailUtility.sendConsolidatedEmail(recipients, testResults, subject, body.toString(), attachments);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testCaseId = "TC_" + result.getMethod().getMethodName();
        String testCaseName = result.getMethod().getMethodName();
        String status = "Skipped";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";
        String errorMessage = "";

        testResults.add(testCaseId + ";" + testCaseName + ";" + status + ";" + duration + ";" + errorMessage);
        System.out.println(testCaseId + " Skipped.");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();

        sendConsolidatedEmail();
    }

}
