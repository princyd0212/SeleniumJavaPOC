package com.sam.selenium.managers;

import com.sam.selenium.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class browserfarmmanger {
    // Helper method to set common capabilities
    private static DesiredCapabilities getCommonCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser", ConfigReader.getConfig("browser"));
        capabilities.setCapability("browser_version", ConfigReader.getConfig("browser_version"));
        capabilities.setCapability("os", ConfigReader.getConfig("os"));
        capabilities.setCapability("os_version", ConfigReader.getConfig("os_version"));
        capabilities.setCapability("project_name", ConfigReader.getConfig("project_name"));
        capabilities.setCapability("build", ConfigReader.getConfig("build"));
        return capabilities;
    }

    public static WebDriver getBrowserStackDriver() throws MalformedURLException {
        // Get credentials and configuration from config.properties
        String browserstack_username = ConfigReader.getConfig("browserstack.username");
        String browserstack_access_key = ConfigReader.getConfig("browserstack.access_key");

        if (browserstack_username == null || browserstack_access_key == null) {
            throw new IllegalArgumentException("BrowserStack credentials are missing in the configuration file.");
        }

        String hubUrl = "https://" + browserstack_username + ":" + browserstack_access_key + "@hub.browserstack.com/wd/hub";

        // Set capabilities
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browser", ConfigReader.getConfig("browserstack"));
        caps.setCapability("browser_version", ConfigReader.getConfig("latest"));
        caps.setCapability("os", ConfigReader.getConfig("Windows"));
        caps.setCapability("os_Version", ConfigReader.getConfig("10"));
        caps.setCapability("Project_name", ConfigReader.getConfig("BrowserStack Test"));
        caps.setCapability("build", ConfigReader.getConfig("Test Build"));

        return new RemoteWebDriver(new URL(hubUrl), caps);
    }
    public static WebDriver getAWSDriver() throws Exception {
        String accessKey = ConfigReader.getConfig("aws.accessKeyId");
        String secretKey = ConfigReader.getConfig("aws.secretAccessKey");
        String region = ConfigReader.getConfig("aws.region");

        // Implement AWS capabilities setup (You can use AWS Device Farm API to set capabilities)
        // Assuming some setup for AWS Device Farm here
        // AWS setup code goes here...
        return null;
    }
    public static WebDriver getLambdaTestDriver() throws Exception {
        String username = ConfigReader.getConfig("lambdaTest.username");
        String accessKey = ConfigReader.getConfig("lambdaTest.accessKey");
        String gridURL = "https://hub.lambdatest.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("browser_version"));
        capabilities.setCapability("platform", ConfigReader.getConfig("os"));

        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }
    public static WebDriver getQyrusDriver() throws Exception {
        String username = ConfigReader.getConfig("qyrus.username");
        String accessKey = ConfigReader.getConfig("qyrus.accessKey");
        String gridURL = "https://grid.qyrus.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("browser_version"));
        capabilities.setCapability("platform", ConfigReader.getConfig("os"));

        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }

    public static WebDriver getSauceLabsDriver() throws Exception {
        String username = ConfigReader.getConfig("sauceLabs.username");
        String accessKey = ConfigReader.getConfig("sauceLabs.accessKey");
        String gridURL = "https://ondemand.saucelabs.com/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", ConfigReader.getConfig("browser"));
        capabilities.setCapability("browserVersion", ConfigReader.getConfig("browser_version"));
        capabilities.setCapability("platform", ConfigReader.getConfig("os"));

        String completeURL = "https://" + username + ":" + accessKey + "@" + gridURL;
        return new RemoteWebDriver(new URL(completeURL), capabilities);
    }

}
