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
    public static Button of(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Button(driver, element);
    }

//    /**
//     * Создаёт кнопку по локатору внутри объекта.
//     *
//     * @param by     локатор
//     * @param driver WebDriver
//     * @param webElement Веб-элемент
//     * @return объект Button
//     */
//    public static Button of(WebDriver driver, By by, WebElement webElement) {
//        WebElement element = webElement.findElement(by);
//        return new Button(driver, element);
//    }

    /**
     * Создаёт кнопку по тексту (XPath).
     *
     * @param driver WebDriver
     * @param text   текст кнопки
     * @return объект Button
     */
    public static Button byText(WebDriver driver, String text) {
        By by = By.xpath(String.format("//button[normalize-space()='%s']", text));
        return of(driver, by);
    }

    public static Button fromShadowHost(WebDriver driver, By hostLocator, By shadowLocator) {
        WebElement host = driver.findElement(hostLocator);
        SearchContext shadowRoot = host.getShadowRoot();
        WebElement element = shadowRoot.findElement(shadowLocator);
        return new Button(driver, element);
    }
}
