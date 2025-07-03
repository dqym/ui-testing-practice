package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Страница отдельного поста.
 * Отвечает за добавление комментариев и проверку их отображения.
 */
public class PostPage extends BasePage {

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

    /**
     * Скроллит элемент в центр видимой области браузера.
     *
     * @param element элемент для скролла
     */
    private void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
