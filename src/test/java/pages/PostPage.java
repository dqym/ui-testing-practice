package pages;

import base.BasePage;
import elements.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Страница отдельного поста.
 * Отвечает за добавление комментариев и проверку их отображения.
 */
public class PostPage extends BasePage {
    // Селекторы для кнопок лайка и дизлайка внутри Shadow DOM
    private static final By SHADOW_HOST = By.cssSelector("shreddit-post");
    private static final By UPVOTE_BUTTON = By.cssSelector("button[upvote]");
    private static final By DOWNVOTE_BUTTON = By.cssSelector("button[downvote]");
    private static final By VOTE_COUNT = By.cssSelector("faceplate-number");

    private static final By ADD_COMMENT_BUTTON = By.cssSelector("[noun='add_comment_button']");
    private static final By COMMENT_INPUT_CONTAINER = By.cssSelector("#main-content shreddit-composer > div:nth-child(1)");
    private static final By SUBMIT_COMMENT_BUTTON = By.cssSelector("button[slot='submit-button']");

    /**
     * Конструктор страницы поста.
     *
     * @param driver WebDriver
     */
    public PostPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Кликает по кнопке "Добавить комментарий".
     */
    public void clickAddCommentButton() {
        WebElement addCommentBtn = waitForClickable(ADD_COMMENT_BUTTON);
        scrollToCenter(addCommentBtn);
        addCommentBtn.click();
    }

    /**
     * Вводит текст комментария в поле ввода.
     *
     * @param text текст комментария
     */
    public void enterCommentText(String text) {
        WebElement commentInput = waitForVisible(COMMENT_INPUT_CONTAINER);
        commentInput.sendKeys(text);
    }

    /**
     * Кликает по кнопке отправки комментария.
     */
    public void clickSubmitCommentButton() {
        WebElement submitBtn = waitForClickable(SUBMIT_COMMENT_BUTTON);
        submitBtn.click();
    }

    // Методы для взаимодействия с кнопками лайка и дизлайка
    public void upvotePost() {
        getUpvoteButton().click();
        wait.until(d -> isUpvoted());
    }

    public void downvotePost() {
        getDownvoteButton().click();
        wait.until(d -> isDownvoted());
    }

    public boolean isUpvoted() {
        return "true".equals(getUpvoteButton().getAttribute("aria-pressed"));
    }

    public boolean isDownvoted() {
        return "true".equals(getDownvoteButton().getAttribute("aria-pressed"));
    }

    public void resetVote() {
        if (isUpvoted()) {
            getUpvoteButton().click();
            wait.until(d -> !isUpvoted());
        } else if (isDownvoted()) {
            getDownvoteButton().click();
            wait.until(d -> !isDownvoted());
        }
    }

    public int getVoteCount() {
        String countText = getVoteCountElement().getText();
        return Integer.parseInt(countText.replaceAll("[^0-9-]", ""));
    }

    // Вспомогательные методы
    private Button getUpvoteButton() {
        return Button.fromShadowHost(driver, SHADOW_HOST, UPVOTE_BUTTON);
    }

    private Button getDownvoteButton() {
        return Button.fromShadowHost(driver, SHADOW_HOST, DOWNVOTE_BUTTON);
    }

    private Button getVoteCountElement() {
        return Button.fromShadowHost(driver, SHADOW_HOST, VOTE_COUNT);
    }

    /**
     * Проверяет, что комментарий с указанным именем пользователя и текстом виден под постом.
     *
     * @param username имя автора комментария
     * @param expectedText текст комментария
     * @return true если комментарий найден и текст совпадает, иначе false
     */
    public boolean isCommentVisible(String username, String expectedText) {
        By commentLocator = By.cssSelector("shreddit-comment[author='" + username + "']");
        WebElement comment = waitForVisible(commentLocator);

        WebElement commentTextElement = comment.findElement(By.cssSelector("div[id$='-post-rtjson-content'] p"));
        String actualText = commentTextElement.getText();

        return expectedText.equals(actualText);
    }
}
