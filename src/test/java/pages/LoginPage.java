package pages;

import org.openqa.selenium.*;

public class LoginPage {
    private WebDriver driver;          // Веб-драйвер для управления браузером
    private WebElement usernameField;  // Поле для ввода имени пользователя
    private WebElement passwordField;  // Поле для ввода пароля
    private WebElement loginButton;    // Кнопка входа

    // Конструктор страницы логина, принимает WebDriver
    public LoginPage(WebDriver driver) {
        this.driver = driver;

        // Находим кастомный элемент faceplate-text-input с id login-username
        WebElement host = driver.findElement(By.cssSelector("faceplate-text-input#login-username"));
        // Получаем shadow root этого кастомного элемента
        SearchContext shadowRoot = host.getShadowRoot();
        // Внутри shadow root находим настоящий input для имени пользователя
        this.usernameField = shadowRoot.findElement(By.cssSelector("input[name='username']"));

        // Аналогично для поля пароля: находим кастомный элемент с id login-password
        host = driver.findElement(By.cssSelector("faceplate-text-input#login-password"));
        shadowRoot = host.getShadowRoot();
        // Внутри shadow root находим input для пароля
        this.passwordField = shadowRoot.findElement(By.cssSelector("input[name='password']"));

        // Находим кнопку логина по классу "login"
        loginButton = driver.findElement(By.cssSelector("button.login"));
    }

    // Метод для выполнения входа: вводит логин и пароль, нажимает кнопку
    public void login(String username, String password) {
        usernameField.sendKeys(username);   // Вводим имя пользователя
        passwordField.sendKeys(password);   // Вводим пароль
        loginButton.click();                 // Кликаем кнопку "Войти"

        try {
            Thread.sleep(6000);              // Пауза 6 секунд для ожидания загрузки/авторизации
        } catch (InterruptedException e) {
            e.printStackTrace();            // Обработка возможного исключения прерывания
        }

        driver.navigate().refresh();         // Обновляем страницу после входа
    }
}
