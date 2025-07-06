package pages;

import elements.Button;
import elements.Link;
import org.openqa.selenium.*;

import java.util.List;

/**
 * Страница ленты постов Reddit.
 * <p>
 * Класс предоставляет методы для взаимодействия с лентой постов Reddit,
 * включая просмотр постов, отправку жалоб, скрытие постов, навигацию по
 * категориям и другие действия, доступные на главной странице ленты.
 * <p>
 * Лента постов является основным интерфейсом Reddit, где пользователи
 * просматривают контент и взаимодействуют с ним. Страница содержит список
 * постов с возможностью выполнять различные действия над ними.
 */
public class FeedPage extends BasePage {

    private final By FEED_CONTAINER_LOCATOR = By.cssSelector("shreddit-feed");
    private final By POST_ITEMS_LOCATOR = By.cssSelector("shreddit-post");
    private final By POST_LINK_LOCATOR = By.cssSelector("a[slot='full-post-link']");
    private final By POST_OVERFLOW_MENU_LOCATOR = By.cssSelector("shreddit-post-overflow-menu");
    private final By POST_OVERFLOW_HIDE_LOCATOR = By.cssSelector("#post-overflow-hide");
    private final By POST_OVERFLOW_REPORT_LOCATOR = By.cssSelector("#post-overflow-report");
    private final By POST_OVERFLOW_REPORT_TYPE_LOCATOR = By.cssSelector("[slot-name='REPORT_REASONS']");
    private final By POST_OVERFLOW_SPAM_LOCATOR = By.cssSelector("[value='SPAM']");
    private final By POST_OVERFLOW_REPORT_NEXT_LOCATOR = By.cssSelector("#report-action-button");
    private final By POST_OVERFLOW_SPAM_CATEGORY_LOCATOR = By.cssSelector("[slot-name='SPAM']");
    private final By POST_OVERFLOW_REPORT_FLOOD_LOCATOR = By.cssSelector("[value='SPAM_COMMENT_FLOODING']");
    private final By POST_REPORT_SEND_SUCCESSFUL_LOCATOR = By.cssSelector("[slot-name='SUCCESS']");
    private final By SIDEBAR_MENU_LOCATOR = By.cssSelector("#flex-left-nav-contents");
    private final By SIDEBAR_EXPAND_BUTTON_LOCATOR = By.cssSelector("#navbar-menu-button");
    private final By SIDEBAR_CATEGORY_LOCATOR = By.cssSelector("left-nav-top-section");
    private final By CREATE_POST_LINK_LOCATOR = By.cssSelector("#create-post");
    private String hiddenPostPermalink;


    /**
     * Конструктор страницы ленты постов.
     * <p>
     * Инициализирует страницу ленты постов Reddit.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     */
    public FeedPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Открывает первый пост в ленте.
     * <p>
     * Метод находит первый пост в ленте и кликает по его ссылке,
     * открывая пост для просмотра. Использует JavaScript-клик для
     * гарантированного срабатывания даже при возможных проблемах
     * с видимостью элемента.
     * @return PostPage элемент страницы, который представляет пост
     * @throws IllegalStateException если в ленте не найдено ни одного поста
     */
    public PostPage openFirstPost() {
        List<WebElement> posts = getAllPosts();

        if (posts.isEmpty()) {
            throw new IllegalStateException("Посты не найдены в ленте.");
        }

        Link link = Link.fromLocator(driver, POST_LINK_LOCATOR);

        link.jsClick();

        return new PostPage(driver);
    }

    /**
     * Получает список всех видимых постов в ленте.
     * <p>
     * Метод ожидает загрузки контейнера ленты и затем
     * получает список всех видимых постов внутри него.
     * Использует ожидание видимости элементов для защиты
     * от проблем с асинхронной загрузкой контента.
     *
     * @return список элементов постов, отображаемых в ленте
     * @throws TimeoutException если контейнер ленты не загрузился за отведенное время
     */
    private List<WebElement> getAllPosts() {
        waitForVisible(FEED_CONTAINER_LOCATOR);
        return waitForAllVisible(POST_ITEMS_LOCATOR);
    }

    /**
     * Кликает по кнопке меню действий у первого поста в ленте.
     * <p>
     * Метод находит кнопку меню действий (три точки) у первого поста
     * и кликает по ней, открывая выпадающее меню с доступными действиями
     * над постом (скрыть, пожаловаться и т.д.).
     */
    public void clickPostOverflowMenu() {
        Button overflowButton = Button.fromLocator(driver, POST_OVERFLOW_MENU_LOCATOR);

        overflowButton.click();
    }

