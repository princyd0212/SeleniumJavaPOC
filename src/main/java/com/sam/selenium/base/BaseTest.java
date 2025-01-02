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
            ChromeOptions option = new ChromeOptions();
            WebDriverManager.chromedriver().setup();
            if (browserName.contains("headless")) {
                option.addArguments("headless");
            }
            driver = new ChromeDriver(option);
            driver.manage().window().setSize(new Dimension(1440, 900));
        } else if (browserName.equalsIgnoreCase("firefox")) {
            WebDriver driver = new FirefoxDriver();
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
    public static void sendFailureNotification(String failureMessage) {
        String webhookUrl = "https://contcentricpvtltd.webhook.office.com/webhookb2/1e82d6a2-3afe-4834-a55d-891d9d1592c7@92df81cd-dcf2-490a-884c-13b58b3a8ca6/IncomingWebhook/7794e09a8ad24f898b170f53b4826638/a98cea20-3ca3-474e-9414-4d238c6c92a0/V2eh5nQsofcLs38I-YRLuqPV370fbBDJQ4yDiBa7slRRQ1";
        String jsonPayload = String.format(
                "{ \"text\": \"🚨 Test Failure Alert: %s\" }",
                failureMessage
        );

        try (var client = org.apache.hc.client5.http.impl.classic.HttpClients.createDefault()) {
            org.apache.hc.client5.http.classic.methods.HttpPost httpPost = new org.apache.hc.client5.http.classic.methods.HttpPost(webhookUrl);
            org.apache.hc.core5.http.io.entity.StringEntity entity = new org.apache.hc.core5.http.io.entity.StringEntity(jsonPayload, org.apache.hc.core5.http.ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            try (org.apache.hc.client5.http.impl.classic.CloseableHttpResponse response = client.execute(httpPost)) {
                System.out.println("Notification sent. Response code: " + response.getCode());
            }
        } catch (Exception ex) {
            System.err.println("Failed to send notification: " + ex.getMessage());
        }
    }

}

