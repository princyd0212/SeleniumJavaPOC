package com.sam.selenium.utils;

import groovy.lang.GString;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
    private static final Properties defaultProperties = new Properties();
    private final Properties customProperties;

    // Static block to load default config.properties file
    static {
        try {
            String defaultFilePath = System.getProperty("user.dir")+"//src/test/java/resources/config/testdata.properties";
            System.out.println(defaultFilePath);
            FileInputStream fis = new FileInputStream(defaultFilePath);
            defaultProperties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load default config.properties file.");
        }
    }

    // Constructor for custom properties file
    public PropertyFileReader(String filePath) throws IOException {
        customProperties = new Properties();
        FileInputStream fis = new FileInputStream(filePath);
        customProperties.load(fis);
    }

    // Default constructor to access only the default config.properties
    public PropertyFileReader() {
        this.customProperties = defaultProperties;
    }

    // Method to get property value
    public String getProperty(String key) {
        // Check in custom properties first, fallback to default properties
        return customProperties.getProperty(key, defaultProperties.getProperty(key));
    }

    // Method to retrieve a Selenium locator
    public By getLocator(String locatorkey) {
        String locator = getProperty(locatorkey);
        if (locator == null) {
            throw new RuntimeException("Locator not found in properties file: " + locatorkey);
        }

        String[] locatorParts = locator.split(":", 2);
        String locatorType = locatorParts[0];
        String locatorValue = locatorParts[1];

        switch (locatorType) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "class":
                return By.className(locatorValue);
            case "tag":
                return By.tagName(locatorValue);
            case "linkText":
                return By.linkText(locatorValue);
            case "partialLinkText":
                return By.partialLinkText(locatorValue);
            default:
                throw new IllegalArgumentException("Locator type '" + locatorType + "' not defined!");
        }
    }
}