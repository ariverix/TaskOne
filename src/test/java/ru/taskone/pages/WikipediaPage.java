package ru.taskone.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Страница Википедии с возможностью поиска и проверки таблиц.
 * Автор: GPT-5.1-Codex-Max.
 */
public class WikipediaPage {
    /**
     * Активный драйвер браузера.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriver driver;

    /**
     * Объект явного ожидания для элементов страницы.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriverWait wait;

    /**
     * Локатор поля поиска Википедии.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final By wikiSearchField = By.id("searchInput");

    /**
     * Локатор строк таблицы "Преподаватели кафедры программирования".
     * Автор: GPT-5.1-Codex-Max.
     */
    private final By programmingLecturersRows = By.xpath(
            "//table[.//caption[contains(.,'Преподаватели кафедры программирования')]]//tr[td]");

    /**
     * Создаёт объект страницы Википедии.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param driver активный экземпляр веб-драйвера
     * @param wait   общий объект явного ожидания
     */
    public WikipediaPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Выполняет поиск по указанному запросу внутри Википедии.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param query текст запроса
     */
    @Step("Ищем в Википедии запрос: {query}")
    public void searchInsideWikipedia(String query) {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(wikiSearchField));
        searchField.click();
        searchField.clear();
        searchField.sendKeys(query);
        searchField.sendKeys(Keys.ENTER);
    }

    /**
     * Возвращает список имён из таблицы преподавателей кафедры программирования.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @return список ФИО преподавателей в порядке отображения таблицы
     */
    @Step("Получаем имена преподавателей из таблицы")
    public List<String> getProgrammingLecturers() {
        List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(programmingLecturersRows));
        return rows.stream()
                .map(row -> row.findElement(By.xpath(".//td[1]")))
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}
