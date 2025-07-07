package pages;

import base.BasePage;
import elements.Button;
import elements.Link;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Страница ленты постов.
 * Отвечает за действия, связанные с открытием постов.
 */
public class FeedPage extends BasePage {
    private static final By FEED_CONTAINER = By.cssSelector("shreddit-feed");
    private static final By POST_ITEMS = By.cssSelector("shreddit-post");
    private static final By POST_LINK = By.cssSelector("a[slot='full-post-link'], a[data-testid='post-title']");
    private static final By SEARCH_RESULT_TITLE = By.cssSelector("h3, [data-testid='post-title']");
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

    public void clickUserMenuButton() {
        Button UserMenuButton = Button.of(driver, By.cssSelector("#expand-user-drawer-button"));

        UserMenuButton.jsClick();
    }
    public void clickQuitButton() {
        Button QuitButton = Button.of(driver, By.cssSelector("#logout-list-item"));

        QuitButton.jsClick();
    }
    public boolean FindLoginButton(){
        Button loginButton = Button.of(driver, By.cssSelector("faceplate-tracker[noun='login']"));
        return loginButton.isVisible();
    }

    public void clickPostOverflowSaveButton() {
        Button overflowReportButton = Button.fromShadowHost(driver,
                By.cssSelector("shreddit-post-overflow-menu"),
                By.cssSelector("#post-overflow-save"));

        overflowReportButton.click();
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

    public void clickCreatePost() {
        Link createPost = Link.of(driver, By.cssSelector("#create-post"));
        createPost.click();
    }

    /**
     * Выполняет поиск по ключевому слову
     */
    public void searchFor(String keyword) {
        // Находим поле поиска через JavaScript
        WebElement searchInput = findSearchInput();

        // Очищаем и вводим текст
        searchInput.clear();
        searchInput.sendKeys(keyword);
        searchInput.sendKeys(Keys.ENTER);

        // Ждем загрузки страницы
        waitForPageLoad();
    }

    /**
     * Находит поле поиска через JavaScript
     */
    private WebElement findSearchInput() {
        return (WebElement) ((JavascriptExecutor) driver).executeScript(
                "function findSearchInput() {" +
                        "   const host = document.querySelector('reddit-search-large');" +
                        "   if (!host) return null;" +
                        "   const shadow1 = host.shadowRoot;" +
                        "   if (!shadow1) return null;" +
                        "   const faceplate = shadow1.querySelector('faceplate-search-input');" +
                        "   if (!faceplate) return null;" +
                        "   const shadow2 = faceplate.shadowRoot;" +
                        "   if (!shadow2) return null;" +
                        "   return shadow2.querySelector('input[type=\"text\"], input[type=\"search\"]');" +
                        "}" +
                        "return findSearchInput();"
        );
    }

    /**
     * Получает текст первого заголовка в результатах поиска
     */
    public String getFirstSearchResultTitle() {
        // Ждем появления заголовка
        WebElement titleElement = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.presenceOfElementLocated(SEARCH_RESULT_TITLE));

        return titleElement.getText();
    }

    /**
     * Ждет загрузки страницы
     */
    private void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState").equals("complete"));
    }
}
