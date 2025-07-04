package base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Базовая страница для всех страниц сайта.
 * Содержит общие методы взаимодействия со страницей и ожидания.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Конструктор базовой страницы.
     *
     * @param driver WebDriver
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    /**
     * Открывает указанную страницу по URL.
     *
     * @param url адрес страницы
     */
    public void open(String url) {
        driver.get(url);
    }

    /**
     * Обновляет текущую страницу в браузере.
     */
    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * Проверяет, что заголовок страницы содержит указанный текст.
     *
     * @param partialTitle часть заголовка
     * @return true, если заголовок содержит текст
     */
    public boolean isTitleContains(String partialTitle) {
        return wait.until(ExpectedConditions.titleContains(partialTitle));
    }

    /**
     * Ждёт, пока указанный элемент станет видимым.
     *
     * @param locator локатор элемента
     * @return WebElement после появления
     */
    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                if (!element.isDisplayed()) {
                    return null;
                }
            }
            return elements;
        });
    }

    /**
     * Ждёт, пока элемент станет кликабельным.
     *
     * @param locator локатор элемента
     * @return WebElement после готовности к клику
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Возвращает текущий URL страницы.
     *
     * @return текущий URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Скроллит элемент в центр видимой области браузера.
     *
     * @param element элемент для скролла
     */
    public void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }
}

