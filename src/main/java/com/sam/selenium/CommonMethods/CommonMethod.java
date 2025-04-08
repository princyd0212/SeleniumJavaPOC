package com.sam.selenium.CommonMethods;

import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CommonMethod {
    WebDriver driver;

    public CommonMethod(WebDriver driver) {
        this.driver = driver;
    }

    public void clickElement(By locator) {
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public void enterText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clearTextField(By locator) {
        WebElement element = driver.findElement(locator);
        element.clear();
    }

    public void waitForElementToContainText(By locator, String text, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void navigateToUrl(String url) {
        driver.navigate().to(url);
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void doubleClickElement(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(locator);
        actions.doubleClick(element).perform();
    }

    public void rightClickElement(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(locator);
        actions.contextClick(element).perform();
    }



    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public String getElementText(By locator) {
        WebElement element = driver.findElement(locator);
        return element.getText();
    }

    public void waitForElementVisible(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public String getElementAttribute(By locator, String attribute) {
        WebElement element = driver.findElement(locator);
        return element.getAttribute(attribute);
    }

    public void waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementPresence(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public void takeScreenshot(String filePath) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        File destFile = new File(filePath);
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public List<String> getAllDropdownOptions(By locator) {
        WebElement dropdown = driver.findElement(locator);
        Select select = new Select(dropdown);
        List<WebElement> options = select.getOptions();
        List<String> optionTexts = new ArrayList<>();
        for (WebElement option : options) {
            optionTexts.add(option.getText());
        }
        return optionTexts;
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        WebElement element = driver.findElement(locator);
        return element.isEnabled();
    }

    public boolean isElementSelected(By locator) {
        WebElement element = driver.findElement(locator);
        return element.isSelected();
    }

    public void waitForPageLoadComplete(int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public void uploadFile(By locator, String filePath) {
        WebElement fileInput = driver.findElement(locator);
        fileInput.sendKeys(filePath);
    }

    public void waitForUrlToContain(String urlFraction, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.urlContains(urlFraction));
    }

    public boolean isElementEditable(By locator) {
        WebElement element = driver.findElement(locator);
        return element.isEnabled() && element.getAttribute("readonly") == null;
    }

    public WebElement retryingFindElement(By locator) {
        WebElement element = null;
        int attempts = 0;
        while (attempts < 3) {
            try {
                element = driver.findElement(locator);
                break;
            } catch (NoSuchElementException e) {
                attempts++;
            }
        }
        return element;
    }

    public void mouseOverElement(By locator) {
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element).perform();
    }

    public static void sendFailureNotification(String failureMessage) {
        String webhookUrl = "https://contcentricpvtltd.webhook.office.com/webhookb2/1e82d6a2-3afe-4834-a55d-891d9d1592c7@92df81cd-dcf2-490a-884c-13b58b3a8ca6/IncomingWebhook/7794e09a8ad24f898b170f53b4826638/a98cea20-3ca3-474e-9414-4d238c6c92a0/V2eh5nQsofcLs38I-YRLuqPV370fbBDJQ4yDiBa7slRRQ1";
        String jsonPayload = String.format(
                "{ \"text\": \"🚨 Test Failure Alert: %s\" }",
                failureMessage
        );

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(webhookUrl);
            StringEntity entity = new StringEntity(jsonPayload, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = client.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    System.out.println("Notification sent successfully!");
                } else {
                    System.err.println("Failed to send notification. HTTP Status Code: " + statusCode);
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to send notification: " + ex.getMessage());
        }
    }

}
