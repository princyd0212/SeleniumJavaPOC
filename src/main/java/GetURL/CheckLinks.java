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
        String[] urls = {
                "https://www.paybima.com/blog/health-insurance/preventive-health-check-up-and-its-tax-benefit",
                "https://www.paybima.com/blog/car-insurance/know-about-bharat-series-number-plates",
                "https://www.paybima.com/blog/bike-insurance/top-lightweight-scooty-in-india",
                "https://www.paybima.com/blog/term-insurance/mdrt-in-lic-complete-information-about-mdrt-in-lic",
                "https://www.paybima.com/blog/life-insurance/how-to-change-nominee-in-lic-online",
                "https://www.paybima.com/blog/investment-and-tax-planning/how-to-find-out-the-lic-premium-amount-without-tax",
                "https://www.paybima.com/blog/health-and-wellness/best-homeopathic-medicine-for-hair-fall-regrowth",
                "https://www.paybima.com/blog/motor-insurance/rto-fine-for-using-tinted-glass",
                "https://www.paybima.com/blog/miscellaneous/union-budget-2024-25-what-it-holds-for-the-insurance-sector"
        };
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        try {

            for (String url : urls) {
                driver.get(url);
                List<WebElement> links = driver.findElements(By.tagName("a"));
                System.out.println("URL: " + url + " has " + links.size() + " links.");

                for (WebElement link : links) {
                    String urlA = link.getAttribute("href");
                    if (urlA != null && !urlA.isEmpty() && isValidUrl(urlA)) {
                        checkUrl(urlA, link.getText());
                    }
                }
            }
            } finally{
                driver.quit();
            }
        }

        private static boolean isValidUrl (String url){
            return !url.equals("javascript:void(0);")
                    && !url.equals("javascript:meetInPersonPopup();")
                    && !url.startsWith("tel:")
                    && !url.startsWith("javascript:void(0)")
                    && !url.startsWith("javascript:scheduleCall();")
                    && !url.startsWith("mailto:")
                    && !url.startsWith("javascript:resetTab();")
                    && !url.startsWith("javascript:connectNow();");
        }

        private static void checkUrl (String url, String linkText){
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
