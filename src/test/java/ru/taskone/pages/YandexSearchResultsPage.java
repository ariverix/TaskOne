package ru.taskone.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * Страница с результатами поиска Яндекса.
 * Автор: GPT-5.1-Codex-Max.
 */
public class YandexSearchResultsPage {
    /**
     * Драйвер для работы со страницей результатов.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriver driver;

    /**
     * Явное ожидание элементов страницы.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriverWait wait;

    /**
     * Локатор любого результата поиска.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final By anyResultLocator = By.xpath("//a[contains(@href,'http')]");

    /**
     * Создаёт объект страницы результатов.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param driver активный экземпляр веб-драйвера
     * @param wait   общий объект явного ожидания
     */
    public YandexSearchResultsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Проверяет наличие ссылки с указанной подстрокой в href.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param hrefPart подстрока, которую нужно найти в адресе ссылки
     * @return признак наличия подходящей ссылки
     */
    @Step("Проверяем наличие ссылки, содержащей: {hrefPart}")
    public boolean hasLinkWithHrefPart(String hrefPart) {
        wait.until(ExpectedConditions.presenceOfElementLocated(anyResultLocator));
        String lowered = hrefPart.toLowerCase();
        return driver.findElements(anyResultLocator)
                .stream()
                .map(element -> element.getAttribute("href"))
                .filter(href -> href != null)
                .anyMatch(href -> href.toLowerCase().contains(lowered));
    }

    /**
     * Проверяет наличие заголовка результата, содержащего указанный текст.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param titlePart текст, который должен присутствовать в заголовке
     * @return признак наличия подходящего результата
     */
    @Step("Проверяем наличие результата с текстом: {titlePart}")
    public boolean hasResultWithTitle(String titlePart) {
        wait.until(ExpectedConditions.presenceOfElementLocated(anyResultLocator));
        return driver.findElements(anyResultLocator)
                .stream()
                .map(WebElement::getText)
                .anyMatch(text -> text != null && text.contains(titlePart));
    }

    /**
     * Открывает первую подходящую ссылку с указанным текстом в новом окне или вкладке.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param titlePart часть заголовка результата, по которой выбирается ссылка
     */
    @Step("Открываем результат с текстом: {titlePart}")
    public void openResultByTitle(String titlePart) {
        wait.until(ExpectedConditions.presenceOfElementLocated(anyResultLocator));
        WebElement target = driver.findElements(anyResultLocator)
                .stream()
                .filter(element -> element.getText() != null && element.getText().contains(titlePart))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Результат с текстом '" + titlePart + "' не найден"));

        String currentHandle = driver.getWindowHandle();
        Set<String> handlesBeforeClick = driver.getWindowHandles();
        target.click();

        wait.until(driverInstance -> driverInstance.getWindowHandles().size() > handlesBeforeClick.size());
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
