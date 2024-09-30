package HW12;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GreenCityTest extends TestRunner {

    @FindBy(css = "app-header:nth-child(1) .ubs-header-sign-in")
    private WebElement signInButton;

    @FindBy(css = "app-sign-in .container h1")
    private WebElement welcomeText;

    @FindBy(css = "app-sign-in .container h2")
    private WebElement signInDetailsText;

    @FindBy(css = "app-sign-in .sign-in-form #email")
    private WebElement emailInput;

    @FindBy(id = "email-err-msg")
    private WebElement emailError;

    @FindBy(css = "app-sign-in .sign-in-form #password")
    private WebElement passwordInput;

    @FindBy(css = "app-sign-in .sign-in-form .ubsStyle")
    private WebElement signInSubmitButton;

    @FindBy(css = "a.close-modal-window[aria-label='close form button']")
    private WebElement closeModalButton;

    @FindBy(css = "app-header:nth-child(1) .body-2")
    private WebElement usernameElement;

    @FindBy(linkText = "Sign out")
    private WebElement signOutLink;

    @BeforeAll
    public static void setUp() {
        TestRunner.setUp();
    }

    @BeforeEach
    public void init() {
        initPageElements(this);
        new LanguageSwitcher(driver, wait).switchToEnglishIfNeeded();
    }

    @AfterAll
    public static void tearDown() {
        TestRunner.tearDown();
    }

    @Test
    public void verifyTitle() {
        Assertions.assertEquals("GreenCity", driver.getTitle());
    }

    @ParameterizedTest
    @CsvSource({
            "xenefa2366@apifan.com, Qwerty1234567!"
    })
    public void signIn(String email, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        wait.until(ExpectedConditions.textToBePresentInElement(welcomeText, "Welcome back!"));
        assertThat(welcomeText.getText().trim(), is("Welcome back!"));
        wait.until(ExpectedConditions.visibilityOf(signInDetailsText));
        assertThat(signInDetailsText.getText(), is("Please enter your details to sign in."));

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(signInSubmitButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(closeModalButton)).click();
        wait.until(ExpectedConditions.visibilityOf(usernameElement));
        Assertions.assertEquals("Qwerty1", usernameElement.getText());

        usernameElement.click();
        wait.until(ExpectedConditions.elementToBeClickable(signOutLink)).click();
        Assertions.assertTrue(driver.findElements(By.cssSelector("app-header:nth-child(1) .header_sign-up-link:nth-child(4) span:nth-child(1)")).size() > 0);
    }

    @ParameterizedTest
    @CsvSource({
            "Please check if the email is written correctly"
    })
    public void signInNotValid(String expectedErrorMessage) {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        emailInput.sendKeys("xenefa2366apifan.com");
        wait.until(ExpectedConditions.elementToBeClickable(passwordInput)).click();
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOf(emailError));
        assertThat(errorElement.getText().trim(), is(expectedErrorMessage));
    }
}
