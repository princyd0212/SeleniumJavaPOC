package cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/java/cucumber",
        glue = {"stepDefinations", "hooks"},
        monochrome = true,
//        tags = "@Regression",
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
//        plugin = {"html:target/cucumber.html"}
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {
}
