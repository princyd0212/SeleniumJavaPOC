package com.sam.selenium.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src\\test\\java\\resources\\features\\ErrorValidation.feature", "src\\test\\java\\resources\\features\\SubmitOrder.feature"},
        glue = {"com/sam/selenium/stepDefinations", "stepDefinations"},
        monochrome = true,
        tags = "@SmokeTest",
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {
}