    /**
     * Скрывает первый пост в ленте.
     * <p>
     * Метод выполняет следующие действия:
     *   Получает список всех постов в ленте
     *   Сохраняет permalink первого поста для последующей проверки
     *   Находит кнопку "Скрыть" в меню действий поста через Shadow DOM
     *   Кликает по кнопке, чтобы скрыть пост
     * <p>
     * Для корректной работы этого метода меню действий поста должно быть
     * уже открыто с помощью метода {@link #clickPostOverflowMenu()}.
     * 
     * @throws NoSuchElementException если не удалось найти кнопку "Скрыть" в меню
     * @throws IllegalStateException если в ленте нет постов
     */
    public void clickPostOverflowHide() {
        List<WebElement> posts = getAllPosts();
        WebElement firstPost = posts.getFirst();

        hiddenPostPermalink = firstPost.getAttribute("permalink");

        Button overflowHideButton = Button.fromShadowHost(driver,
                POST_OVERFLOW_MENU_LOCATOR,
                POST_OVERFLOW_HIDE_LOCATOR);

        overflowHideButton.click();
    }

    /**
     * Проверяет, был ли скрыт пост после выполнения действия "Скрыть".
     * <p>
     * Метод ищет пост с сохраненным permalink и проверяет,
     * присутствует ли у него атрибут "hidden", указывающий на то,
     * что пост был скрыт.
     * <p>
     * Этот метод должен вызываться после {@link #clickPostOverflowHide()}.
     *
     * @return true если пост найден и имеет атрибут "hidden", иначе false
     */
    public boolean isPostHidden() {
        List<WebElement> posts = getAllPosts();

        for (WebElement post : posts) {
            String permalink = post.getAttribute("permalink");

            if (permalink != null && permalink.equals(hiddenPostPermalink)) {
                String hiddenAttr = post.getAttribute("hidden");
                return hiddenAttr != null;
            }
        }

        return false;
    }

    /**
     * Кликает по кнопке "Пожаловаться" в меню действий поста.
     * <p>
     * Метод находит кнопку "Пожаловаться" в меню действий поста через Shadow DOM
     * и кликает по ней, открывая форму отправки жалобы.
     * <p>
     * Для корректной работы этого метода меню действий поста должно быть
     * уже открыто с помощью метода {@link #clickPostOverflowMenu()}.
     * 
     * @throws NoSuchElementException если не удалось найти кнопку "Пожаловаться" в меню
     */
    public void clickPostOverflowReport() {
        Button overflowReportButton = Button.fromShadowHost(driver,
                POST_OVERFLOW_MENU_LOCATOR,
                POST_OVERFLOW_REPORT_LOCATOR);

        overflowReportButton.click();
    }

    /**
     * Выбирает причину жалобы "СПАМ" в форме отправки жалобы.
     * <p>
     * Метод находит радиокнопку с причиной жалобы "СПАМ" через Shadow DOM
     * и кликает по ней, выбирая эту причину для жалобы на пост.
     * <p>
     * Этот метод должен вызываться после {@link #clickPostOverflowReport()}.
     * 
     * @throws NoSuchElementException если не удалось найти опцию "СПАМ" в форме
     */
    public void clickReportSPAMButton() {
        Button SPAM = Button.fromShadowHost(driver,
                POST_OVERFLOW_REPORT_TYPE_LOCATOR,
                POST_OVERFLOW_SPAM_LOCATOR);

        SPAM.click();
    }

    /**
     * Кликает по кнопке "Далее" в форме отправки жалобы.
     * <p>
     * Метод находит кнопку "Далее" через Shadow DOM и кликает по ней,
     * переходя к следующему шагу отправки жалобы.
     * <p>
     * Использует JavaScript-клик для надежности срабатывания.
     * 
     * @throws NoSuchElementException если не удалось найти кнопку "Далее"
     */
    public void clickNextReportButton() {
        Button nextReport = Button.fromShadowHost(driver,
                POST_OVERFLOW_REPORT_TYPE_LOCATOR,
                POST_OVERFLOW_REPORT_NEXT_LOCATOR);

        nextReport.jsClick();
    }

