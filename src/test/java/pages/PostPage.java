package pages;

import elements.Button;
import elements.Comment;
import elements.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Страница отдельного поста.
 * Отвечает за добавление комментариев и проверку их отображения.
 */
public class PostPage extends BasePage {

    private final By ADD_COMMENT_BUTTON = By.cssSelector("[noun='add_comment_button']");
    private final By COMMENT_INPUT_CONTAINER = By.cssSelector("#main-content shreddit-composer > div:nth-child(1)");
    private final By SUBMIT_COMMENT_BUTTON = By.cssSelector("button[slot='submit-button']");

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
        Button addCommentBtn = Button.fromLocator(driver, ADD_COMMENT_BUTTON);
        scrollToCenter(addCommentBtn.getElement());

        addCommentBtn.click();
    }

    /**
     * Вводит текст комментария в поле ввода.
     *
     * @param text текст комментария
     */
    public void enterCommentText(String text) {
        Input commentInput = Input.fromLocator(driver, COMMENT_INPUT_CONTAINER);

        commentInput.sendKeys(text);
    }

    /**
     * Кликает по кнопке отправки комментария.
     */
    public void clickSubmitCommentButton() {
        Button submitBtn = Button.fromLocator(driver, SUBMIT_COMMENT_BUTTON);

        submitBtn.jsClick();
    }

    /**
     * Проверяет, что комментарий с указанным именем пользователя и текстом виден под постом.
     * <p>
     * Метод выполняет следующие действия:
     * <ol>
     *   <li>Ищет комментарий от указанного пользователя по атрибуту author</li>
     *   <li>Извлекает текст найденного комментария</li>
     *   <li>Сравнивает фактический текст с ожидаемым</li>
     * </ol>
     * <p>
     * Если комментарий не найден, будет выброшено исключение NoSuchElementException,
     * которое может быть обработано вызывающим кодом.
     *
     * @param username имя автора комментария для поиска
     * @param expectedText ожидаемый текст комментария для проверки
     * @return true если комментарий найден и его текст совпадает с ожидаемым, иначе false
     * @throws org.openqa.selenium.NoSuchElementException если комментарий от указанного пользователя не найден
     */
    public boolean isCommentVisible(String username, String expectedText) {
        Comment comment = Comment.fromLocator(driver, By.cssSelector("shreddit-comment[author='" + username + "']"));
        String actualText = comment.getText();

        return expectedText.equals(actualText);
    }
}
