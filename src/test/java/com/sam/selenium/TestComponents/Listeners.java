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

import java.io.IOException;
import java.util.List;

public class Listeners extends BaseTest implements ITestListener {
    ExtentTest test;
    ExtentReports extent = ExtentReporterNG.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();

    /**
     * @param result the partially filled <code>ITestResult</code>
     */
    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
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
            filePath = getScreenshot(testMethodName, driver); // Capture screenshot
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        extentTest.get().addScreenCaptureFromPath(filePath, testMethodName);
        sendFailureNotification("Test failed: " + result.getMethod().getMethodName());
        // Gather test failure details
        String testCaseId = "TC_" + testMethodName;
        String testCaseName = result.getMethod().getMethodName();
        String failedStep = "Step description not available";
        String expectedResult = "Expected result description";
        String actualResult = "Actual result description";
        String errorMessage = result.getThrowable().getMessage();
        String testSteps = "Steps leading to failure";
        String severity = "Critical";
        String testExecutionDate = "2024-12-18";
        String testEnvironment = "Chrome, Windows 10";

        // List of recipients
        List<String> recipients = List.of(
                "kartavya.b@tridhyatech.com",
                "kaushalbrahmbhattt@gmail.com"

        );

        // Send email with details and screenshot
        EmailUtility.sendEmail(recipients, testCaseId, testCaseName, failedStep, expectedResult,
                actualResult, errorMessage, testSteps, severity, testExecutionDate, testEnvironment, filePath);
    }


    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    /**
     * @param context The test context
     */
    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    /**
     * @param context The test context
     */
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
