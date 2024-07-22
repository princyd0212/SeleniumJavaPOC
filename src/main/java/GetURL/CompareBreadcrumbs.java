package GetURL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import java.util.List;
import java.util.stream.Collectors;

public class CompareBreadcrumbs {
    public static void main(String[] args) {
        WebDriver driver1 = new ChromeDriver();
        driver1.manage().window().maximize();
        WebDriver driver2 = new ChromeDriver();
        driver2.manage().window().maximize();
        try {
            String url1 = "https://www.paybima.com/motor-insurance/passenger-carrying-vehicle-insurance";
            String url2 = "https://uat.paybima.com/motor-insurance/passenger-carrying-vehicle-insurance";
            driver1.get(url1);
            String urlProd = driver1.getCurrentUrl();
            List<String> breadcrumbs1 = getBreadcrumbs(driver1);

            driver2.get(url2);
            String urlPreProd = driver2.getCurrentUrl();
            List<String> breadcrumbs2 = getBreadcrumbs(driver2);


            System.out.println("urlProd: " + urlProd);
            System.out.println("Breadcrumbs 1: " + breadcrumbs1);
            System.out.println("urlPreProd: " + urlPreProd);
            System.out.println("Breadcrumbs 2: " + breadcrumbs2);

            if (breadcrumbs1.equals(breadcrumbs2)) {
                System.out.println("Breadcrumbs are the same.");
            } else {
                System.out.println("Breadcrumbs are different.");
                System.out.println("Differences:");
                compareBreadcrumbs(breadcrumbs1, breadcrumbs2);
            }
        } finally {

            driver1.quit();
            driver2.quit();
        }
    }

    private static List<String> getBreadcrumbs(WebDriver driver) {
        List<WebElement> breadcrumbElements = driver.findElements(By.cssSelector(".breadcrumb"));
        return breadcrumbElements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private static void compareBreadcrumbs(List<String> breadcrumbs1, List<String> breadcrumbs2) {
        int size = Math.max(breadcrumbs1.size(), breadcrumbs2.size());

        for (int i = 0; i < size; i++) {
            String breadcrumb1 = i < breadcrumbs1.size() ? breadcrumbs1.get(i) : "MISSING";
            String breadcrumb2 = i < breadcrumbs2.size() ? breadcrumbs2.get(i) : "MISSING";

            if (!breadcrumb1.equals(breadcrumb2)) {
                System.out.println("Difference at index " + i + ":");
                System.out.println("URL 1: " + breadcrumb1);
                System.out.println("URL 2: " + breadcrumb2);
            }
        }
    }
}
