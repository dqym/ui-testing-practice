package pages;

import base.BasePage;
import elements.Input;
import elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * Страница логина.
 * Предоставляет методы для взаимодействия с полями ввода и кнопкой входа.
 */
public class LoginPage extends BasePage {

    private final Input usernameInput;
    private final Input passwordInput;
    private final Button loginButton;

    /**
     * Конструктор страницы логина.
     * Инициализирует элементы страницы.
     *
     * @param driver WebDriver
     */
    public LoginPage(WebDriver driver) {
        super(driver);

        usernameInput = Input.fromShadowHost(driver,
                By.cssSelector("faceplate-text-input#login-username"),
                By.cssSelector("input[name='username']"));

        passwordInput = Input.fromShadowHost(driver,
                By.cssSelector("faceplate-text-input#login-password"),
                By.cssSelector("input[name='password']"));

        loginButton = Button.of(driver, By.cssSelector("button.login"));
    }

    /**
     * Вводит имя пользователя в поле логина.
     *
     * @param username имя пользователя
     */
    public void enterUsername(String username) {
        usernameInput.sendKeys(username);
    }

    /**
     * Вводит пароль в поле пароля.
     *
     * @param password пароль пользователя
     */
    public void enterPassword(String password) {
        passwordInput.sendKeys(password);
    }

    /**
     * Кликает кнопку входа и ожидает перезагрузку страницы.
     */
    public void clickLoginButton() {
//        WebElement oldPage = driver.findElement(By.tagName("html"));
        loginButton.click();

        try {
            Thread.sleep(6000);              // Пауза 6 секунд для ожидания загрузки/авторизации
        } catch (InterruptedException e) {
            e.printStackTrace();            // Обработка возможного исключения прерывания
        }

        driver.navigate().refresh();         // Обновляем страницу после входа
//        // Ожидаем, пока старая страница станет "устаревшей" (перезагрузится)
//        wait.withTimeout(java.time.Duration.ofSeconds(6))
//                .until(ExpectedConditions.stalenessOf(oldPage));
    }

}
