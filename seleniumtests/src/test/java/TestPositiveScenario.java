import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TestPositiveScenario {
    //@Test
    public void testCreateGroup() {
        System.setProperty("webdriver.chrome.driver", "/Users/amakarova/Downloads/chromedriver");
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(10l, TimeUnit.SECONDS);
        chromeDriver.get("http://localhost:8080");

        ExpectedCondition<Boolean> pageLoadCondition = driver -> ((JavascriptExecutor)driver)
                .executeScript("return document.readyState").equals("complete");

        WebDriverWait wait = new WebDriverWait(chromeDriver, 30);
        wait.until(pageLoadCondition);

        WebElement element = chromeDriver.findElement(By.cssSelector("button"));
        element.click();
    }
}