    /**
     * Выбирает подкатегорию СПАМ "Флуд комментариями" в форме отправки жалобы.
     * <p>
     * Метод находит радиокнопку с конкретным типом спама через Shadow DOM
     * и кликает по ней, уточняя причину жалобы.
     * <p>
     * Этот метод должен вызываться после {@link #clickNextReportButton()}.
     * 
     * @throws NoSuchElementException если не удалось найти опцию в форме
     */
    public void clickCategoryOfSPAM() {
        Button category = Button.fromShadowHost(driver,
                POST_OVERFLOW_SPAM_CATEGORY_LOCATOR,
                POST_OVERFLOW_REPORT_FLOOD_LOCATOR);

        category.click();
    }

    /**
     * Кликает по кнопке отправки жалобы в финальном шаге формы.
     * <p>
     * Метод находит кнопку отправки жалобы через Shadow DOM и кликает по ней,
     * завершая процесс отправки жалобы.
     * <p>
     * Использует JavaScript-клик для надежности срабатывания.
     * 
     * @throws NoSuchElementException если не удалось найти кнопку отправки
     */
    public void clickSendReportButton() {
        Button sendReport = Button.fromShadowHost(driver,
                POST_OVERFLOW_SPAM_CATEGORY_LOCATOR,
                POST_OVERFLOW_REPORT_NEXT_LOCATOR);

        sendReport.jsClick();
    }

    /**
     * Проверяет, была ли успешно отправлена жалоба.
     * <p>
     * Метод проверяет наличие и видимость элемента успешной отправки жалобы,
     * который появляется после завершения процесса отправки жалобы.
     *
     * @return true если жалоба была успешно отправлена, иначе false
     * @throws NoSuchElementException если не удалось найти элемент успешной отправки
     */
    public Boolean isReportSend() {
        WebElement successPlate = driver.findElement(POST_REPORT_SEND_SUCCESSFUL_LOCATOR);

        return successPlate.isDisplayed();
    }

    /**
     * Раскрывает боковую панель навигации, если она скрыта.
     * <p>
     * Метод проверяет, отображается ли боковая панель навигации.
     * Если панель скрыта, метод кликает по кнопке меню навигации,
     * чтобы раскрыть боковую панель.
     */
    public void expandSideBar() {
        WebElement sideBar = driver.findElement(SIDEBAR_MENU_LOCATOR);
        if (!sideBar.isDisplayed()) {
            Button sideBarButton = Button.fromLocator(driver, SIDEBAR_EXPAND_BUTTON_LOCATOR);
            sideBarButton.click();
        }
    }

    /**
     * Кликает по кнопке выбранной категории в боковой панели навигации.
     * <p>
     * Метод находит ссылку на указанную категорию в боковой панели
     * через Shadow DOM и кликает по ней, переходя на страницу выбранной
     * категории (например, "home", "popular", "all").
     * <p>
     * Использует JavaScript-клик для надежности срабатывания.
     *
     * @param nameCategory название категории для перехода (например, "home", "popular", "all")
     * @throws NoSuchElementException если категория с указанным именем не найдена
     */
    public void clickCategoryButton(String nameCategory) {
        Link homePageLink = Link.fromShadowHost(driver,
                SIDEBAR_CATEGORY_LOCATOR,
                By.cssSelector("faceplate-tracker[noun='" + nameCategory + "'] a"));

        homePageLink.jsClick();
    }

    /**
     * Проверяет, соответствует ли текущий URL выбранной категории.
     * <p>
     * Метод находит элемент выбранной категории в боковой панели,
     * извлекает из него URL-адрес и сравнивает с текущим URL страницы,
     * чтобы определить, находится ли пользователь на странице выбранной
     * категории.
     *
     * @param namePage название категории для проверки (например, "home", "popular", "all")
     * @return true если текущий URL содержит URL выбранной категории, иначе false
     * @throws NoSuchElementException если категория с указанным именем не найдена
     */
    public Boolean checkActivePage(String namePage) {
        Button home = Button.fromShadowHost(driver,
                SIDEBAR_CATEGORY_LOCATOR,
                By.cssSelector("#" + namePage + "-posts"));

        String href = home.getElement().findElement(By.cssSelector("a[href]")).getAttribute("href");

        return getCurrentUrl().contains(href);
    }

    /**
     * Кликает по кнопке создания нового поста.
     * <p>
     * Метод находит ссылку на создание нового поста и кликает по ней,
     * переходя на страницу создания поста.
     *
     * @throws NoSuchElementException если кнопка создания поста не найдена
     */
    public void clickCreatePost() {
        Link createPost = Link.fromLocator(driver, CREATE_POST_LINK_LOCATOR);
        createPost.click();
    }
}
