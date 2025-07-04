package util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

/**
 * Утилитный класс для сохранения и загрузки cookies браузера.
 * <p>
 * Данный класс предоставляет функциональность для сериализации и десериализации
 * объектов Cookie из Selenium WebDriver. Это позволяет сохранять состояние 
 * авторизации пользователя между запусками тестов, что повышает эффективность
 * тестирования, устраняя необходимость повторной авторизации.
 * <p>
 * Cookies сохраняются в виде сериализованных объектов в директории 'cookies',
 * которая создается автоматически при первом использовании класса.
 */
public class CookieManager {

    /**
     * Путь к директории, где будут храниться файлы с сохраненными cookies.
     */
    private static final String COOKIE_DIR = "cookies";

    /**
     * Статический блок инициализации.
     * Создает директорию для хранения cookies, если она не существует.
     */
    static {
        new File(COOKIE_DIR).mkdirs();
    }

    /**
     * Сохраняет cookies текущей сессии браузера в файл.
     * <p>
     * Метод сериализует все cookies из WebDriver и сохраняет их в файл,
     * который именуется в соответствии с указанным именем пользователя.
     *
     * @param driver   экземпляр WebDriver, из которого извлекаются cookies
     * @param username имя пользователя, используемое для формирования имени файла
     *                 с сохраненными cookies
     */
    public static void saveCookies(WebDriver driver, String username) {
        File file = new File(COOKIE_DIR + File.separator + username + ".data");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(driver.manage().getCookies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает cookies из файла и добавляет их в текущую сессию браузера.
     * <p>
     * Метод десериализует cookies из файла, соответствующего указанному имени пользователя,
     * и добавляет их в текущую сессию WebDriver. Перед добавлением cookies, браузер
     * обязательно открывает страницу домена, для которого эти cookies предназначены.
     * <p>
     * Если файл с cookies для указанного пользователя не существует, метод завершает выполнение
     * без каких-либо действий.
     *
     * @param driver   экземпляр WebDriver, в который будут загружены cookies
     * @param username имя пользователя, соответствующее файлу с сохраненными cookies
     */
    public static void loadCookies(WebDriver driver, String username) {
        File file = new File(COOKIE_DIR + File.separator + username + ".data");
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Set<Cookie> cookies = (Set<Cookie>) ois.readObject();
            driver.get("https://www.reddit.com"); // обязательно зайти на домен
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
            driver.navigate().refresh();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


