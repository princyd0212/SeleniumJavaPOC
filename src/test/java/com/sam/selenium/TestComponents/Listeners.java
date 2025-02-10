package com.sam.selenium.TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.sam.selenium.base.BaseTest;
import com.sam.selenium.utils.EmailUtility;
import com.sam.selenium.utils.ExtentReporterNG;
import com.sam.selenium.utils.PropertyFileReader;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Listeners extends BaseTest implements ITestListener {
    ExtentTest test;
    ExtentReports extent = ExtentReporterNG.getReportObject();
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // Store results for all tests
    private final List<String> testResults = new ArrayList<>();

    private PropertyFileReader propertyReader;

    // Static flag to prevent duplicate email sending
    private static final AtomicBoolean emailSent = new AtomicBoolean(false);

    public Listeners() {
        try {
            propertyReader = new PropertyFileReader(System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties");
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

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");

        String testCaseName = result.getMethod().getMethodName();
        String status = "Pass";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";

        testResults.add(testCaseName + ";" + status + ";" + duration);
        System.out.println(testCaseName + " Passed.");
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
            filePath = getScreenshot(testMethodName, driver);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        extentTest.get().addScreenCaptureFromPath(filePath, testMethodName);
        String testCaseName = result.getMethod().getMethodName();
        String status = "Fail";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";
        testResults.add(testCaseName + ";" + status + ";" + duration + ";" + filePath);
        System.out.println("Screenshot saved at: " + filePath);

        System.out.println(testCaseName + " Failed.");
    }

    /**
     * @param result <code>ITestResult</code> containing information about the run test
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testCaseName = result.getMethod().getMethodName();
        String status = "Skipped";
        String duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0 + "s";

        testResults.add(testCaseName + ";" + status + ";" + duration);
        System.out.println(testCaseName + " Skipped.");
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

        if (!testResults.isEmpty()) {
            EmailUtility.sendConsolidatedEmail(testResults, "Test Suite Execution Report"); // Send the consolidated email at the end
        }
    }

    // Public method to access the test results
    public List<String> getTestResults() {
        return testResults;
    }
}