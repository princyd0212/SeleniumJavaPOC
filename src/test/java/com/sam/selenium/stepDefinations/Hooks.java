package com.sam.selenium.stepDefinations;

import com.sam.selenium.base.BaseTest;
import com.sam.selenium.utils.PropertyFileReader;
import com.sam.selenium.utils.ScreenRecorderUtil;
import com.sam.selenium.utils.VideoConversionBatch;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Hooks extends BaseTest {

    @Before
    public void beforeScenario(Scenario scenario){
        try {
            if (!isRecordingEnabled()) {
                System.out.println("Screen recording is disabled. Skipping recording.");
                return;
            } else {
                String scenarioName = scenario.getName();
                System.out.println("Scenario failed: " + scenarioName);
                ScreenRecorderUtil.startRecording(scenarioName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void takeScreenshotOnFailure(Scenario scenario) throws Exception {
        if (scenario.isFailed()) {
            WebDriver driver = getDriver();
            if (driver != null) {
                try {
                    String screenshotPath = getScreenshot(scenario.getName(), driver);
                    FileInputStream fis = new FileInputStream(screenshotPath);
                    Allure.addAttachment("Screenshot", new ByteArrayInputStream(fis.readAllBytes()));
                    String scenarioName = scenario.getName();
                    System.out.println("Scenario failed: " + scenarioName);
                    Thread.sleep(2000);
                    // Stop the recording after capturing failure context
                    ScreenRecorderUtil.stopRecording();
                    System.out.println("Recording captured for failed scenario: " + scenarioName);
                    // Attach recording to Allure report
                    attachRecordingToAllure(VideoConversionBatch.convertedFileName);
                    fis.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }  else {
            System.out.println("Test passed: " + scenario.getName());
            ScreenRecorderUtil.stopRecording(); // Stop even if passed (no Allure attachment)
        }
       driver.quit();
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
        String flag = new PropertyFileReader(System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties").getProperty("screen.recording.enabled");
        boolean isheadless = Boolean.parseBoolean(new PropertyFileReader(System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties").getProperty("isheadless"));
        if (isheadless) {
            System.out.println("Headless mode detected. Screen recording is disabled.");
            return false;
        }
        return "ON".equalsIgnoreCase(flag);
    }


}