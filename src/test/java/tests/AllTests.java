package tests;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import pages.FeedPage;
import pages.LoginPage;
import pages.PostPage;

/**
 * Тест на добавление комментария к посту.
 */
public class AllTests extends BaseTest {

    private static final String TEST_USERNAME = "testmail36244@mail.ru";
    private static final String TEST_PASSWORD = "S26Yqm#@yCePjq@";
    private static final String COMMENT_TEXT = "meow";
    private static final String COMMENT_AUTHOR = "AdSafe1407";

    private LoginPage loginPage;
    private FeedPage feedPage;
    private PostPage postPage;

    @Override
    public void openBrowser() {
        super.openBrowser();

        loginPage = new LoginPage(driver);
        feedPage = new FeedPage(driver);
        postPage = new PostPage(driver);
    }

    /**
     * Тест проверяет возможность добавить комментарий под первым постом в ленте.
     */
    @Test
    public void testAddComment() {
        try {
            loginPage.enterUsername(TEST_USERNAME);
            Thread.sleep(3000);
            loginPage.enterPassword(TEST_PASSWORD);
            Thread.sleep(1000);
            loginPage.clickLoginButton();
        } catch (InterruptedException e) {
            e.printStackTrace();            // Обработка возможного исключения прерывания
        }

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
        try {
            loginPage.enterUsername(TEST_USERNAME);
            Thread.sleep(3000);
            loginPage.enterPassword(TEST_PASSWORD);
            Thread.sleep(1000);
            loginPage.clickLoginButton();
        } catch (InterruptedException e) {
            e.printStackTrace();            // Обработка возможного исключения прерывания
        }

        feedPage.clickPostOverflowMenu();
        feedPage.clickPostOverflowReport();
        feedPage.clickReportSPAMButton();
        feedPage.clickNextReportButton();
        feedPage.clickCategoryOfSPAM();
        feedPage.clickSendReportButton();

        Assert.assertTrue("Жалоба не отправлена", feedPage.isReportSend());
    }
}
