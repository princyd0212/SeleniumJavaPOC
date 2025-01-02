package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

    public class ConfigReader {

        private static Properties properties = new Properties();

        static {
            try {
                // Make sure the config.properties file is in the correct path
                FileInputStream fis = new FileInputStream("D:\\Automation Project POC\\SeleniumJavaPOC\\src\\test\\java\\resources\\config\\config.properties");
                properties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static String getProperty(String key) {
            return properties.getProperty(key);
        }
    }


