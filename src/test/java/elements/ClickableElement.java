package elements;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Базовый класс для кликабельных элементов интерфейса.
 * <p>
 * Класс расширяет базовую функциональность {@link BaseElement} и добавляет
 * специфические методы для элементов, по которым можно кликать (кнопки, ссылки и т.д.).
 * <p>
 * Предоставляет методы для выполнения как обычного клика (с ожиданием кликабельности),
 * так и клика через JavaScript (для случаев, когда обычный клик не работает).
 */
public abstract class ClickableElement extends BaseElement {

    /**
     * Конструктор для кликабельного элемента.
     * <p>
     * Создает кликабельный элемент на основе уже найденного WebElement.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param element WebElement, представляющий кликабельный элемент на странице
     */
    protected ClickableElement(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Кликает по элементу с ожиданием его кликабельности.
     * <p>
     * Метод добавляет явное ожидание кликабельности элемента перед выполнением клика.
     * Это помогает избежать ошибок при попытке клика по неактивным элементам.
     * 
     * @throws TimeoutException если элемент не стал кликабельным за отведенное время
     * @throws ElementClickInterceptedException если клик перехвачен другим элементом
     */
    public void click() {
        if (locator != null) {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        }
    }

    /**
     * Кликает по элементу через JavaScript.
     */
    public void jsClick() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement toClick = (element != null) ? element : driver.findElement(locator);
        js.executeScript("arguments[0].click();", toClick);
    }
}
