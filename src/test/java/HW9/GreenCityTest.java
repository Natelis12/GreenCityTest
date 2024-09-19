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

    @FindBy(css = "a.close-modal-window[aria-label='close form button']")
    private WebElement closeModalButton;

    @FindBy(css = ".mat-simple-snackbar > span")
    private WebElement result;

//    @FindBy(css = ".alert-general-error.ng-star-inserted")
//    private WebElement errorMessage;

//    @FindBy(css = "#email-err-msg")
//    private WebElement errorMessage;

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
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.get("https://www.greencity.cx.ua/");
    }

    @BeforeEach
    public void initPageElements() {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Wait for the language switcher element to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul[aria-label='language switcher'] li.lang-option > span")));

        String currentLanguage = driver.findElement(By.cssSelector("ul[aria-label='language switcher'] li.lang-option > span")).getText();
        if (currentLanguage.equalsIgnoreCase("UA")) {
            driver.findElement(By.cssSelector("div.main-content.app-container li.lang-option > span")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.main-content.app-container li[aria-label='EN'] > span"))).click();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyTitle() {
        Assertions.assertEquals("GreenCity", driver.getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "xenefa2366@apifan.com, Qwerty1234567!",
            "cekige2052@klanze.com, Qwerty1!"
    })
    public void signIn(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();

        // Wait for the "Welcome back!" text to appear
        wait.until(ExpectedConditions.textToBePresentInElement(welcomeText, "Welcome back!"));
        assertThat(welcomeText.getText().trim(), is("Welcome back!"));

        wait.until(ExpectedConditions.visibilityOf(signInDetailsText));
        assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));

        wait.until(ExpectedConditions.visibilityOf(emailLabel));
        assertThat(emailLabel.getText(), is("Email"));

        emailInput.sendKeys(email);
        assertThat(emailInput.getAttribute("value"), is(email));

        passwordInput.sendKeys(password);
        assertThat(passwordInput.getAttribute("value"), is(password));

        wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(closeModalButton));
        closeModalButton.click();

//        wait.until(ExpectedConditions.elementToBeClickable(authModal)).click();

        wait.until(ExpectedConditions.visibilityOf(usernameElement));
        Assertions.assertEquals("Qwerty1", usernameElement.getText());

        // Sign out process
        usernameElement.click();
        wait.until(ExpectedConditions.elementToBeClickable(signOutLink)).click();

        // Verify that the "Sign in" button is present after signing out
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("app-header:nth-child(1) .header_sign-up-link:nth-child(4) span:nth-child(1)")));
        assert (!elements.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "Please check if the email is written correctly"
    })

    public void signInNotValid(String expectedErrorMessage) {
        // Click on the Sign In button
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();

        // Enter incorrect email and password
        emailInput.sendKeys("xenefa2366apifan.com");  // Incorrect email format
        passwordInput.sendKeys("Qwerty1234567!");

        // Click on the Submit button
        wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton)).click();

        // Wait for the error message to appear and verify it
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOf(emailError));
        assertThat(errorElement.getText().trim(), is(expectedErrorMessage));
    }
}
