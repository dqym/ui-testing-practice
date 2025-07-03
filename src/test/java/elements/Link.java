package elements;

import base.BaseElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс для взаимодействия с элементом-ссылкой <a>.
 */
public class Link extends BaseElement {

    /**
     * Конструктор, инициализирующий ссылку по WebElement.
     *
     * @param driver  WebDriver
     * @param element WebElement
     */
    public Link(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Фабричный метод создания ссылки по локатору.
     *
     * @param by     локатор ссылки
     * @param driver WebDriver
     * @return объект Link
     */
    public static Link of(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Link(driver, element);
    }

    /**
     * Фабричный метод создания ссылки по тексту.
     *
     * @param driver WebDriver
     * @param linkText текст ссылки
     * @return объект Link
     */
    public static Link byText(WebDriver driver, String linkText) {
        return of(driver, By.linkText(linkText));
    }

    /**
     * Возвращает URL, на который указывает ссылка.
     *
     * @return значение атрибута href
     */
    public String getHref() {
        return element.getAttribute("href");
    }

    /**
     * Проверяет, ведёт ли ссылка на указанный URL.
     *
     * @param url ожидаемый URL
     * @return true, если href совпадает
     */
    public boolean pointsTo(String url) {
        return getHref().equalsIgnoreCase(url);
    }

    /**
     * Кликает по ссылке через JavaScript.
     */
    public void jsClick() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}
