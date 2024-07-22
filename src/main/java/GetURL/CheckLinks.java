package GetURL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CheckLinks {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        try {
            driver.get("https://www.paybima.com/knowledge-center/guide/health-insurance");

            List<WebElement> links = driver.findElements(By.tagName("a"));

            for (WebElement link : links) {
                String url = link.getAttribute("href");
                if (url != null && !url.isEmpty() && isValidUrl(url)) {
                    checkUrl(url, link.getText());
                }
            }
        } finally {
            driver.quit();
        }
    }

    private static boolean isValidUrl(String url) {
        return !url.equals("javascript:void(0);")
                && !url.equals("javascript:meetInPersonPopup();")
                && !url.startsWith("tel:")
                && !url.startsWith("javascript:void(0)")
                && !url.startsWith("javascript:scheduleCall();")
                && !url.startsWith("mailto:");
    }

    private static void checkUrl(String url, String linkText) {
        try {
            
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 404) {
                System.out.println("Link with 404: " + url + " (Text: " + linkText + ")");
            }
            if (url.contains("uat")) {
                System.out.println("Found 'uat' in URL: " + url + " (Text: " + linkText + ")");
            }
        } catch (IOException e) {
            System.out.println("Error checking link: " + url);
            e.printStackTrace();
        }
    }
}
