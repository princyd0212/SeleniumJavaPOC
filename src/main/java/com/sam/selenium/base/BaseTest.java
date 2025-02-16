package com.sam.selenium.base;

import com.sam.selenium.managers.browserfarmmanger;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import com.sam.selenium.utils.ScreenRecorderUtil;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BaseTest {
    public static WebDriver driver;
    public LandingPage landingPage;

    private static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

    public static WebDriver initializeDriver() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//com//sam//selenium/utils//GlobleData.properties");
        prop.load(fis);
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");
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
        // Capture screenshot as a file
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Define the screenshot folder (reports directory)
        File reportsDir = new File(System.getProperty("user.dir") + "/reports/");

        // If the reports directory doesn't exist, create it
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();  // Create the folder if it doesn't exist
        }

        // Define the destination path for the screenshot
        String destPath = reportsDir + "/" + testCaseName + ".png";
        File destination = new File(destPath);

        // Copy the screenshot file to the destination
        FileUtils.copyFile(source, destination);

        // Return the path where the screenshot is saved
        return destPath;
    }
}

