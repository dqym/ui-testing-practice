package pages;

import elements.Input;
import elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * Страница логина.
 * Предоставляет методы для взаимодействия с полями ввода и кнопкой входа.
 */
public class LoginPage extends BasePage {
    private final By USERNAME_INPUT_LOCATOR = By.cssSelector("faceplate-text-input#login-username");
    private final By USERNAME_INNER_INPUT_LOCATOR = By.cssSelector("input[name='username']");
    private final By PASSWORD_INPUT_LOCATOR = By.cssSelector("faceplate-text-input#login-password");
    private final By PASSWORD_INNER_INPUT_LOCATOR = By.cssSelector("input[name='password']");
    private final By LOGIN_BUTTON_LOCATOR = By.cssSelector("button.login");

    /**
     * Конструктор страницы логина.
     * Инициализирует элементы страницы.
     *
     * @param driver WebDriver
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Вводит имя пользователя в поле логина.
     *
     * @param username имя пользователя
     */
    public void enterUsername(String username) {
        Input usernameInput = Input.fromShadowHost(driver,
                USERNAME_INPUT_LOCATOR,
                USERNAME_INNER_INPUT_LOCATOR);

        usernameInput.sendKeys(username);
    }

    /**
     * Вводит пароль в поле пароля.
     *
     * @param password пароль пользователя
     */
    public void enterPassword(String password) {
        Input passwordInput = Input.fromShadowHost(driver,
                PASSWORD_INPUT_LOCATOR,
                PASSWORD_INNER_INPUT_LOCATOR);

        passwordInput.sendKeys(password);
    }

    /**
     * Кликает кнопку входа и ожидает перезагрузку страницы.
     */
    public void clickLoginButton() {
        Button loginButton = Button.fromLocator(driver, LOGIN_BUTTON_LOCATOR);

        loginButton.click();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.navigate().refresh();
    }

}
