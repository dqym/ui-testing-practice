package elements;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс для взаимодействия с элементом кнопки.
 */
public class Button extends ClickableElement {

    protected Button(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Создаёт кнопку по локатору.
     *
     * @param by     локатор
     * @param driver WebDriver
     * @return объект Button
     */
    public static Button fromLocator(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Button(driver, element);
    }

    /**
     * Создает объект Button для элемента, находящегося в Shadow DOM.
     * <p>
     * Метод ищет элемент-хост Shadow DOM по указанному локатору,
     * затем находит элемент ввода внутри Shadow DOM и создает объект Button.
     * <p>
     * Этот метод полезен для работы с веб-компонентами, использующими Shadow DOM,
     * что часто встречается в современных веб-приложениях.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param hostLocator локатор для поиска элемента-хоста Shadow DOM
     * @param shadowLocator локатор для поиска элемента ввода внутри Shadow DOM
     * @return новый объект Button
     * @throws org.openqa.selenium.NoSuchElementException если хост или элемент внутри Shadow DOM не найден
     */
    public static Button fromShadowHost(WebDriver driver, By hostLocator, By shadowLocator) {
        WebElement host = driver.findElement(hostLocator);
        SearchContext shadowRoot = host.getShadowRoot();
        WebElement element = shadowRoot.findElement(shadowLocator);
        return new Button(driver, element);
    }
}
