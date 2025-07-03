package pages;

import base.BasePage;
import elements.Button;
import elements.Input;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    public void clickSelectProfile(String profileName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("li[data-select-value]")
        ));

        By locator = By.xpath("//li[contains(@data-select-value, '" + profileName + "')]");
        WebElement target = driver.findElement(locator);
        target.click();
    }


    public void clickSubmitPostButton() {
        Button postButton = Button.fromShadowHost(driver,
                By.cssSelector("#submit-post-button"),
                By.cssSelector("#inner-post-submit-button"));

        postButton.jsClick();
    }
}
