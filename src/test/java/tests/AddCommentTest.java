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
public class AddCommentTest extends BaseTest {

    private static final String TEST_USERNAME = "testmail99213@mail.ru";
    private static final String TEST_PASSWORD = "#55Ra9k9?j@xTyv";
    private static final String COMMENT_TEXT = "❤";
    private static final String COMMENT_AUTHOR = "No-Customer3367";

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
        loginPage.enterUsername(TEST_USERNAME);
        loginPage.enterPassword(TEST_PASSWORD);
        loginPage.clickLoginButton();

        feedPage.openFirstPost();

        postPage.clickAddCommentButton();
        postPage.enterCommentText(COMMENT_TEXT);
        postPage.clickSubmitCommentButton();

        Assert.assertTrue(
                "Комментарий не найден под постом",
                postPage.isCommentVisible(COMMENT_AUTHOR, COMMENT_TEXT)
        );
    }
}
