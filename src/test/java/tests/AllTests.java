package tests;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import pages.CreatePostPage;
import pages.FeedPage;
import pages.LoginPage;
import pages.PostPage;

/**
 * Тест на добавление комментария к посту.
 */
public class AllTests extends BaseTest {

    private static final String TEST_USERNAME = "testmail36244@mail.ru";
    private static final String TEST_PASSWORD = "S26Yqm#@yCePjq@";
    private static final String COMMENT_TEXT = "lol";
    private static final String COMMENT_AUTHOR = "AdSafe1407";

    private LoginPage loginPage;
    private FeedPage feedPage;
    private PostPage postPage;
    private CreatePostPage createPostPage;

    private void authorize() {
        try {
            Thread.sleep(2000);
            loginPage.enterUsername(TEST_USERNAME);
            Thread.sleep(3000);
            loginPage.enterPassword(TEST_PASSWORD);
            Thread.sleep(1000);
            loginPage.clickLoginButton();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
    public void testNotification() { //не работает :(
        authorize();

        feedPage.clickCreatePost();

        createPostPage.clickComunityPickerMenu();
        createPostPage.clickSelectProfile(COMMENT_AUTHOR);
        createPostPage.clickTitle();
        createPostPage.enterPostTitleText();
        createPostPage.clickBody();
        createPostPage.enterPostBodyText();
        createPostPage.clickSubmitPostButton();
    }
}
