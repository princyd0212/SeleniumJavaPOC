package GetURL;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CombinedCompare {
    public static void main(String[] args) {
        WebDriver driver1 = new ChromeDriver();
        driver1.manage().window().maximize();
        WebDriver driver2 = new ChromeDriver();
        driver2.manage().window().maximize();

        try {
            // URLs to compare
            String url1 = "https://www.paybima.com/life-insurance/term-insurance/saral-jeevan-bima";
            String url2 = "https://uat.paybima.com/life-insurance/term-insurance/saral-jeevan-bima";

            // Navigate to the URLs
            driver1.get(url1);
            driver2.get(url2);

            // Compare entire page content
            comparePageContent(driver1, driver2);

            // Compare breadcrumbs
            compareBreadcrumbs(driver1, driver2);

            // Compare headers (h1, h2, h3)
            compareHeaders(driver1, driver2);

            // Check for 404 errors on all links
            checkLinksFor404(driver1, url1);

        } finally {
            driver1.quit();
            driver2.quit();
        }
    }

    private static void comparePageContent(WebDriver driver1, WebDriver driver2) {
        WebElement body1 = driver1.findElement(By.tagName("body"));
        String text1 = body1.getText();

        WebElement body2 = driver2.findElement(By.tagName("body"));
        String text2 = body2.getText();

        List<String> lines1 = Arrays.asList(text1.split("\n"));
        List<String> lines2 = Arrays.asList(text2.split("\n"));

        Patch<String> patch = DiffUtils.diff(lines1, lines2);

        System.out.println("Comparing entire page content:");
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            System.out.println(delta);
        }
    }

    private static void compareBreadcrumbs(WebDriver driver1, WebDriver driver2) {
        List<String> breadcrumbs1 = getBreadcrumbs(driver1);
        List<String> breadcrumbs2 = getBreadcrumbs(driver2);

        System.out.println("Comparing breadcrumbs:");
        if (breadcrumbs1.equals(breadcrumbs2)) {
            System.out.println("Breadcrumbs are the same.");
        } else {
            System.out.println("Breadcrumbs are different.");
            compareLists(breadcrumbs1, breadcrumbs2);
        }
    }

    private static List<String> getBreadcrumbs(WebDriver driver) {
        List<WebElement> breadcrumbElements = driver.findElements(By.cssSelector(".breadcrumb .breadcrumb-item"));
        return breadcrumbElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private static void compareHeaders(WebDriver driver1, WebDriver driver2) {
        List<String> headers1 = getHeaders(driver1);
        List<String> headers2 = getHeaders(driver2);

        Patch<String> patch = DiffUtils.diff(headers1, headers2);

        System.out.println("Comparing headers (h1, h2, h3):");
        for (AbstractDelta<String> delta : patch.getDeltas()) {
            System.out.println(delta);
            if (!delta.getSource().getLines().isEmpty() && delta.getTarget().getLines().isEmpty()) {
                System.out.println("Missing in URL 2: " + delta.getSource().getLines());
            } else if (delta.getSource().getLines().isEmpty() && !delta.getTarget().getLines().isEmpty()) {
                System.out.println("Missing in URL 1: " + delta.getTarget().getLines());
            } else {
                System.out.println("Different in both URLs:");
                System.out.println("URL 1: " + delta.getSource().getLines());
                System.out.println("URL 2: " + delta.getTarget().getLines());
            }
        }
    }

    private static List<String> getHeaders(WebDriver driver) {
        List<String> headers = new ArrayList<>();
        List<WebElement> h1Elements = driver.findElements(By.tagName("h1"));
        List<WebElement> h2Elements = driver.findElements(By.tagName("h2"));
        List<WebElement> h3Elements = driver.findElements(By.tagName("h3"));

        for (WebElement element : h1Elements) {
            headers.add(element.getText());
        }
        for (WebElement element : h2Elements) {
            headers.add(element.getText());
        }
        for (WebElement element : h3Elements) {
            headers.add(element.getText());
        }

        return headers;
    }

    private static void checkLinksFor404(WebDriver driver, String baseUrl) {
        List<WebElement> links = driver.findElements(By.tagName("a"));

        System.out.println("Checking for 404 errors in links:");
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && !url.isEmpty() && url.startsWith(baseUrl)) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();

                    if (responseCode == 404) {
                        System.out.println("404 error in link: " + url + " (Text: " + link.getText() + ")");
                    }

                } catch (IOException e) {
                    System.out.println("Error checking link: " + url);
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compareLists(List<String> list1, List<String> list2) {
        int size = Math.max(list1.size(), list2.size());

        for (int i = 0; i < size; i++) {
            String item1 = i < list1.size() ? list1.get(i) : "MISSING";
            String item2 = i < list2.size() ? list2.get(i) : "MISSING";

            if (!item1.equals(item2)) {
                System.out.println("Difference at index " + i + ":");
                System.out.println("URL 1: " + item1);
                System.out.println("URL 2: " + item2);
            }
        }
    }
}
