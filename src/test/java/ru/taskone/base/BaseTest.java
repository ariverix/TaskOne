package ru.taskone.base;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Базовый класс для инициализации веб-драйвера и ожиданий перед запуском тестов.
 * Автор: GPT-5.1-Codex-Max.
 */
public abstract class BaseTest {
    /**
     * Экземпляр веб-драйвера, используемый во всех тестах.
     * Автор: GPT-5.1-Codex-Max.
     */
    protected WebDriver driver;

    /**
     * Явное ожидание с базовым таймаутом.
     * Автор: GPT-5.1-Codex-Max.
     */
    protected WebDriverWait wait;

    /**
     * Базовое значение таймаута для явных ожиданий.
     * Автор: GPT-5.1-Codex-Max.
     */
    protected static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    /**
     * Подготавливает окружение и запускает браузер перед каждым тестом.
     * Автор: GPT-5.1-Codex-Max.
     */
    @BeforeEach
    @Step("Подготовка веб-драйвера")
    public void setUpDriver() {
        String driverPath = System.getenv("CHROME_DRIVER");
        if (driverPath == null || driverPath.isEmpty()) {
            throw new IllegalStateException("Переменная окружения CHROME_DRIVER не задана");
        }
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Завершает работу веб-драйвера после каждого теста.
     * Автор: GPT-5.1-Codex-Max.
     */
    @AfterEach
    @Step("Закрытие веб-драйвера")
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
