package com.sam.selenium.utils;

import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
    private final Properties properties;

    public PropertyFileReader(String filePath) throws IOException {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(filePath);
        properties.load(inputStream);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public By getLocator(String locatorkey) {
        String locator =  properties.getProperty(locatorkey);
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
                throw new IllegalArgumentException("Locator type '" + locatorType + "' not defined!!");
        }
    }
}
