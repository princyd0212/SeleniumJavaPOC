package ExtentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ExtentReportsDemo {
    ExtentReports extent;

    @BeforeTest
    public ExtentReports config(){
        String path = System.getProperty("user.dir")+ "/reports/index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Web Automation Result");
        reporter.config().setDocumentTitle("Test Results");

        extent=new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Samir");
        return extent;

    }
    @Test
    public void inialDemo(){
        ExtentTest test = extent.createTest("Initial Demo");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://rahulshettyacademy.com/");
        System.out.println(driver.getTitle());
        driver.close();
//        test.fail("Result do not match");
        extent.flush();
    }
}
