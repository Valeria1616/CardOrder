package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankCardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() { WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldTestV1() /* Фамилия + пробел + имя */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }
    @Test
    public void shouldTestV2() /* Ошибка валидации в поле "Фамилия и имя". Ввод латиницы. */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualTextElement.isDisplayed());
   }

    @Test
    public void shouldTestV3() /* Ошибка валидации в поле "Фамилия и имя". Ввод кириллицы с числом. */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван5");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldTestV4() /* Ошибка валидации в поле "Фамилия и имя". Ввод кириллицы с символами. */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов? Иван!");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldTestV5() /* Пустое поле ввода имени и фамилии */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
   public void shouldTestV6() /* Телефон из нулей */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+00000000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldTestV7() /* Ввод номера без "+". */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79273334242");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldTestV8() /* Пустое поле ввода телефона. */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        var actualTextElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualTextElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualTextElement.isDisplayed());
    }

    @Test
    public void shouldTestV9() /* Отправление формы без галочки в чек-боксе */ {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79273334242");
        driver.findElement(By.className("button")).click();
        boolean agreement = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid")).isDisplayed();
        assertTrue(agreement);
    }
}