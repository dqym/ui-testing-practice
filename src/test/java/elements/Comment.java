package elements;

import org.openqa.selenium.*;

/**
 * Класс представляет комментарий на Reddit.
 * <p>
 * Этот класс обеспечивает взаимодействие с элементом комментария на странице поста.
 * Позволяет получать информацию о комментарии, такую как автор и текст комментария.
 * <p>
 * Комментарий определяется элементом `shreddit-comment` в DOM-структуре страницы.
 */
public class Comment extends BaseElement {

    private final By COMMENT_TEXT = By.cssSelector("div[id$='-post-rtjson-content'] p");

    /**
     * Конструктор комментария на основе WebElement.
     * <p>
     * Создает объект комментария на основе уже найденного WebElement.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param element WebElement корневого элемента комментария
     */
    public Comment(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    /**
     * Создает объект комментария по указанному локатору.
     * <p>
     * Метод ищет элемент комментария на странице по заданному локатору
     * и создает на его основе объект Comment.
     *
     * @param driver экземпляр WebDriver для взаимодействия с браузером
     * @param by локатор для поиска элемента комментария
     * @return новый объект Comment
     * @throws NoSuchElementException если комментарий не найден по указанному локатору
     */
    public static Comment fromLocator(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return new Comment(driver, element);
    }

    /**
     * Возвращает имя автора комментария.
     * <p>
     * Метод извлекает значение атрибута "author" из элемента комментария,
     * который содержит имя пользователя, оставившего комментарий.
     *
     * @return имя автора комментария
     */
    public String getAuthor() {
        return getElement().getAttribute("author");
    }

    /**
     * Возвращает текст комментария.
     * <p>
     * Метод находит элемент, содержащий текст комментария, внутри корневого
     * элемента комментария и извлекает его содержимое.
     * <p>
     * Переопределяет стандартный метод getText() для корректного извлечения
     * текста именно из контента комментария, а не из всего элемента.
     *
     * @return текст комментария
     * @throws NoSuchElementException если текстовый элемент не найден внутри комментария
     */
    @Override
    public String getText() {
        WebElement textElement = getElement().findElement(COMMENT_TEXT);
        return textElement.getText();
    }
}
