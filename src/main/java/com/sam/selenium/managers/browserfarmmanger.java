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

    public static WebDriver getBrowserStackDriver() throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "//src//main//java//com//sam//selenium/utils//GlobleData.properties");
        prop.load(fis);

        String username = prop.getProperty("username");
        String accessKey = prop.getProperty("access_key");
        String browserFarm = prop.getProperty("browserfarm_browser");
        String hubURL;

        if (username == null || accessKey == null) {
            throw new IllegalArgumentException("BrowserStack credentials are missing.");
        }

        if (browserFarm.equalsIgnoreCase("browserstack")) {
             hubURL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
        } else if (browserFarm.equalsIgnoreCase("lambdatest")) {
             hubURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";
        } else if (browserFarm.equalsIgnoreCase("saucelabs")) {
             hubURL = "https://" + username + ":" + accessKey + "@ondemand.saucelabs.com/wd/hub";
        } else if (browserFarm.equalsIgnoreCase("qyrus")) {
             hubURL = "https://grid.qyrus.com/wd/hub";
        } else {
            throw new IllegalArgumentException("Invalid browser farm specified: " + browserFarm);
        }

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", prop.getProperty("browser"));
        capabilities.setCapability("browserVersion", prop.getProperty("browser_version"));

        MutableCapabilities bstackOptions = new MutableCapabilities();
        bstackOptions.setCapability("os", prop.getProperty("os"));
        bstackOptions.setCapability("osVersion", prop.getProperty("os_version"));
        bstackOptions.setCapability("projectName", prop.getProperty("project_name"));
        bstackOptions.setCapability("buildName", prop.getProperty("buildName"));

        capabilities.setCapability("bstack:options", bstackOptions);

        return new RemoteWebDriver(new URL(hubURL), capabilities);
    }

}