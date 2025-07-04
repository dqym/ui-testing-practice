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

    private static final By FEED_CONTAINER = By.cssSelector("shreddit-feed");
    private static final By POST_ITEMS = By.cssSelector("shreddit-post");
    private static final By POST_LINK = By.cssSelector("a[slot='full-post-link']");
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
     *
     * @return WebElement первого поста в ленте
     * @throws IllegalStateException если в ленте не найдено ни одного поста
     */
    public WebElement openFirstPost() {
        List<WebElement> posts = getAllPosts();

        if (posts.isEmpty()) {
            throw new IllegalStateException("Посты не найдены в ленте.");
        }

        WebElement firstPost = posts.getFirst();
        Link link = Link.fromLocator(driver, POST_LINK);

        link.jsClick();

        return firstPost;
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
    public List<WebElement> getAllPosts() {
        waitForVisible(FEED_CONTAINER);
        return waitForAllVisible(POST_ITEMS);
    }

    /**
     * Кликает по кнопке меню действий у первого поста в ленте.
     * <p>
     * Метод находит кнопку меню действий (три точки) у первого поста
     * и кликает по ней, открывая выпадающее меню с доступными действиями
     * над постом (скрыть, пожаловаться и т.д.).
     */
    public void clickPostOverflowMenu() {
        Button overflowButton = Button.fromLocator(driver, By.cssSelector("shreddit-post-overflow-menu"));

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

        Button overflowReportButton = Button.fromShadowHost(driver,
                By.cssSelector("shreddit-post-overflow-menu"),
                By.cssSelector("#post-overflow-hide"));

        overflowReportButton.click();
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
                By.cssSelector("shreddit-post-overflow-menu"),
                By.cssSelector("#post-overflow-report"));

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
                By.cssSelector("[slot-name='REPORT_REASONS']"),
                By.cssSelector("[value='SPAM']"));

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
                By.cssSelector("[slot-name='REPORT_REASONS']"),
                By.cssSelector("#report-action-button"));

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
                By.cssSelector("[slot-name='SPAM']"),
                By.cssSelector("[value='SPAM_COMMENT_FLOODING']"));

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
                By.cssSelector("[slot-name='SPAM']"),
                By.cssSelector("#report-action-button"));

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
        WebElement successPlate = driver.findElement(By.cssSelector("[slot-name='SUCCESS']"));

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
        WebElement sideBar = driver.findElement(By.cssSelector("#flex-left-nav-contents"));
        if (!sideBar.isDisplayed()) {
            Button sideBarButton = Button.fromLocator(driver, By.cssSelector("#navbar-menu-button"));
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
                By.cssSelector("left-nav-top-section"),
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
                By.cssSelector("left-nav-top-section"),
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
        Link createPost = Link.fromLocator(driver, By.cssSelector("#create-post"));
        createPost.click();
    }
}
