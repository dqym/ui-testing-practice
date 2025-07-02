package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PostPage {
    private WebDriver driver;  // Веб-драйвер для управления браузером

    // Конструктор принимает WebDriver и сохраняет в поле класса
    public PostPage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод для добавления комментария с текстом `text`
    public void addComment(String text) {
        // Создаем WebDriverWait с таймаутом 10 секунд
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ждем, пока кнопка "добавить комментарий" станет кликабельной, находим её по атрибуту noun
        WebElement commentBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[noun='add_comment_button']")));

        // Скроллим кнопку в центр видимой области браузера, чтобы на неё можно было кликнуть
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", commentBox);

        // Кликаем по кнопке "добавить комментарий"
        commentBox.click();

        // Находим внутри страницы элемент ввода комментария.
        // XPath указывает на div[1] внутри shreddit-composer внутри main-content.
        WebElement innerInput = commentBox.findElement(By.xpath("//*[@id=\"main-content\"]//shreddit-composer/div[1]\n"));

        // Вводим текст комментария в найденное поле
        innerInput.sendKeys(text);

        // Находим кнопку отправки комментария (submit-button) и кликаем по ней
        WebElement commentButton = driver.findElement(By.cssSelector("button[slot='submit-button']"));
        commentButton.click();
    }

    // Метод проверяет, что комментарий от пользователя `username` с текстом `expectedText` отображается
    public boolean isCommentVisible(String username, String expectedText) {
        // Находим элемент комментария по автору (атрибут author)
        WebElement comment = driver.findElement(By.cssSelector("shreddit-comment[author='" + username + "']"));

        // Внутри комментария находим содержимое текста (параграф в div с id, оканчивающимся на '-post-rtjson-content')
        String commentText = comment.findElement(By.cssSelector("div[id$='-post-rtjson-content'] p")).getText();

        // Сравниваем текст комментария с ожидаемым и возвращаем результат
        return commentText.equals(expectedText);
    }
}
