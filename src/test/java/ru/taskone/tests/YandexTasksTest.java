package ru.taskone.tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.taskone.base.BaseTest;
import ru.taskone.pages.CurrencyRatesPage;
import ru.taskone.pages.WikipediaPage;
import ru.taskone.pages.YandexMainPage;
import ru.taskone.pages.YandexSearchResultsPage;

import java.math.BigDecimal;
import java.util.List;

/**
 * Набор автоматизированных сценариев для заданий 1.1-1.3.
 * Автор: GPT-5.1-Codex-Max.
 */
public class YandexTasksTest extends BaseTest {

    /**
     * Проверяет наличие ссылки на Ozon при поиске цветка "Гладиолус".
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param query          поисковый запрос
     * @param expectedDomain ожидаемая подстрока домена в результатах
     */
    @Feature("Задание 1.1")
    @Story("Проверка результатов поиска Яндекс")
    @DisplayName("Поиск Гладиолуса и нахождение Ozon")
    @ParameterizedTest(name = "Запрос: {0}, ожидаем: {1}")
    @CsvSource({"Гладиолус,ozon"})
    public void testFindGladiolusWithOzon(String query, String expectedDomain) {
        YandexMainPage mainPage = new YandexMainPage(driver, wait);
        mainPage.open();
        YandexSearchResultsPage resultsPage = mainPage.search(query);

        Assertions.assertTrue(resultsPage.hasLinkWithHrefPart(expectedDomain),
                "На первой странице нет ссылки на домен " + expectedDomain);
    }

    /**
     * Проверяет наличие блока курсов валют и сравнивает значения для Лари и Азербайджанского маната.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param query         поисковый запрос
     * @param expectedTitle часть заголовка, по которой открывается результат
     */
    @Feature("Задание 1.2")
    @Story("Проверка официальных курсов валют")
    @DisplayName("Проверка курса Лари и Азербайджанского маната")
    @ParameterizedTest(name = "Запрос: {0}, заголовок: {1}")
    @CsvSource({"курсы валют,Официальные курсы валют"})
    public void testCurrencyRates(String query, String expectedTitle) {
        YandexMainPage mainPage = new YandexMainPage(driver, wait);
        mainPage.open();
        YandexSearchResultsPage resultsPage = mainPage.search(query);

        Assertions.assertTrue(resultsPage.hasResultWithTitle(expectedTitle),
                "Не найден результат с заголовком: " + expectedTitle);

        resultsPage.openResultByTitle(expectedTitle);

        CurrencyRatesPage ratesPage = new CurrencyRatesPage(driver, wait);
        Assertions.assertTrue(ratesPage.hasAtLeastCurrencies(3), "На странице должно быть минимум три валюты");

        BigDecimal lari = ratesPage.getRateByCurrencyName("Лари");
        BigDecimal manat = ratesPage.getRateByCurrencyName("Азербайджанский манат");

        Assertions.assertNotNull(lari, "Курс Лари не найден на странице");
        Assertions.assertNotNull(manat, "Курс Азербайджанского маната не найден на странице");
        Assertions.assertTrue(lari.compareTo(manat) < 0,
                "Курс Лари должен быть меньше Азербайджанского маната, получено: " + lari + " и " + manat);
    }

    /**
     * Проверяет корректность таблицы преподавателей на странице Википедии после повторного поиска.
     * Автор: GPT-5.1-Codex-Max.
     *
     * @param query            поисковый запрос для Яндекса
     * @param wikiResultTitle  заголовок результата для перехода на Википедию
     * @param wikiSearchQuery  запрос для поиска внутри Википедии
     * @param expectedFirst    ожидаемое имя в начале таблицы
     * @param expectedLast     ожидаемое имя в конце таблицы
     */
    @Feature("Задание 1.3")
    @Story("Проверка таблицы преподавателей на Википедии")
    @DisplayName("Проверка порядка преподавателей в таблице")
    @ParameterizedTest(name = "Запрос: {0}, результат: {1}")
    @CsvSource({"таблица википедия,Википедия:Таблицы,Таблица,Сергей Владимирович,Сергей Адамович"})
    public void testWikipediaLecturersTable(String query, String wikiResultTitle, String wikiSearchQuery,
                                            String expectedFirst, String expectedLast) {
        YandexMainPage mainPage = new YandexMainPage(driver, wait);
        mainPage.open();
        YandexSearchResultsPage resultsPage = mainPage.search(query);

        Assertions.assertTrue(resultsPage.hasResultWithTitle(wikiResultTitle),
                "Не найден результат с заголовком: " + wikiResultTitle);

        resultsPage.openResultByTitle(wikiResultTitle);

        WikipediaPage wikipediaPage = new WikipediaPage(driver, wait);
        wikipediaPage.searchInsideWikipedia(wikiSearchQuery);

        List<String> lecturers = wikipediaPage.getProgrammingLecturers();
        Assertions.assertFalse(lecturers.isEmpty(), "Таблица преподавателей не содержит данных");

        String first = lecturers.get(0);
        String last = lecturers.get(lecturers.size() - 1);

        Assertions.assertEquals(expectedFirst, first, "Ожидается другой первый преподаватель в таблице");
        Assertions.assertEquals(expectedLast, last, "Ожидается другой последний преподаватель в таблице");
    }
}
