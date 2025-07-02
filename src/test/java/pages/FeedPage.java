package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FeedPage {
    private WebDriver driver;      // Веб-драйвер для управления браузером

    // Конструктор страницы ленты, принимает WebDriver
    public FeedPage(WebDriver driver) {
        this.driver = driver;
    }

    // Метод открытия первого поста из ленты
    public void openFirstPost() {
        // Создаем объект ожидания с таймаутом 10 секунд
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Ждем появления элемента ленты с селектором "shreddit-feed"
        WebElement feed = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("shreddit-feed")
        ));

        // Ждем появления всех элементов постов с селектором "shreddit-post"
        List<WebElement> posts = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("shreddit-post")
        ));

        // Получаем первый пост из списка
        WebElement firstPost = posts.getFirst();

        // Находим внутри первого поста ссылку с атрибутом slot='full-post-link' — это ссылка на полный пост
        WebElement titleLink = firstPost.findElement(By.cssSelector("a[slot='full-post-link']"));

        // Выполняем клик по ссылке через JavaScript, чтобы избежать проблем с элементом, который может быть не кликабелен напрямую
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", titleLink);
    }
}
