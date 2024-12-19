package com.sam.selenium.managers;

import com.sam.selenium.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class browserfarmmanger {
    // Helper method to set common capabilities

    public static WebDriver getBrowserStackDriver() throws MalformedURLException {
        // Get credentials and configuration from config.properties
        String browserstack_username = ConfigReader.getConfig("browserstack.username");
        String browserstack_access_key = ConfigReader.getConfig("browserstack.access_key");

        if (browserstack_username == null || browserstack_access_key == null) {
            throw new IllegalArgumentException("BrowserStack credentials are missing in the configuration file.");
        }

        // BrowserStack hub URL
        String hubUrl = "https://" + browserstack_username + ":" + browserstack_access_key + "@hub.browserstack.com/wd/hub";

        // Set BrowserStack capabilities
        DesiredCapabilities caps = new DesiredCapabilities();
        //caps.setCapability("browserstack.browserfarm_browser", ConfigReader.getConfig("browserstack.browserstack"));
        caps.setCapability("browserstack.browserName", ConfigReader.getConfig("browserstack.firefox"));
        caps.setCapability("browserstack.browser_version", ConfigReader.getConfig("browserstack.latest"));
        caps.setCapability("browserstack.os", ConfigReader.getConfig("browserstack.Windows"));
        caps.setCapability("browserstack.os_version", ConfigReader.getConfig("browserstack.10"));
        caps.setCapability("browserstack.project_name", ConfigReader.getConfig("browserstack.BrowserStack Test"));
        caps.setCapability("browserstack.buildName", ConfigReader.getConfig("browserstack.Test Build"));

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(new URL(hubUrl), caps);
    }

    public static WebDriver getAWSDriver() throws Exception {
        // AWS Device Farm setup (this is a placeholder)
        String accessKey = ConfigReader.getConfig("aws.accessKeyId");
        String secretKey = ConfigReader.getConfig("aws.secretAccessKey");
        String region = ConfigReader.getConfig("aws.region");

        // Implement AWS capabilities setup (AWS Device Farm API integration)
        // AWS setup code goes here...
        throw new UnsupportedOperationException("AWS Device Farm setup is not yet implemented.");
    }

    public static WebDriver getLambdaTestDriver() throws Exception {
        // LambdaTest configuration
        String username = ConfigReader.getConfig("lambdaTest.username");
        String accessKey = ConfigReader.getConfig("lambdaTest.accessKey");
        String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // Set LambdaTest capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("lambdaTest.browser_name")); // Fixed key name
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("lambdaTest.browser_version")); // Fixed key name
        capabilities.setCapability("platform", ConfigReader.getConfig("lambdaTest.os")); // Fixed key name
        capabilities.setCapability("platformVersion", ConfigReader.getConfig("lambdaTest.os_version")); // Fixed key name

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(new URL(gridURL), capabilities);
    }
    public static WebDriver getQyrusDriver() throws Exception {
        // Qyrus configuration
        String username = ConfigReader.getConfig("qyrus.username");
        String accessKey = ConfigReader.getConfig("qyrus.accessKey");
        String gridURL = "https://grid.qyrus.com/wd/hub";

        // Set Qyrus capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("qyrus.browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("qyrus.browser_version"));
        capabilities.setCapability("platform", ConfigReader.getConfig("qyrus.os"));

        // Prepare URL and return RemoteWebDriver instance
        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }

    public static WebDriver getSauceLabsDriver() throws Exception {
        // SauceLabs configuration
        String username = ConfigReader.getConfig("sauceLabs.username");
        String accessKey = ConfigReader.getConfig("sauceLabs.accessKey");
        String gridURL = "https://ondemand.saucelabs.com/wd/hub";

        // Set SauceLabs capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("sauceLabs.browser", ConfigReader.getConfig("sauceLabs.sauceLabs")); // Browser name
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("sauceLabs.browser_version")); // Browser version
        capabilities.setCapability("platformName", ConfigReader.getConfig("sauceLabs.os")); // Operating system name

        // Prepare the complete URL and return RemoteWebDriver instance
        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        try {
            return new RemoteWebDriver(new URL(completeURL), capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SauceLabs WebDriver. Error: " + e.getMessage(), e);
        }
    }
}