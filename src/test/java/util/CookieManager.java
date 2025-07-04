package util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

/**
 * Утилитный класс для сохранения и загрузки cookies.
 */
public class CookieManager {

    private static final String COOKIE_FILE = "cookies.data";

    /**
     * Сохраняет все cookies текущей сессии в файл.
     *
     * @param driver WebDriver с активной сессией
     */
    public static void saveCookies(WebDriver driver) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COOKIE_FILE))) {
            oos.writeObject(driver.manage().getCookies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Загружает cookies из файла и добавляет их в текущую сессию.
     *
     * @param driver WebDriver для добавления cookies
     */
    @SuppressWarnings("unchecked")
    public static void loadCookies(WebDriver driver) {
        File file = new File(COOKIE_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Set<Cookie> cookies = (Set<Cookie>) ois.readObject();
            driver.get("https://www.reddit.com"); // важно: открыть домен перед добавлением cookies
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
            driver.navigate().refresh(); // обновить страницу с куками
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

