package elements;

import base.BaseElement;
import org.openqa.selenium.By;
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
}
