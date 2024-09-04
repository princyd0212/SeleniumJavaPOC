package com.sam.selenium.stepDefinations;

import com.sam.selenium.DataHelper.ReadCSVData;
import com.sam.selenium.DataHelper.ReadExcelData;
import com.sam.selenium.base.BaseTest;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Hooks extends BaseTest {

    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = getDriver();
            if (driver != null) {
                try {
                    String screenshotPath = getScreenshot(scenario.getName(), driver);
                    FileInputStream fis = new FileInputStream(screenshotPath);
                    Allure.addAttachment("Screenshot", new ByteArrayInputStream(fis.readAllBytes()));
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       driver.quit();
    }


}