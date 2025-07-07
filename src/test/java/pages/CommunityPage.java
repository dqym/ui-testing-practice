package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CommunityPage extends BasePage {

    // Основной хост
    private static final By HEADER_BUTTONS_HOST = By.cssSelector("shreddit-subreddit-header-buttons");

    // Более гибкий селектор для трекера
    private static final By SUBSCRIBE_TRACKER = By.cssSelector("faceplate-tracker");

    // Селекторы для кнопки
    private static final By JOIN_BUTTON_HOST = By.cssSelector("shreddit-join-button");
    private static final By JOIN_BUTTON = By.cssSelector("button.join-btn, button[class*='join-btn']");

    public CommunityPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Кликает на кнопку подписки/отписки
     */
    public void clickSubscribeButton() {
        WebElement button = findSubscribeButton();
        highlightElement(button);
        button.click();
    }

    /**
     * Получает текст кнопки подписки
     */
    public String getSubscribeButtonText() {
        return findSubscribeButton().getText();
    }

    /**
     * Находит кнопку подписки через Shadow DOM
     */
    private WebElement findSubscribeButton() {
        // 1. Находим основной хост с явным ожиданием
        WebElement headerHost = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(HEADER_BUTTONS_HOST));

        // 2. Получаем первый Shadow DOM
        SearchContext firstShadow = expandShadowRoot(headerHost);

        // 3. Находим faceplate-tracker (более гибкий поиск)
        WebElement tracker = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> firstShadow.findElements(SUBSCRIBE_TRACKER)
                        .stream()
                        .filter(WebElement::isDisplayed)
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("No visible subscribe tracker found")));

        // 4. Внутри tracker находим join-хост
        WebElement joinHost = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> tracker.findElement(JOIN_BUTTON_HOST));

        // 5. Получаем Shadow DOM join-хоста
        SearchContext secondShadow = expandShadowRoot(joinHost);

        // 6. Находим кнопку с расширенным селектором
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOf(
                        secondShadow.findElement(JOIN_BUTTON)
                ));
    }

    /**
     * Вспомогательный метод для доступа к Shadow Root
     */
    private SearchContext expandShadowRoot(WebElement element) {
        return (SearchContext) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot", element);
    }

    /**
     * Подсвечивает элемент (для отладки)
     */
    private void highlightElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border = '3px solid red'",
                element
        );
    }

    /**
     * Ждет появления кнопки подписки
     */
    public void waitForSubscribeButton() {
        new WebDriverWait(driver, Duration.ofSeconds(25))
                .until(d -> {
                    try {
                        WebElement button = findSubscribeButton();
                        return button != null && button.isDisplayed();
                    } catch (Exception e) {
                        System.out.println("Ожидание кнопки: " + e.getMessage());
                        return false;
                    }
                });
    }

    /**
     * Проверяет, подписан ли пользователь
     */
    public boolean isSubscribed() {
        try {
            String text = getSubscribeButtonText();
            return "В сообществе".equals(text) || "B coo6щecтвe".equals(text);
        } catch (Exception e) {
            return false;
        }
    }
}