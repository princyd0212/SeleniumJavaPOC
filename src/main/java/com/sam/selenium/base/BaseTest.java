package com.sam.selenium.base;

import org.openqa.selenium.*;
import com.sam.selenium.pageObjects.LandingPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class BaseTest {
    public static WebDriver driver;
    public LandingPage landingPage;

    private static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

    public static WebDriver initializeDriver() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\com\\sam\\selenium\\utils\\GlobleData.properties");
        prop.load(fis);
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();
            WebDriverManager.chromedriver().setup();

            // Enable headless mode if "headless" keyword is in browserName
            if (browserName.contains("headless")) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080"); // Optional for better rendering
            }

            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(1440, 900));
        }  else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        tdriver.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        return tdriver.get();
    }

    @BeforeMethod(alwaysRun = true)
    public LandingPage lunchApplication() throws IOException {
        // Skip browser launch for API tests
        String skipBrowser = System.getProperty("skipBrowser");
        if ("true".equalsIgnoreCase(skipBrowser)) {
            System.out.println("Skipping browser launch for API tests.");
            return null;
        }

        initializeDriver();
        landingPage = new LandingPage(driver);
        landingPage.GoTo();
        return landingPage;
    }

    @AfterMethod
    public void closeDriver() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.quit();
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, String>> data = mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {
        });
        return data;
    }



    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/reports/" + testCaseName + ".png";
        File destination = new File(destPath);
        FileUtils.copyFile(source, destination);
        return destPath; // Return the path of the screenshot
    }
}
