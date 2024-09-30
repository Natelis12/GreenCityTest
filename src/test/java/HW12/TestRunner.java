package HW12;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestRunner {
    protected static WebDriver driver;
    protected WebDriverWait wait;

    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get("https://www.greencity.cx.ua/");
    }

    public void initPageElements(Object page) {
        PageFactory.initElements(driver, page);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
