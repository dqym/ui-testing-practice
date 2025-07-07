package tests;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import util.CookieManager;

import java.time.Duration;

/**
 * Тест на добавление комментария к посту.
 */
public class AllTests extends BaseTest {

    private static final String TEST_USERNAME = "testmail7654352@mail.ru";
    private static final String TEST_PASSWORD = "qwe123123";
    private static final String TEST_USERNAME_2 = "testmail@mail.ru";
    private static final String TEST_PASSWORD_2 = "popka068";
    private static final String COMMENT_TEXT = "lol";
    private static final String COMMENT_AUTHOR = "FearlessMacaron214";

    private LoginPage loginPage;
    private FeedPage feedPage;
    private PostPage postPage;
    private CreatePostPage createPostPage;

    private void authorize() {
        CookieManager.loadCookies(driver);
        if (driver.getCurrentUrl().contains("/login")) {
            try {
                Thread.sleep(2000);
                loginPage.enterUsername(TEST_USERNAME);
                Thread.sleep(3000);
                loginPage.enterPassword(TEST_PASSWORD);
                Thread.sleep(1000);
                loginPage.clickLoginButton();
                Thread.sleep(3000);
                CookieManager.saveCookies(driver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void openBrowser() {
        super.openBrowser();

        loginPage = new LoginPage(driver);
        feedPage = new FeedPage(driver);
        postPage = new PostPage(driver);
        createPostPage = new CreatePostPage(driver);
    }
    @Test
    public void testSuccessfulLogin() {
        // 3. Ввод учетных данных
        loginPage.enterUsername(TEST_USERNAME);
        loginPage.enterPassword(TEST_PASSWORD);

        // 4. Нажатие кнопки входа
        loginPage.clickLoginButton();

        // 5. Ожидание загрузки фида (можно добавить явное ожидание)
        try {
            Thread.sleep(3000); // Лучше заменить на WebDriverWait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 7. Дополнительная проверка URL (опционально)
        Assert.assertFalse("После авторизации остались на странице входа",loginPage.getCurrentUrl().contains("login"));
    }
    @Test
    public void testUnsuccessfulLogin() {
        // 3. Ввод учетных данных
        loginPage.enterUsername(TEST_USERNAME_2);
        loginPage.enterPassword(TEST_PASSWORD_2);

        // 4. Нажатие кнопки входа
        loginPage.clickLoginButton();

        // 5. Ожидание загрузки фида (можно добавить явное ожидание)
        try {
            Thread.sleep(3000); // Лучше заменить на WebDriverWait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 7. Дополнительная проверка URL (опционально)
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("Ожидалось остаться на странице входа. Текущий URL: " + currentUrl, currentUrl.contains("login") || currentUrl.contains("error"));
    }
    @Test
    public void testSuccessfulLogout() {
        // 3. Ввод учетных данных
        loginPage.enterUsername(TEST_USERNAME);
        loginPage.enterPassword(TEST_PASSWORD);

        // 4. Нажатие кнопки входа
        loginPage.clickLoginButton();

        feedPage.clickUserMenuButton();

        feedPage.clickQuitButton();
        // 5. Ожидание загрузки фида (можно добавить явное ожидание)
        try {
            Thread.sleep(3000); // Лучше заменить на WebDriverWait
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue("Кнопка входа не отображается после выхода",feedPage.FindLoginButton());
    }
    @Test
    public void testCreatePost() {
        authorize();

        feedPage.clickCreatePost();

        createPostPage.clickComunityPickerMenu();
        createPostPage.enterUsernameText(COMMENT_AUTHOR);
        createPostPage.clickSelectProfile(COMMENT_AUTHOR);
        createPostPage.clickTitle();
        createPostPage.enterPostTitleText();
        createPostPage.clickBody();
        createPostPage.enterPostBodyText();
        createPostPage.clickSubmitPostButton();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("Ожидалось перейти на страницу профиля. Текущий URL: " + currentUrl, currentUrl.contains("user") || currentUrl.contains("error"));
    }

    @Test
    public void testSavePost() {
        authorize();

        feedPage.clickPostOverflowMenu();
        feedPage.clickPostOverflowSaveButton();

        feedPage.clickPostOverflowMenu();
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("Ожидалось остаться в контекстном меню: " + currentUrl, currentUrl.contains("reddit"));
    }

    /**
     * Тест проверяет возможность добавить комментарий под первым постом в ленте.
     */
    @Test
    public void testAddComment() {
        authorize();

        feedPage.openFirstPost();

        postPage.clickAddCommentButton();
        postPage.enterCommentText(COMMENT_TEXT);
        postPage.clickSubmitCommentButton();

        Assert.assertTrue(
                "Комментарий не найден под постом",
                postPage.isCommentVisible(COMMENT_AUTHOR, COMMENT_TEXT)
        );
    }

    @Test
    public void testSendReport() {
        authorize();

        feedPage.clickPostOverflowMenu();
        feedPage.clickPostOverflowReport();
        feedPage.clickReportSPAMButton();
        feedPage.clickNextReportButton();
        feedPage.clickCategoryOfSPAM();
        feedPage.clickSendReportButton();

        Assert.assertTrue("Жалоба не отправлена", feedPage.isReportSend());
    }

    @Test
    public void testNavigationBarHome() {
        authorize();

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("home");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому", feedPage.checkActivePage("home"));
    }

    @Test
    public void testNavigationBarPopular() {
        authorize();

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("popular");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому",
                feedPage.checkActivePage("popular"));
    }

    @Test
    public void testNavigationBarAll() {
        authorize();

        feedPage.expandSideBar();
        feedPage.clickCategoryButton("all");

        Assert.assertTrue("Текущий URL не соответствует ожидаемому",
                feedPage.checkActivePage("all"));
    }

    @Test
    public void testNotification() throws InterruptedException { //не работает :(
        authorize();

        feedPage.clickCreatePost();

        createPostPage.clickComunityPickerMenu();
        createPostPage.enterUsernameText(COMMENT_AUTHOR);
        createPostPage.clickSelectProfile(COMMENT_AUTHOR);
        createPostPage.clickTitle();
        createPostPage.enterPostTitleText();
        createPostPage.clickBody();
        createPostPage.enterPostBodyText();
        createPostPage.clickSubmitPostButton();
    }

    @Test
    public void testUpvoteFunctionality() {
        authorize();
        feedPage.openFirstPost();

        PostPage postPage = new PostPage(driver);
        postPage.resetVote();
        int initialCount = postPage.getVoteCount();

        postPage.upvotePost();

        Assert.assertTrue(postPage.isUpvoted());
        Assert.assertEquals(postPage.getVoteCount(), initialCount + 1);
    }

    @Test
    public void testDownvoteFunctionality() {
        authorize();
        feedPage.openFirstPost();

        PostPage postPage = new PostPage(driver);
        postPage.resetVote();
        int initialCount = postPage.getVoteCount();

        postPage.downvotePost();

        Assert.assertTrue(postPage.isDownvoted());
        Assert.assertEquals(postPage.getVoteCount(), initialCount - 1);
    }

    @Test
    public void testSubscribeToSubreddit() {
        // Авторизация
        authorize();

        // Переходим на страницу сабреддита
        String subredditUrl = "https://www.reddit.com/r/reddit_ukr";
        driver.get(subredditUrl);

        // Создаем экземпляр CommunityPage
        CommunityPage communityPage = new CommunityPage(driver);

        // Ждем появления кнопки подписки
        communityPage.waitForSubscribeButton();

        // Проверяем начальное состояние
        boolean initiallySubscribed = communityPage.isSubscribed();
        System.out.println("Начальное состояние подписки: " + (initiallySubscribed ? "подписан" : "не подписан"));

        // Выполняем действие: подписываемся или отписываемся
        communityPage.clickSubscribeButton();

        // Ждем изменения состояния (до 15 секунд)
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> communityPage.isSubscribed() != initiallySubscribed);

        // Проверяем конечное состояние
        boolean finallySubscribed = communityPage.isSubscribed();
        System.out.println("Конечное состояние подписки: " + (finallySubscribed ? "подписан" : "не подписан"));

        // Проверяем, что состояние изменилось
        Assert.assertNotEquals("Состояние подписки не изменилось", initiallySubscribed, finallySubscribed);
    }

    @Test
    public void testSearchPost() {
        // Авторизация
        authorize();

        // Выполняем поиск
        feedPage.searchFor("test");

        // Получаем заголовок первого результата
        String title = feedPage.getFirstSearchResultTitle().toLowerCase();

        // Проверяем, что заголовок содержит ключевое слово
        Assert.assertTrue(
                "Заголовок первого результата не содержит 'test': " + title,
                title.contains("test")
        );
    }
}
