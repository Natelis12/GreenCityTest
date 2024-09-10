package HW9;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GreenCityTest {

    // Locators
    @FindBy(css = "app-header:nth-child(1) .ubs-header-sign-in")
    private WebElement signInButton;

    @FindBy(css = "app-sign-in .container h1")
    private WebElement welcomeText;

    @FindBy(css = "app-sign-in .container h2")
    private WebElement signInDetailsText;

    @FindBy(css = "app-sign-in .sign-in-form label[for='email']")
    private WebElement emailLabel;

    @FindBy(css = "app-sign-in .sign-in-form #email")
    private WebElement emailInput;

    @FindBy(css = "app-sign-in .sign-in-form #email-err-msg .app-error")
    private WebElement emailError;

    @FindBy(css = "app-sign-in .sign-in-form label[for='password']")
    private WebElement passwordLabel;

    @FindBy(css = "app-sign-in .sign-in-form #password")
    private WebElement passwordInput;

    @FindBy(css = "app-sign-in .sign-in-form .ubsStyle")
    private WebElement signInSubmitButton;

    @FindBy(css = ".mat-simple-snackbar > span")
    private WebElement result;

    @FindBy(css = ".alert-general-error.ng-star-inserted")
    private WebElement errorMessage;

    @FindBy(css = "app-header:nth-child(1) .body-2")
    private WebElement usernameElement;

    @FindBy(linkText = "Sign out")
    private WebElement signOutLink;

    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.greencity.cx.ua/");
    }

    @BeforeEach
    public void initPageElements() throws InterruptedException {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String currentLanguage = driver.findElement(By.cssSelector("ul[aria-label='language switcher'] li.lang-option > span")).getText();
        Thread.sleep(3000);

        if (currentLanguage.equalsIgnoreCase("UA")) {
            driver.findElement(By.cssSelector("div.main-content.app-container li.lang-option > span")).click();
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("div.main-content.app-container li[aria-label='EN'] > span")).click();
            Thread.sleep(3000);
        }
    }
    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyTitle() throws InterruptedException {
        Assertions.assertEquals("GreenCity", driver.getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "xenefa2366@apifan.com, Qwerty1234567!"
    })
    public void signIn(String email, String password) throws InterruptedException {
        signInButton.click();
        assertThat(welcomeText.getText(), is("Welcome back!"));
        assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));
        assertThat(emailLabel.getText(), is("Email"));
        emailInput.sendKeys(email);
        assertThat(emailInput.getAttribute("value"), is(email));
        passwordInput.sendKeys(password);
        assertThat(passwordInput.getAttribute("value"), is(password));
        signInSubmitButton.click();

        Assertions.assertEquals("Qwerty1", driver.findElement(By.cssSelector("app-header:nth-child(1) .body-2")).getText());
        Thread.sleep(1000);
        //
        driver.findElement(By.cssSelector("app-header:nth-child(1) .body-2")).click();
        Thread.sleep(1000);
        //
        driver.findElement(By.linkText("Sign out")).click();
        Thread.sleep(1000);
        List<WebElement> elements = driver.findElements(By.cssSelector("app-header:nth-child(1) .header_sign-up-link:nth-child(4) span:nth-child(1)"));
        assert (!elements.isEmpty());
        Thread.sleep(1000);
    }

    @ParameterizedTest
    @CsvSource({
            "Please check if the email is written correctly"
    })
    public void signInNotValid(String expectedErrorMessage) {
        signInButton.click();
        // Enter an incorrect email and password
        emailInput.sendKeys("xenefa2366apifan.com");
        passwordInput.sendKeys("Qwerty1234567!");
        signInSubmitButton.click(); // Make sure to click the submit button to trigger the validation

        // Wait for the error message to be visible and get its text
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#email-err-msg .ng-star-inserted")));

        // Verify that the error message text matches the expected message
        assertThat(errorElement.getText().trim(), is(expectedErrorMessage));
    }
}


