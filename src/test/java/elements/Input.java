package elements;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс для взаимодействия с элементами ввода (input).
 * <p>
 * Класс предоставляет функциональность для работы с полями ввода на веб-странице,
 * включая текстовые поля, текстовые области и другие элементы ввода.
 * <p>
 * Поддерживает различные способы создания объекта поля ввода, включая поиск
 * по локатору, классу или внутри Shadow DOM.
 */
public class Input extends ClickableElement {

    /**
     * Защищенный конструктор для создания объекта Input на основе WebElement.
     * <p>
     * Создает поле ввода на основе уже найденного WebElement.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param element WebElement, представляющий поле ввода на странице
     */
    protected Input(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Создает объект Input по указанному локатору.
     * <p>
     * Метод ищет элемент ввода на странице по заданному локатору
     * и создает на его основе объект Input.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param by локатор для поиска элемента ввода
     * @return новый объект Input
     * @throws org.openqa.selenium.NoSuchElementException если элемент не найден по указанному локатору
     */
    public static Input fromLocator(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Input(driver, element);
    }

    /**
     * Создает объект Input по имени класса элемента.
     * <p>
     * Вспомогательный метод для быстрого создания поля ввода по его CSS-классу.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param className имя CSS-класса элемента ввода
     * @return новый объект Input
     * @throws org.openqa.selenium.NoSuchElementException если элемент с указанным классом не найден
     */
    public static Input byClass(WebDriver driver, String className) {
        return fromLocator(driver, By.className(className));
    }

    /**
     * Создает объект Input для элемента, находящегося в Shadow DOM.
     * <p>
     * Метод ищет элемент-хост Shadow DOM по указанному локатору,
     * затем находит элемент ввода внутри Shadow DOM и создает объект Input.
     * <p>
     * Этот метод полезен для работы с веб-компонентами, использующими Shadow DOM,
     * что часто встречается в современных веб-приложениях.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param hostLocator локатор для поиска элемента-хоста Shadow DOM
     * @param shadowLocator локатор для поиска элемента ввода внутри Shadow DOM
     * @return новый объект Input
     * @throws org.openqa.selenium.NoSuchElementException если хост или элемент внутри Shadow DOM не найден
     */
    public static Input fromShadowHost(WebDriver driver, By hostLocator, By shadowLocator) {
        WebElement host = driver.findElement(hostLocator);
        SearchContext shadowRoot = host.getShadowRoot();
        WebElement element = shadowRoot.findElement(shadowLocator);
        return new Input(driver, element);
    }

    /**
     * Вводит указанный текст в поле ввода.
     * <p>
     * Метод имитирует ввод текста пользователем в поле ввода.
     * Текст добавляется к существующему содержимому поля ввода.
     *
     * @param text текст для ввода в поле
     * @throws org.openqa.selenium.ElementNotInteractableException если элемент найден, но ввод в него невозможен
     */
    public void sendKeys(String text) {
        WebElement temp_element = getElement();
        temp_element.sendKeys(text);
    }
}
