package com.sam.selenium.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    public static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/com/sam/selenium/utils/GlobleData.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties file.", e);
        }
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }

}
