package GetURL;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class SectionCompare {
    public static void main(String[] args) {
        WebDriver driver1 = new ChromeDriver();
        driver1.manage().window().maximize();
        WebDriver driver2 = new ChromeDriver();
        driver2.manage().window().maximize();
        try {
            String url1 = "https://www.paybima.com/join-as-a-posp-insurance-agent";
            String url2 = "https://uat.paybima.com/join-as-a-posp-insurance-agent";
            driver1.get(url1);
            String urlProd = driver1.getCurrentUrl();
            List<String> headers1 = getHeaders(driver1);

            driver2.get(url2);
            String urlPreProd = driver2.getCurrentUrl();
            List<String> headers2 = getHeaders(driver2);

            Patch<String> patch = DiffUtils.diff(headers1, headers2);

            System.out.println("urlProd: " + urlProd);
            System.out.println("urlPreProd: " + urlPreProd);

            System.out.println("Differences:");
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
        } finally {

            driver1.quit();
            driver2.quit();
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
}
