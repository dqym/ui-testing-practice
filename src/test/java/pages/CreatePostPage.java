package pages;

import base.BasePage;
import elements.Button;
import elements.Input;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CreatePostPage extends BasePage {

    private static final String POST_TITLE = "test1";
    private static final String POST_TEXT = "test1";

    public CreatePostPage(WebDriver driver) {
        super(driver);
    }

    public void clickTitle() {
        Input textField = Input.of(driver, By.cssSelector("faceplate-textarea-input[name='title']"));

        textField.click();
    }

    public void enterPostTitleText() {
        Input textField = Input.of(driver, By.cssSelector("faceplate-textarea-input[name='title']"));

        textField.sendKeys(POST_TITLE);
    }

    public void clickBody() {
        Input textField = Input.of(driver, By.cssSelector("shreddit-composer[id='post-composer_bodytext']"));

        textField.click();
    }

    public void enterPostBodyText() {
        Input textField = Input.of(driver, By.cssSelector("shreddit-composer[id='post-composer_bodytext']"));

        textField.sendKeys(POST_TEXT);
    }

    public void clickComunityPickerMenu() {
        Button menu = Button.of(driver, By.cssSelector("#post-submit-community-picker"));

        menu.click();
    }

    public void enterUsernameText(String username) {
        Input input = Input.fromShadowHost(driver,
                By.cssSelector("#post-submit-community-picker"),
                By.cssSelector("#search-input"));

        input.sendKeys("u/" + username);
    }

    public void clickSelectProfile(String profileName) {
        WebElement host = driver.findElement(By.cssSelector("#post-submit-community-picker"));
        SearchContext shadowRoot = host.getShadowRoot();

        List<WebElement> listItems = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> {
                    List<WebElement> items = shadowRoot.findElements(By.cssSelector("li[data-select-value]"));
                    return items.isEmpty() ? null : items;
                });

        WebElement target = listItems.stream()
                .filter(el -> el.getAttribute("data-select-value").contains(profileName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Профиль не найден: " + profileName));

        target.click();
    }




    public void clickSubmitPostButton() {
        Button postButton = Button.fromShadowHost(driver,
                By.cssSelector("#submit-post-button"),
                By.cssSelector("#inner-post-submit-button"));

        postButton.jsClick();
    }
}
