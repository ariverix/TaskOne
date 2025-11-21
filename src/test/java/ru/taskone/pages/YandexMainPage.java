package ru.taskone.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Главная страница поисковой системы Яндекс.
 * Автор: GPT-5.1-Codex-Max.
 */
public class YandexMainPage {
    /**
     * Драйвер для работы со страницей.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriver driver;

    /**
     * Явное ожидание для элементов страницы.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriverWait wait;

    /**
     * Поле ввода поискового запроса.
     * Автор: GPT-5.1-Codex-Max.
     */
    @FindBy(how = How.NAME, using = "text")
    private WebElement searchInput;

    /**
     * Кнопка отправки поискового запроса.
     * Автор: GPT-5.1-Codex-Max.
     */
    @FindBy(how = How.XPATH, using = "//button[@type='submit' and @aria-label='Найти']")
    private WebElement searchButton;

    /**
     * Создаёт объект страницы и инициализирует элементы.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param driver активный экземпляр веб-драйвера
     * @param wait   общий объект явного ожидания
     */
    public YandexMainPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    /**
     * Открывает главную страницу Яндекса.
     * Автор: GPT-5.1-Codex-Max.
     */
    @Step("Открываем главную страницу Яндекса")
    public void open() {
        driver.get("https://ya.ru/");
    }

    /**
     * Выполняет поиск по указанной строке запроса.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param query текст поискового запроса
     * @return страницу с результатами поиска
     */
    @Step("Выполняем поиск по запросу: {query}")
    public YandexSearchResultsPage search(String query) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.click();
        searchInput.clear();
        searchInput.sendKeys(query);
        searchInput.sendKeys(Keys.ENTER);
        return new YandexSearchResultsPage(driver, wait);
    }
}
