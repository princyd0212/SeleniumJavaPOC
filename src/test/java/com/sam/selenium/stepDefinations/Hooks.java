package com.sam.selenium.stepDefinations;

import com.sam.selenium.base.BaseTest;
import com.sam.selenium.TestComponents.Listeners;
import com.sam.selenium.utils.EmailUtility;
import io.qameta.allure.Allure;
import com.sam.selenium.utils.PropertyFileReader;
import com.sam.selenium.utils.ScreenRecorderUtil;
import com.sam.selenium.utils.VideoConversionBatch;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Hooks extends BaseTest {

    private static final Listeners listeners = new Listeners();
    private static final Logger logger = Logger.getLogger(Hooks.class.getName());

    @Before
    public void beforeScenario(Scenario scenario) {
        String scenarioName = scenario.getName();
        try {
            if (!isRecordingEnabled()) {
                System.out.println("Screen recording is disabled. Only recording failures.");
                return;
            }
            System.out.println("Recording Scenario: " + scenarioName);
            ScreenRecorderUtil.startRecording(scenarioName);
        } catch (Exception e) {
            System.err.println("Error starting recording for scenario: " + scenarioName + " - " + e.getMessage());
        }
    }

    @After
    public void takeScreenshotOnFailure(Scenario scenario) throws Exception {
        String scenarioName = scenario.getName();
        String status = scenario.isFailed() ? "Fail" : "Pass"; // Capture both Pass and Fail
        String screenshotPath = "";

        WebDriver driver = getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                try {
                    // Capture screenshot for failed test cases
                    File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    screenshotPath = getScreenshot(scenarioName.replaceAll(" ", "_") + ".png", driver);
                    FileUtils.copyFile(screenshotFile, new File(screenshotPath));

                    // Attach screenshot to Allure report
                    Allure.addAttachment(scenarioName, new ByteArrayInputStream(FileUtils.readFileToByteArray(screenshotFile)));
                    System.out.println("Scenario failed: " + scenarioName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Add test result (Pass, Fail) to the list
            listeners.getTestResults().add(
                    scenarioName + ";" + status + ";" + "N/A" + ";" + (scenario.isFailed() ? screenshotPath : "No Screenshot")
            );
        }

        if (driver != null) {
            driver.quit();
        }
    }
//sendEmailReport();



    private void attachRecordingToAllure(String recordingFilePath) {
        File recordingFile = new File(recordingFilePath);
        if (recordingFile.exists()) {
            try (FileInputStream fis = new FileInputStream(recordingFile)) {
                Allure.addAttachment("Screen Recording", "video/mp4", fis, ".mp4");
                System.out.println("MP4 recording attached to Allure report.");
            } catch (IOException e) {
                System.err.println("Error attaching recording to Allure: " + e.getMessage());
            }
        } else {
            System.err.println("Recording file not found: " + recordingFilePath);
        }
    }

    private static boolean isRecordingEnabled() throws IOException {
        String recordingFlag = new PropertyFileReader(System.getProperty("user.dir") + "//src/test/java/resources/config/testdata.properties").getProperty("screen.recording.enabled");
        boolean isHeadless = Boolean.parseBoolean(new PropertyFileReader(System.getProperty("user.dir") + "//src/test/java/resources/config/testdata.properties").getProperty("isheadless"));
        if (isHeadless) {
            System.out.println("Headless mode detected. Screen recording is disabled.");
            return false;
        }
        return "ON".equalsIgnoreCase(recordingFlag);
    }

    @AfterAll
    public static void sendEmailReport() {
        List<String> testResults = listeners.getTestResults(); // Collect all results

        if (!testResults.isEmpty()) {
            logger.info("Sending test report email with " + testResults.size() + " results (Pass/Fail/Skipped).");
            EmailUtility.sendConsolidatedEmail(testResults, "Cucumber Test Suite Execution Report");
        } else {
            logger.warning("No test results found. Email will not be sent.");
        }
    }

}


