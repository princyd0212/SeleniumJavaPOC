package com.sam.selenium.managers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static java.lang.System.getProperty;

public class browserfarmmanger {
    // Helper method to set common capabilities

    public static RemoteWebDriver getBrowserStackDriver() throws IOException {
        // Get credentials and configuration from config.properties


        Properties prop = new Properties();
        prop.load(new FileInputStream(getProperty("user.dir") + "//src//main//java//com//sam//selenium/utils//GlobleData.properties"));
        String browserstackUsername = prop.getProperty("browserstack.username");
        String browserstackAccessKey = prop.getProperty("browserstack.access_key");

        if (browserstackUsername == null || browserstackAccessKey == null) {
            throw new IllegalArgumentException("BrowserStack credentials are missing in the configuration file.");
        }

        // Set standard capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", getProperty("browserstack.browserName"));

        // Set BrowserStack-specific capabilities under "bstack:options"
        MutableCapabilities bstackOptions = new MutableCapabilities();
        bstackOptions.setCapability("os", getProperty("browserstack.os"));
        bstackOptions.setCapability("osVersion", getProperty("browserstack.os_version"));
        bstackOptions.setCapability("browserVersion", getProperty("browserstack.browser_version"));
        bstackOptions.setCapability("projectName", getProperty("browserstack.project_name"));
        bstackOptions.setCapability("buildName", getProperty("browserstack.buildName"));

        capabilities.setCapability("bstack:options", bstackOptions);

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(
                new URL("https://" + browserstackUsername + ":" + browserstackAccessKey + "@hub.browserstack.com/wd/hub"),
                capabilities
        );
    }

    public static WebDriver getLambdaTestDriver() throws IOException {
        // LambdaTest credentials and grid URL
        String username = getProperty("lambdaTest.username");
        String accessKey = getProperty("lambdaTest.accessKey");
        String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        // Set W3C-compatible LambdaTest capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", getProperty("lambdaTest.browser_name"));
        capabilities.setCapability("browserVersion", getProperty("lambdaTest.browser_version"));

        MutableCapabilities ltOptions = new MutableCapabilities();
        ltOptions.setCapability("platformName", getProperty("lambdaTest.os"));
        ltOptions.setCapability("platformVersion", getProperty("lambdaTest.os_version"));
        ltOptions.setCapability("buildName", getProperty("lambdaTest.buildName"));

        capabilities.setCapability("LT:Options", ltOptions);

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(new URL(gridURL), capabilities);
    }

    public static WebDriver getAWSDriver() throws IOException {
        // Placeholder for AWS Device Farm setup
        throw new UnsupportedOperationException("AWS Device Farm setup is not yet implemented.");
    }

    public static WebDriver getQyrusDriver() throws IOException {
        // Qyrus credentials and grid URL
        String username = getProperty("qyrus.username");
        String accessKey = getProperty("qyrus.accessKey");
        String gridURL = "https://grid.qyrus.com/wd/hub";

        // Set W3C-compatible Qyrus capabilities
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", getProperty("qyrus.browser"));
        capabilities.setCapability("browserVersion", getProperty("qyrus.browser_version"));

        MutableCapabilities qyrusOptions = new MutableCapabilities();
        qyrusOptions.setCapability("platformName", getProperty("qyrus.os"));

        capabilities.setCapability("qyrus:options", qyrusOptions);

        // Return the RemoteWebDriver instance
        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }

    public static WebDriver getSauceLabsDriver() throws IOException {
        // Fetch SauceLabs credentials from the configuration
        String sauceUsername = getProperty("sauceLabs.username");
        String sauceAccessKey = getProperty("sauceLabs.accessKey");

        if (sauceUsername == null || sauceAccessKey == null) {
            throw new IllegalArgumentException("SauceLabs credentials are missing in the configuration file.");
        }

        // Construct the SauceLabs grid URL
        String gridURL = "https://" + sauceUsername + ":" + sauceAccessKey + "@ondemand.saucelabs.com/wd/hub";
        System.out.println("Grid URL: " + gridURL); // Debugging the URL

        // Set W3C-compatible capabilities for SauceLabs
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", getProperty("sauceLabs.browser_name"));
        capabilities.setCapability("browserVersion", getProperty("sauceLabs.browser_version"));

        // Set Sauce-specific options under "sauce:options"
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("platformName", getProperty("sauceLabs.os"));
        sauceOptions.setCapability("platformVersion", getProperty("sauceLabs.os_version"));
        sauceOptions.setCapability("buildName", getProperty("sauceLabs.buildName"));

        capabilities.setCapability("sauce:options", sauceOptions);
        System.out.println(sauceUsername);
        System.out.println(sauceAccessKey);

        // Return the RemoteWebDriver instance
        return new RemoteWebDriver(new URL(gridURL), capabilities);
    }
}