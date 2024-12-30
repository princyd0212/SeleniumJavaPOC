package com.sam.selenium.managers;

import com.sam.selenium.utils.ConfigReader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.IOException;
import java.net.URL;

public class browserfarmmanger {
    // Helper method to set common capabilities
    public static ConfigReader ConfigReader;

    public static RemoteWebDriver getBrowserStackDriver() throws IOException {
        // Get credentials and configuration from config.properties
        String browserstackUsername = ConfigReader.getConfig("browserstack.username");
        String browserstackAccessKey = ConfigReader.getConfig("browserstack.access_key");

        if (browserstackUsername == null || browserstackAccessKey == null) {
            throw new IllegalArgumentException("BrowserStack credentials are missing in the configuration file.");
        }

        // Set standard capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("browserstack.browserName"));

        // Set BrowserStack-specific capabilities under "bstack:options"
        MutableCapabilities bstackOptions = new MutableCapabilities();
        bstackOptions.setCapability("os", ConfigReader.getConfig("browserstack.os"));
        bstackOptions.setCapability("osVersion", ConfigReader.getConfig("browserstack.os_version"));
        bstackOptions.setCapability("browserVersion", ConfigReader.getConfig("browserstack.browser_version"));
        bstackOptions.setCapability("projectName", ConfigReader.getConfig("browserstack.project_name"));
        bstackOptions.setCapability("buildName", ConfigReader.getConfig("browserstack.buildName"));

        capabilities.setCapability("bstack:options", bstackOptions);

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(
                new URL("https://" + browserstackUsername + ":" + browserstackAccessKey + "@hub.browserstack.com/wd/hub"),
                capabilities
        );
    }

    public static WebDriver getAWSDriver() throws IOException {
        // Placeholder for AWS Device Farm setup
        throw new UnsupportedOperationException("AWS Device Farm setup is not yet implemented.");
    }

    public static WebDriver getLambdaTestDriver() throws IOException {
        // LambdaTest credentials and grid URL
        String username = ConfigReader.getConfig("lambdaTest.username");
        String accessKey = ConfigReader.getConfig("lambdaTest.accessKey");
        String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // Set W3C-compatible LambdaTest capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("lambdaTest.browser_name"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("lambdaTest.browser_version"));

        MutableCapabilities ltOptions = new MutableCapabilities();
        ltOptions.setCapability("platformName", ConfigReader.getConfig("lambdaTest.os"));
        ltOptions.setCapability("platformVersion", ConfigReader.getConfig("lambdaTest.os_version"));

        capabilities.setCapability("LT:Options", ltOptions);

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(new URL(gridURL), capabilities);
    }

    public static WebDriver getQyrusDriver() throws IOException {
        // Qyrus credentials and grid URL
        String username = ConfigReader.getConfig("qyrus.username");
        String accessKey = ConfigReader.getConfig("qyrus.accessKey");
        String gridURL = "https://grid.qyrus.com/wd/hub";

        // Set W3C-compatible Qyrus capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("qyrus.browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("qyrus.browser_version"));

        MutableCapabilities qyrusOptions = new MutableCapabilities();
        qyrusOptions.setCapability("platformName", ConfigReader.getConfig("qyrus.os"));

        capabilities.setCapability("qyrus:options", qyrusOptions);

        // Return the RemoteWebDriver instance
        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }

    public static WebDriver getSauceLabsDriver() throws IOException {
        // SauceLabs credentials and grid URL
        String username = ConfigReader.getConfig("sauceLabs.username");
        String accessKey = ConfigReader.getConfig("sauceLabs.accessKey");
        String gridURL = "https://ondemand.saucelabs.com/wd/hub";

        // Set W3C-compatible SauceLabs capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("sauceLabs.browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("sauceLabs.browser_version"));

        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("platformName", ConfigReader.getConfig("sauceLabs.os"));

        capabilities.setCapability("sauce:options", sauceOptions);

        // Return the RemoteWebDriver instance
        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }
}