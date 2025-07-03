package pages;

import base.BasePage;
import base.BaseElement;
import elements.Link;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Страница ленты постов.
 * Отвечает за действия, связанные с открытием постов.
 */
public class FeedPage extends BasePage {

    private static final By FEED_CONTAINER = By.cssSelector("shreddit-feed");
    private static final By POST_ITEMS = By.cssSelector("shreddit-post");
    private static final By POST_LINK = By.cssSelector("a[slot='full-post-link']");

    /**
     * Конструктор страницы ленты.
     *
     * @param driver WebDriver
     */
    public FeedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Открывает первый пост в ленте.
     *
     * @return WebElement первого поста
     */
    public WebElement openFirstPost() {
        waitForVisible(FEED_CONTAINER);
        List<WebElement> posts = waitForAllVisible(POST_ITEMS);

        if (posts.isEmpty()) {
            throw new IllegalStateException("Посты не найдены в ленте.");
        }

        WebElement firstPost = posts.getFirst();
        Link link = Link.of(driver, POST_LINK);

        link.jsClick();

        return firstPost;
    }

    /**
     * Скроллит элемент в центр экрана.
     *
     * @param element элемент, к которому скроллим
     */
    protected void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    /**
     * Выполняет клик по элементу.
     *
     * @param element элемент для клика
     */
    protected void click(WebElement element) {
        wait.until(driver -> {
            try {
                element.click();
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    /**
     * Получает список всех видимых постов в ленте.
     *
     * @return список элементов постов
     */
    public List<WebElement> getAllPosts() {
        waitForVisible(FEED_CONTAINER);
        return waitForAllVisible(POST_ITEMS);
    }
}
