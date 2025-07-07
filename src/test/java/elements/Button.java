package elements;

import base.BaseElement;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс для взаимодействия с элементом кнопки.
 */
public class Button extends BaseElement {

    public Button(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Создаёт кнопку по локатору.
     *
     * @param by     локатор
     * @param driver WebDriver
     * @return объект Button
     */
    public static Button of(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Button(driver, element);
    }

    // Добавляем методы для работы с элементами
    public String getAttribute(String name) {
        return getElement().getAttribute(name);
    }

    public String getText() {
        return getElement().getText();
    }


    /**
     * Создаёт кнопку по тексту (XPath).
     *
     * @param driver WebDriver
     * @param text   текст кнопки
     * @return объект Button
     */

    public static Button fromShadowHost(WebDriver driver, By hostLocator, By shadowLocator) {
        WebElement host = driver.findElement(hostLocator);
        SearchContext shadowRoot = host.getShadowRoot();
        WebElement element = shadowRoot.findElement(shadowLocator);
        return new Button(driver, element);
    }
}
