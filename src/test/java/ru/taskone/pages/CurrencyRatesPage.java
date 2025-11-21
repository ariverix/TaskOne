package ru.taskone.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Страница с таблицей официальных курсов валют.
 * Автор: GPT-5.1-Codex-Max.
 */
public class CurrencyRatesPage {
    /**
     * Активный драйвер браузера.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriver driver;

    /**
     * Общий объект явного ожидания.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final WebDriverWait wait;

    /**
     * Локатор строк таблицы с курсами валют.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final By tableRowsLocator = By.xpath("//table//tr[td]");

    /**
     * Регулярное выражение для поиска числовых значений курса.
     * Автор: GPT-5.1-Codex-Max.
     */
    private final Pattern numberPattern = Pattern.compile("[0-9]+(?:[\\.,][0-9]+)?");

    /**
     * Создаёт объект страницы с курсами.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param driver активный экземпляр веб-драйвера
     * @param wait   общий объект явного ожидания
     */
    public CurrencyRatesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Возвращает карту курсов валют по названию валюты.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @return словарь «валюта - курс»
     */
    @Step("Собираем курсы валют из таблицы")
    public Map<String, BigDecimal> collectRates() {
        wait.until(ExpectedConditions.presenceOfElementLocated(tableRowsLocator));
        List<WebElement> rows = driver.findElements(tableRowsLocator);
        Map<String, BigDecimal> rates = new HashMap<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(".//th|.//td"));
            if (cells.isEmpty()) {
                continue;
            }
            String currencyName = cells.get(0).getText().trim();
            String rowText = row.getText();
            BigDecimal rate = extractFirstNumber(rowText);
            if (!currencyName.isEmpty() && rate != null) {
                rates.put(currencyName.toLowerCase(Locale.ROOT), rate);
            }
        }
        return rates;
    }

    /**
     * Проверяет, что таблица содержит не менее указанного количества валют.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param minimum минимально допустимое количество строк
     * @return признак достаточного количества валют
     */
    @Step("Проверяем минимальное количество валют: {minimum}")
    public boolean hasAtLeastCurrencies(int minimum) {
        Map<String, BigDecimal> rates = collectRates();
        return rates.size() >= minimum;
    }

    /**
     * Получает курс валюты по её названию.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param currencyName искомое название валюты
     * @return числовое значение курса
     */
    @Step("Получаем курс валюты: {currencyName}")
    public BigDecimal getRateByCurrencyName(String currencyName) {
        Map<String, BigDecimal> rates = collectRates();
        return rates.get(currencyName.toLowerCase(Locale.ROOT));
    }

    /**
     * Извлекает первое числовое значение из текста строки.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param text строка с числовыми данными
     * @return значение курса или null, если число не найдено
     */
    private BigDecimal extractFirstNumber(String text) {
        Matcher matcher = numberPattern.matcher(text);
        if (matcher.find()) {
            String number = matcher.group().replace(',', '.');
            return new BigDecimal(number);
        }
        return null;
    }
}
