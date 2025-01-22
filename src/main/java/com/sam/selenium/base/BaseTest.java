package com.sam.selenium.base;

import com.sam.selenium.managers.browserfarmmanger;
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

    // Initialize Driver
    public static WebDriver initializeDriver() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//com//sam//selenium/utils//GlobleData.properties");
        prop.load(fis);

        String browserName = System.getProperty("local_browser") != null ? System.getProperty("local_browser") : prop.getProperty("local_browser");
        String runOn = prop.getProperty("runOn"); // "local" or "browserfarm"
        String browserFarm = prop.getProperty("browserfarm_browser");
        String browserstackName = System.getProperty("browserstack.browserName") != null ? System.getProperty("browserstack.browserName") : prop.getProperty("browserstack.browserName");

        if (runOn.equalsIgnoreCase("local")) {
            if (browserName.contains("chrome")) {
                ChromeOptions option = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                if (browserName.contains("headless")) {
                    option.addArguments("headless");
                }
                driver = new ChromeDriver(option);
                driver.manage().window().setSize(new Dimension(1440, 900));
            } else if (browserName.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                throw new IllegalArgumentException("Unsupported local browser: " + browserName);
            }
        } else if (runOn.equalsIgnoreCase("browserfarm")) {
            if (browserFarm.equalsIgnoreCase("browserstack")) {
                driver = browserfarmmanger.getBrowserStackDriver();
               if (browserstackName.contains("chrome")) {
                    WebDriverManager.chromedriver().setup();
                    driver.manage().window().setSize(new Dimension(1440, 900));
                } else if (browserstackName.equalsIgnoreCase("firefox")) {
                    WebDriverManager.firefoxdriver().setup();
                } else {
                    throw new IllegalArgumentException("Unsupported Farm browser: " + browserstackName);
                }// Calls the updated BrowserStack method
            } else if (browserFarm.equalsIgnoreCase("lambdaTest")) {
                driver = browserfarmmanger.getLambdaTestDriver();
            } else if (browserFarm.equalsIgnoreCase("aws")) {
                driver = browserfarmmanger.getAWSDriver();
            } else if (browserFarm.equalsIgnoreCase("qyrus")) {
                driver = browserfarmmanger.getQyrusDriver();
            } else if (browserFarm.equalsIgnoreCase("sauceLabs")) {
                driver = browserfarmmanger.getSauceLabsDriver();
            } else {
                throw new IllegalArgumentException("Unknown browser farm: " + browserFarm);
            }
        } else {
            throw new IllegalArgumentException("Unsupported run environment: " + runOn);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        tdriver.set(driver);
        return driver;
    }

    // Get Driver using in failure Screenshot
    public static WebDriver getDriver() {
        return tdriver.get();
    }

    @BeforeMethod(alwaysRun = true)
    public LandingPage launchApplication() throws IOException {
        initializeDriver();
        landingPage = new LandingPage(driver);
        landingPage.GoTo();
        return landingPage;
    }

    @AfterMethod
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Convert JSON data to Map
    public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
    }

    // Capture Screenshot
    public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/reports/" + testCaseName + ".png";
        File destination = new File(destPath);
        FileUtils.copyFile(source, destination);
        return destPath; // Return the path of the screenshot
    }
}