package tests;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

/**
 * Базовый класс для всех тестов.
 * Содержит общую настройку и завершение работы драйвера.
 */
public abstract class BaseTest {

    protected WebDriver driver;

    private static final String BASE_URL = "https://www.reddit.com/login/";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);

    /**
     * Метод инициализирует WebDriver и открывает стартовую страницу перед каждым тестом.
     */
    @Before
    public void openBrowser() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        openPage(BASE_URL);
    }

    /**
     * Метод завершает работу WebDriver после каждого теста.
     */
    @After
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Открывает указанную ссылку в браузере.
     *
     * @param url URL страницы для открытия
     */
    protected void openPage(String url) {
        driver.get(url);
    }
}
