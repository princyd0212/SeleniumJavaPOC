package GetURL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;

import java.util.Arrays;
import java.util.List;

public class Compare {
    public static void main(String[] args) {
        WebDriver driver1 = new ChromeDriver();
        driver1.manage().window().maximize();
        WebDriver driver2 = new ChromeDriver();
        driver2.manage().window().maximize();

        String url1 = "https://www.paybima.com/knowledge-center/guide/health-insurance";
        String url2 = "https://uat.paybima.com/knowledge-center/guide/health-insurance";

        driver1.get(url1);
        WebElement body1 = driver1.findElement(By.tagName("body"));
        String text1 = body1.getText();

        driver2.get(url2);
        WebElement body2 = driver2.findElement(By.tagName("body"));
        String text2 = body2.getText();

        List<String> lines1 = Arrays.asList(text1.split("\n"));
        List<String> lines2 = Arrays.asList(text2.split("\n"));

        Patch<String> patch = DiffUtils.diff(lines1, lines2);

        for (AbstractDelta<String> delta : patch.getDeltas()) {
            System.out.println(delta);
        }

        driver1.quit();
        driver2.quit();
    }
}
