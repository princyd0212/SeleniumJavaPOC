package hooks;

import TestComponent.BaseTest;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.picocontainer.annotations.Inject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Hooks extends BaseTest {
    @After
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = BaseTest.getDriver();
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
    }
}