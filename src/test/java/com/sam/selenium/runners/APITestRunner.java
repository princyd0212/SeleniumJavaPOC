package com.sam.selenium.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src\\test\\java\\resources\\features\\apiTest.feature",
        glue = {"com/sam/selenium/stepDefinations"},
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/API-reports.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
)
public class APITestRunner extends AbstractTestNGCucumberTests {
}
