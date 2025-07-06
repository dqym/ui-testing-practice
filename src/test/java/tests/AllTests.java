package tests;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.FeedPage;
import pages.LoginPage;
import pages.PostPage;
import util.CookieManager;

import java.time.Duration;


/**
 * Класс с тестами Reddit.
 * <p>
 * Содержит набор тестов для проверки основной функциональности Reddit:
 * добавление комментариев, отправка жалоб, навигация по категориям сайта,
 * скрытие постов.
 */
public class AllTests extends BaseTest {

    private static final String TEST_USERNAME = "testmail99213@mail.ru";
    private static final String TEST_PASSWORD = "#55Ra9k9?j@xTyv";
    private static final String COMMENT_TEXT = "lol";
    private static final String COMMENT_AUTHOR = "No-Customer3367";

    /**
     * Метод для авторизации пользователя в Reddit.
     * <p>
     * Сначала пытается загрузить сохраненные cookies, если они есть.
     * Если авторизация не прошла автоматически (пользователь остался на странице логина),
     * выполняет ручной вход с указанными учётными данными и сохраняет cookies для
     * последующего использования.
     * <p>
     * Между действиями используются {@code Thread.sleep}, чтобы обойти защиту:
     * при слишком быстром вводе логина и пароля сайт может временно заблокировать авторизацию
     * как подозрительную активность. Задержки имитируют поведение реального пользователя.
     *
     * @param loginPage объект страницы логина для взаимодействия с формой входа
     * @return FeedPage объект страницы после авторизации
     */
    private FeedPage authorize(LoginPage loginPage) {
        CookieManager.loadCookies(driver, TEST_USERNAME);

        if (driver.getCurrentUrl().contains("/login")) {
            try {
                Thread.sleep(2000);
                loginPage.enterUsername(TEST_USERNAME);
                Thread.sleep(3000);
                loginPage.enterPassword(TEST_PASSWORD);
                Thread.sleep(1000);
                loginPage.clickLoginButton();
                Thread.sleep(3000);
                CookieManager.saveCookies(driver, TEST_USERNAME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new FeedPage(driver);
    }

    /**
     * Тест проверяет возможность добавить комментарий под первым постом в ленте.
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Открытие первого поста в ленте
     * 3. Добавление комментария с заданным текстом
     * 4. Проверка, что комментарий отображается с нужным автором и текстом
     */
    @Test
    public void testAddComment() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        PostPage postPage = feedPage.openFirstPost();
        postPage.clickAddCommentButton();
        postPage.enterCommentText(COMMENT_TEXT);
        postPage.clickSubmitCommentButton();

        Assert.assertTrue(
                "Комментарий не найден под постом",
                postPage.isCommentVisible(COMMENT_AUTHOR, COMMENT_TEXT)
        );
    }

    /**
     * Тест проверяет функциональность отправки жалобы на пост.
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Открытие меню действий для поста
     * 3. Выбор опции "Пожаловаться"
     * 4. Выбор причины жалобы (СПАМ)
     * 5. Проход по цепочке экранов отправки жалобы
     * 6. Проверка успешной отправки жалобы
     */
    @Test
    public void testSendReport() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        feedPage.clickPostOverflowMenu();
        feedPage.clickPostOverflowReport();
        feedPage.clickReportSPAMButton();
        feedPage.clickNextReportButton();
        feedPage.clickCategoryOfSPAM();
        feedPage.clickSendReportButton();

        Assert.assertTrue("Жалоба не отправлена", feedPage.isReportSend());
    }

    /**
     * Тест проверяет навигацию в боковом меню - переход на главную страницу.
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Раскрытие бокового меню навигации
     * 3. Клик по кнопке категории "home"
     * 4. Проверка соответствия URL ожидаемому для главной страницы
     */
    @Test
    public void testNavigationBarHome() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("home");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому",
                feedPage.checkActivePage("home"));
    }

    /**
     * Тест проверяет навигацию в боковом меню - переход на страницу популярных постов.
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Раскрытие бокового меню навигации
     * 3. Клик по кнопке категории "popular"
     * 4. Проверка соответствия URL ожидаемому для страницы популярных постов
     */
    @Test
    public void testNavigationBarPopular() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("popular");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому",
                feedPage.checkActivePage("popular"));
    }

    /**
     * Тест проверяет навигацию в боковом меню - переход на страницу всех постов (all).
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Раскрытие бокового меню навигации
     * 3. Клик по кнопке категории "all"
     * 4. Проверка соответствия URL ожидаемому для страницы всех постов
     */
    @Test
    public void testNavigationBarAll() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("all");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому",
                feedPage.checkActivePage("all"));
    }

    /**
     * Тест проверяет функциональность скрытия поста.
     * <p>
     * Последовательность действий:
     * 1. Авторизация в системе
     * 2. Открытие меню действий для поста
     * 3. Выбор опции "Скрыть"
     * 4. Ожидание применения действия
     * 5. Проверка, что пост скрыт на странице
     * 
     * @throws InterruptedException если выполнение потока было прервано во время ожидания
     */
    @Test
    public void testHidePost() {
        LoginPage loginPage = new LoginPage(driver);
        FeedPage feedPage = authorize(loginPage);

        feedPage.clickPostOverflowMenu();
        feedPage.clickPostOverflowHide();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        boolean hidden = wait.until(driver -> feedPage.isPostHidden());

        Assert.assertTrue("Пост не скрыт", hidden);
    }

}
