package HW12;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LanguageSwitcher {
    private WebDriver driver;
    private WebDriverWait wait;

    public LanguageSwitcher(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void switchToEnglishIfNeeded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[aria-label='language switcher'] li.lang-option > span")));
        String currentLanguage = driver.findElement(By.cssSelector("ul[aria-label='language switcher'] li.lang-option > span")).getText();
        if (currentLanguage.equalsIgnoreCase("UA")) {
            driver.findElement(By.cssSelector("div.main-content.app-container li.lang-option > span")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.main-content.app-container li[aria-label='EN'] > span"))).click();
        }
    }
}
