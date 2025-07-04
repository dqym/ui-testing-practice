package elements;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовый класс для всех элементов на странице.
 * <p>
 * Класс предоставляет общую функциональность для всех элементов пользовательского интерфейса,
 * таких как кнопки, поля ввода, ссылки и т.д. Содержит базовые методы для взаимодействия
 * с элементами, включая поиск элементов, клики, получение текста и проверку видимости.
 * <p>
 * Поддерживает два способа инициализации: через локатор (By) или через уже найденный WebElement.
 */
public abstract class BaseElement {

    /** Экземпляр WebDriver для взаимодействия с браузером. */
    protected final WebDriver driver;

    /** Объект ожидания с настроенным таймаутом. */
    protected final WebDriverWait wait;

    /** Локатор элемента (может быть null, если элемент инициализирован через WebElement). */
    protected final By locator;

    /** Ссылка на WebElement (может быть null, если элемент инициализирован через локатор). */
    protected final WebElement element;

    /** Стандартный таймаут ожидания в секундах. */
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    /**
     * Конструктор через локатор.
     * <p>
     * Создает элемент, который будет найден по указанному локатору при первом обращении.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param locator локатор для поиска элемента на странице
     */
    public BaseElement(WebDriver driver, By locator) {
        this.driver = driver;
        this.locator = locator;
        this.element = null;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    /**
     * Конструктор через WebElement.
     * <p>
     * Создает элемент на основе уже найденного WebElement.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param element уже найденный WebElement
     */
    public BaseElement(WebDriver driver, WebElement element) {
        this.driver = driver;
        this.element = element;
        this.locator = null;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    /**
     * Возвращает WebElement, либо из кэша, либо через ожидание по локатору.
     * <p>
     * Если элемент был инициализирован через WebElement, возвращает его напрямую.
     * Если через локатор - ищет элемент на странице с ожиданием его появления.
     *
     * @return WebElement для дальнейшего взаимодействия
     * @throws TimeoutException если элемент не найден за отведенное время
     */
    public WebElement getElement() {
        if (element != null) {
            return element;
        }
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Проверяет, виден ли элемент на странице.
     * <p>
     * Метод безопасно обрабатывает случаи, когда элемент не найден или не виден,
     * возвращая false вместо выбрасывания исключения.
     *
     * @return true если элемент найден и отображается, иначе false
     */
    public boolean isVisible() {
        try {
            return getElement().isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }

    /**
     * Получает текст элемента.
     * <p>
     * Возвращает текстовое содержимое элемента, видимое пользователю.
     *
     * @return текст элемента
     * @throws TimeoutException если элемент не найден за отведенное время
     */
    public String getText() {
        return getElement().getText();
    }

    /**
     * Прокручивает страницу к элементу, чтобы он был виден в центре экрана.
     * <p>
     * Метод использует JavaScript для прокрутки страницы таким образом,
     * чтобы элемент оказался в центре видимой области.
     */
    public void scrollToElement() {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", getElement());
    }
}
