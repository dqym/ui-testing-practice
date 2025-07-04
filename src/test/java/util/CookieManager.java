package util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.util.Set;

/**
 * Утилитный класс для сохранения и загрузки cookies.
 */
public class CookieManager {

    private static final String COOKIE_DIR = "cookies";

    static {
        new File(COOKIE_DIR).mkdirs(); // создать директорию, если не существует
    }

    public static void saveCookies(WebDriver driver, String username) {
        File file = new File(COOKIE_DIR + File.separator + username + ".data");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(driver.manage().getCookies());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


