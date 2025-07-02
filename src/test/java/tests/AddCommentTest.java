package tests;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;

import java.time.Duration;

public class AddCommentTest {
    private WebDriver driver;      // Веб-драйвер для управления браузером
    private LoginPage loginPage;   // Страница логина
    private FeedPage feedPage;     // Страница ленты постов
    private PostPage postPage;     // Страница отдельного поста

    // Метод, выполняемый перед каждым тестом — настройка драйвера и страниц
    @Before
    public void setUp() {
        driver = new ChromeDriver();  // Инициализация ChromeDriver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Установка неявного ожидания 10 секунд
        driver.get("https://www.reddit.com/login/");  // Открываем страницу логина

        // Инициализация объектов страниц, передаем драйвер в конструкторы
        loginPage = new LoginPage(driver);
        feedPage = new FeedPage(driver);
        postPage = new PostPage(driver);
    }

    // Сам тест — проверка возможности добавить комментарий под постом
    @Test
    public void testAddComment() {
        loginPage.login("testmail99213@mail.ru", "#55Ra9k9?j@xTyv");  // Логинимся под тестовым пользователем
        feedPage.openFirstPost();  // Открываем первый пост из ленты
        String comment = "❤";      // Текст комментария для добавления
        postPage.addComment(comment);  // Добавляем комментарий
        // Проверяем, что комментарий отображается под постом, ожидаем имя автора комментария "No-Customer3367"
        Assert.assertTrue("Комментарий не найден под постом", postPage.isCommentVisible("No-Customer3367", comment));
    }

    // Метод, выполняемый после каждого теста — закрываем браузер
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();  // Закрываем все окна и останавливаем драйвер
        }
    }
}
