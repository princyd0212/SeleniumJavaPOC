package GetURL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompareLinks {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // URLs to be collected
        String url1 = "https://www.tridhyatech.com/";
        String url2 = "https://storelocator.devhostserver.com/tridhya/";

        // Get all URLs from the first page
        Set<String> linksPage1 = getAllLinks(driver, url1);
        System.out.println("Links from the first page:");
        for (String link : linksPage1) {
            System.out.println(link);
        }

        // Get all URLs from the second page
        Set<String> linksPage2 = getAllLinks(driver, url2);
        System.out.println("Links from the second page:");
        Set<String> modifiedLinksPage2 = new HashSet<>();
        for (String link : linksPage2) {
            String replacedLink = link.replace("https://storelocator.devhostserver.com/tridhya/", "https://www.tridhyatech.com/");
            if (replacedLink.endsWith("/")) {
                replacedLink = replacedLink.substring(0, replacedLink.length() - 1);
            }
            modifiedLinksPage2.add(replacedLink);
            System.out.println(replacedLink);
        }
        linksPage2 = modifiedLinksPage2;

        // Find and remove the common links
        Set<String> commonLinks = new HashSet<>(linksPage1);
        commonLinks.retainAll(linksPage2);
        linksPage1.removeAll(commonLinks);
        linksPage2.removeAll(commonLinks);

        // Print the remaining links from the first page
        System.out.println("Remaining Links from the first page:");
        for (String link : linksPage1) {
            System.out.println(link);
        }

        // Print the remaining links from the second page
        System.out.println("Remaining Links from the second page:");
        for (String link : linksPage2) {
            System.out.println(link);
        }

        // Close the browser
        driver.quit();
    }

    public static Set<String> getAllLinks(WebDriver driver, String url) {
        driver.get(url);

        // Wait for the links to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("a")));

        List<WebElement> linkElements = driver.findElements(By.tagName("a"));
        Set<String> links = new HashSet<>();
        for (WebElement linkElement : linkElements) {
            String link = linkElement.getAttribute("href");
            if (link != null && !link.isEmpty()) {
                links.add(link);
            }
        }

        System.out.println("Total links found: " + links.size()); // Log the total number of links found
        return links;
    }
}
