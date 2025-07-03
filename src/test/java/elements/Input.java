package elements;

import base.BaseElement;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Класс для взаимодействия с элементом input.
 */
public class Input extends BaseElement {

    protected Input(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    public static Input of(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Input(driver, element);
    }

    public static Input byClass(WebDriver driver, String className) {
        return of(driver, By.className(className));
    }

    public static Input fromShadowHost(WebDriver driver, By hostLocator, By shadowLocator) {
        WebElement host = driver.findElement(hostLocator);
        SearchContext shadowRoot = host.getShadowRoot();
        WebElement element = shadowRoot.findElement(shadowLocator);
        return new Input(driver, element);
    }

}
