package GetURL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FooterLinksCheck {
    @Test(groups={"CheckUATLinks"})
    public void FooterLinks() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://paybima.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        List<WebElement> headerLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("footer a")));
        System.out.println("Number of header links: " + headerLinks.size());

        List<String> headerURLs = new ArrayList<>();
        for (WebElement headerLink : headerLinks) {
            String headerUrl = headerLink.getAttribute("href");
            if (headerUrl != null && !headerUrl.isEmpty() && !headerUrl.equals("javascript:void(0)") && !headerUrl.equals("https://paybima.com/sitemap.xml")) {
                headerURLs.add(headerUrl);
            }
        }

        for (String headerUrl : headerURLs) {
            System.out.println("Checking header URL: " + headerUrl);

            driver.get(headerUrl);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

            List<WebElement> pageLinks = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("a")));
            System.out.println(pageLinks.size());
            for (WebElement pageLink : pageLinks) {
                String pageUrl = pageLink.getAttribute("href");
                if (pageUrl != null && !pageUrl.isEmpty()) {
                    if (pageUrl.contains("uat")) {
                        System.out.println("Found 'uat' in URL: " + pageUrl);
                    } else {
                        System.out.println(pageUrl);
                    }
                }
            }
        }
        driver.close();
    }
}
