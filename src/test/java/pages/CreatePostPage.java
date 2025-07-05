package pages;

import elements.Button;
import elements.Input;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Страница создания нового поста на Reddit.
 * <p>
 * Класс предоставляет методы для взаимодействия с элементами формы создания поста,
 * включая заполнение заголовка, текста, выбор сообщества для публикации и отправку поста.
 * <p>
 * Страница создания поста содержит несколько основных элементов:
 * <ul>
 *   <li>Поле для ввода заголовка</li>
 *   <li>Редактор текста поста</li>
 *   <li>Выпадающее меню для выбора сообщества или профиля</li>
 *   <li>Кнопка отправки поста</li>
 * </ul>
 */
public class CreatePostPage extends BasePage {

    private static final String POST_TITLE = "test title";
    private static final String POST_TEXT = "test text";
    private final By POST_TITLE_LOCATOR = By.cssSelector("faceplate-textarea-input[name='title']");
    private final By POST_TEXT_LOCATOR = By.cssSelector("shreddit-composer[id='post-composer_bodytext']");
    private final By SUBMIT_POST_BUTTON_LOCATOR = By.cssSelector("#submit-post-button");
    private final By INNER_SUBMIT_POST_BUTTON_LOCATOR = By.cssSelector("#inner-post-submit-button");
    private final By PROFILE_PICKER_MENU_LOCATOR = By.cssSelector("#post-submit-community-picker");
    private final By PROFILE_PICKER_MENU_SEARCH_INPUT_LOCATOR = By.cssSelector("#search-input");
    private final By PROFILE_PICKER_MENU_LIST_ITEM_LOCATOR = By.cssSelector("li[data-select-value]");

    /**
     * Конструктор страницы создания поста.
     * <p>
     * Инициализирует страницу создания нового поста на Reddit.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     */
    public CreatePostPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Кликает по полю заголовка поста.
     * <p>
     * Метод находит поле для ввода заголовка поста и кликает по нему,
     * чтобы активировать для ввода текста.
     */
    public void clickTitle() {
        Input textField = Input.fromLocator(driver, POST_TITLE_LOCATOR);

        textField.click();
    }

    /**
     * Вводит стандартный текст заголовка поста.
     * <p>
     * Метод находит поле для ввода заголовка поста и вводит в него
     * предопределенный текст из константы POST_TITLE.
     */
    public void enterPostTitleText() {
        Input textField = Input.fromLocator(driver, POST_TITLE_LOCATOR);

        textField.sendKeys(POST_TITLE);
    }

    /**
     * Кликает по полю для ввода текста поста.
     * <p>
     * Метод находит область редактора текста поста и кликает по ней,
     * активируя для ввода содержимого поста.
     */
    public void clickBody() {
        Input textField = Input.fromLocator(driver, POST_TEXT_LOCATOR);

        textField.click();
    }

    /**
     * Вводит стандартный текст содержимого поста.
     * <p>
     * Метод находит область редактора текста поста и вводит в неё
     * предопределенный текст из константы POST_TEXT.
     */
    public void enterPostBodyText() {
        Input textField = Input.fromLocator(driver, POST_TEXT_LOCATOR);

        textField.sendKeys(POST_TEXT);
    }

    /**
     * Открывает выпадающее меню для выбора сообщества.
     * <p>
     * Метод кликает по элементу выбора сообщества, чтобы открыть
     * выпадающий список доступных сообществ для публикации поста.
     */
    public void clickComunityPickerMenu() {
        Button menu = Button.fromLocator(driver, PROFILE_PICKER_MENU_LOCATOR);

        menu.click();
    }

    /**
     * Вводит имя пользователя в поле поиска сообщества.
     * <p>
     * Метод вводит имя пользователя с префиксом "u/" в поле поиска
     * выпадающего меню для выбора профиля пользователя в качестве
     * места публикации поста.
     *
     * @param username имя пользователя без префикса "u/"
     */
    public void enterUsernameText(String username) {
        Input input = Input.fromShadowHost(driver,
                PROFILE_PICKER_MENU_LOCATOR,
                PROFILE_PICKER_MENU_SEARCH_INPUT_LOCATOR);

        input.sendKeys("u/" + username);
    }

    /**
     * Выбирает профиль пользователя из выпадающего списка.
     * <p>
     * Метод ожидает появления элементов в выпадающем списке после ввода текста,
     * находит элемент, содержащий указанное имя профиля, и кликает по нему.
     * Использует Shadow DOM для доступа к элементам внутри компонента выбора сообщества.
     *
     * @param profileName имя профиля для выбора
     * @throws NoSuchElementException если профиль с указанным именем не найден в списке
     */
    public void clickSelectProfile(String profileName) {
        WebElement host = driver.findElement(PROFILE_PICKER_MENU_LOCATOR);
        SearchContext shadowRoot = host.getShadowRoot();

        List<WebElement> listItems = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(driver -> {
                    List<WebElement> items = shadowRoot.findElements(PROFILE_PICKER_MENU_LIST_ITEM_LOCATOR);
                    return items.isEmpty() ? null : items;
                });

        WebElement target = listItems.stream()
                .filter(el -> el.getAttribute("data-select-value").contains(profileName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Профиль не найден: " + profileName));

        target.click();
    }

    /**
     * Кликает по кнопке отправки поста.
     * <p>
     * Метод находит кнопку отправки поста, которая находится внутри Shadow DOM,
     * и выполняет JavaScript-клик для гарантированного нажатия даже в случае
     * перекрытия или других проблем с видимостью.
     */
    public void clickSubmitPostButton() {
        Button postButton = Button.fromShadowHost(driver,
                SUBMIT_POST_BUTTON_LOCATOR,
                INNER_SUBMIT_POST_BUTTON_LOCATOR);

        postButton.jsClick();
    }
}
