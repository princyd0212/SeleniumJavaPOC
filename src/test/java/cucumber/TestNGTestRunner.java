package cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(
        features = {"src/test/java/cucumber/ErrorValidation.feature", "src/test/java/cucumber/SubmitOrder.feature"},
        glue = {"stepDefinations", "hooks"},
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber-reports.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}
//        plugin = {"html:target/cucumber.html"}
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {
}
