package com.sam.selenium.stepDefinations;

import com.sam.selenium.base.BaseTest;
import com.sam.selenium.TestComponents.Listeners;
import com.sam.selenium.tests.EmailUtility;
import io.qameta.allure.Allure;
import com.sam.selenium.utils.PropertyFileReader;
import com.sam.selenium.utils.ScreenRecorderUtil;
import com.sam.selenium.utils.VideoConversionBatch;
import io.cucumber.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Hooks extends BaseTest {

    // Create an instance of the Listeners class to manage test results
    private static final Listeners listeners = new Listeners();

    @Before
    public void beforeScenario(Scenario scenario){
        String scenarioName = scenario.getName();
        try {
            if (!isRecordingEnabled()) {
                System.out.println("Screen recording is disabled for all scenarios. Will record only on failure.");
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
        String testCaseName = scenario.getName();
        String status = scenario.isFailed() ? "Fail" : "Pass";
        String screenshotPath = "";

        if (scenario.isFailed()) {
            WebDriver driver = getDriver();
            if (driver != null) {
                try {
                    /*String screenshotPath = getScreenshot(scenario.getName(), driver);
                    FileInputStream fis = new FileInputStream(screenshotPath);
                    Allure.addAttachment("Screenshot", new ByteArrayInputStream(fis.readAllBytes()));*/
                    final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment(scenario.getName(), new ByteArrayInputStream(screenshot));
                    String scenarioName = scenario.getName();
                    System.out.println("Scenario failed: " + scenarioName);

                    if (!isRecordingEnabled()) {
                        if (scenario.isFailed()) {
                            System.out.println("Scenario failed: " + scenarioName + ". Starting and stopping recording for failure.");
                            ScreenRecorderUtil.startRecording(scenarioName); // Start recording for the failed scenario
                            ScreenRecorderUtil.stopRecording(); // Stop the recording immediately
                            attachRecordingToAllure(VideoConversionBatch.convertedFileName);
                        } else {
                            System.out.println("Scenario passed: " + scenarioName + ". No recording needed.");
                        }
                        return;
                    }
                    Thread.sleep(2000);
                    // Stop the recording after capturing failure context
                    ScreenRecorderUtil.stopRecording();
                    if (scenario.isFailed()) {
                        System.out.println("Scenario failed: " + scenarioName + ". Attaching recording to Allure report.");
                        // Attach recording to Allure report
                        attachRecordingToAllure(VideoConversionBatch.convertedFileName);
                    } else {
                        System.out.println("Scenario passed: " + scenarioName + ". No recording will be attached.");
                    }
                    // Log test results to Listeners, without Test Case ID and Error Message
                    listeners.getTestResults().add(
                            testCaseName + ";" + status + ";;" + screenshotPath
                    );
               //     fis.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (driver != null) {
                        driver.quit();
                    }
                }
            }
        }
    }

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
        String recordingflag = new PropertyFileReader(System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties").getProperty("screen.recording.enabled");
        boolean isheadless = Boolean.parseBoolean(new PropertyFileReader(System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties").getProperty("isheadless"));
        if (isheadless) {
            System.out.println("Headless mode detected. Screen recording is disabled.");
            return false;
        }
        return "ON".equalsIgnoreCase(recordingflag);
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