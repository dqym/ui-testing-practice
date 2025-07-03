package pages;

import base.BasePage;
import base.BaseElement;
import elements.Button;
import elements.Link;
import org.openqa.selenium.*;

import java.util.List;

/**
 * Страница ленты постов.
 * Отвечает за действия, связанные с открытием постов.
 */
public class FeedPage extends BasePage {

    private static final By FEED_CONTAINER = By.cssSelector("shreddit-feed");
    private static final By POST_ITEMS = By.cssSelector("shreddit-post");
    private static final By POST_LINK = By.cssSelector("a[slot='full-post-link']");

    /**
     * Конструктор страницы ленты.
     *
     * @param driver WebDriver
     */
    public FeedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Открывает первый пост в ленте.
     *
     * @return WebElement первого поста
     */
    public WebElement openFirstPost() {
        List<WebElement> posts = getAllPosts();

        if (posts.isEmpty()) {
            throw new IllegalStateException("Посты не найдены в ленте.");
        }

        WebElement firstPost = posts.getFirst();
        Link link = Link.of(driver, POST_LINK);

        link.jsClick();

        return firstPost;
    }

    /**
     * Получает список всех видимых постов в ленте.
     *
     * @return список элементов постов
     */
    public List<WebElement> getAllPosts() {
        waitForVisible(FEED_CONTAINER);
        return waitForAllVisible(POST_ITEMS);
    }

    /**
     * Кликает по кнопке "действий" у первого поста.
     */
    public void clickPostOverflowMenu() {
        Button overflowButton = Button.of(driver, By.cssSelector("shreddit-post-overflow-menu"));

        overflowButton.click();
    }

    public void clickPostOverflowReport() {
        Button overflowReportButton = Button.fromShadowHost(driver,
                By.cssSelector("shreddit-post-overflow-menu"),
                By.cssSelector("#post-overflow-report"));

        overflowReportButton.click();
    }

    public void clickReportSPAMButton() {
        Button SPAM = Button.fromShadowHost(driver,
                By.cssSelector("[slot-name='REPORT_REASONS']"),
                By.cssSelector("[value='SPAM']"));

        SPAM.click();
    }

    public void clickNextReportButton() {
        Button nextReport = Button.fromShadowHost(driver,
                By.cssSelector("[slot-name='REPORT_REASONS']"),
                By.cssSelector("#report-action-button"));

        nextReport.jsClick();
    }

    public void clickCategoryOfSPAM() {
        Button category = Button.fromShadowHost(driver,
                By.cssSelector("[slot-name='SPAM']"),
                By.cssSelector("[value='SPAM_COMMENT_FLOODING']"));

        category.click();
    }

    public void clickSendReportButton() {
        Button sendReport = Button.fromShadowHost(driver,
                By.cssSelector("[slot-name='SPAM']"),
                By.cssSelector("#report-action-button"));

        sendReport.jsClick();
    }

    public Boolean isReportSend() {
        WebElement successPlate = driver.findElement(By.cssSelector("[slot-name='SUCCESS']"));

        return successPlate.isDisplayed();
    }

    public void expandSideBar() {
        WebElement sideBar = driver.findElement(By.cssSelector("#flex-left-nav-contents"));
        if (!sideBar.isDisplayed()) {
            Button sideBarButton = Button.of(driver, By.cssSelector("#navbar-menu-button"));
            sideBarButton.click();
        }
    }

    public void clickCategoryButton(String nameCategory) {
        Link homePageLink = Link.fromShadowHost(driver,
                By.cssSelector("left-nav-top-section"),
                By.cssSelector("faceplate-tracker[noun='" + nameCategory + "'] a"));

        homePageLink.jsClick();
    }

    public Boolean checkActivePage(String namePage) {
        Button home = Button.fromShadowHost(driver,
                By.cssSelector("left-nav-top-section"),
                By.cssSelector("#" + namePage + "-posts"));

        String href = home.getElement().findElement(By.cssSelector("a[href]")).getAttribute("href");

        return getCurrentUrl().contains(href);
    }

}
