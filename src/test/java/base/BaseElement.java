    package base;

    import org.openqa.selenium.*;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.time.Duration;

    /**
     * Базовый класс для всех элементов на странице.
     */
    public abstract class BaseElement {

        protected final WebDriver driver;
        protected final WebDriverWait wait;
        protected final By locator;
        protected final WebElement element;

        private static final Duration TIMEOUT = Duration.ofSeconds(10);

        /**
         * Конструктор через локатор.
         */
        public BaseElement(WebDriver driver, By locator) {
            this.driver = driver;
            this.locator = locator;
            this.element = null;
            this.wait = new WebDriverWait(driver, TIMEOUT);
        }

        /**
         * Конструктор через WebElement.
         */
        public BaseElement(WebDriver driver, WebElement element) {
            this.driver = driver;
            this.element = element;
            this.locator = null;
            this.wait = new WebDriverWait(driver, TIMEOUT);
        }

        /**
         * Возвращает WebElement, либо из кэша, либо через ожидание по локатору.
         */
        public WebElement getElement() {
            if (element != null) {
                return element;
            }
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        }

        public void click() {
            if (locator != null) {
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            } else  {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            }
        }

        /**
         * Кликает по элементу через JavaScript.
         */
        public void jsClick() {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            if (element != null) {
                js.executeScript("arguments[0].click();", element);
            } else {
                js.executeScript("arguments[0].click();", driver.findElement(locator));
            }
        }

        public boolean isVisible() {
            try {
                return getElement().isDisplayed();
            } catch (NoSuchElementException | TimeoutException e) {
                return false;
            }
        }

        public String getText() {
            return getElement().getText();
        }

        public void scrollToElement() {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block: 'center'});", getElement());
        }

        public void sendKeys(String text) {
            WebElement temp_element = getElement();
            temp_element.sendKeys(text);
        }
    }
