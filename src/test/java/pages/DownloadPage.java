package pages;

import config.ProjectConfig;
import io.qameta.allure.Step;

import java.util.List;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static helpers.PageAvailability.shouldNotBeRateLimited;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DownloadPage {

    @Step("Открываем страницу загрузки MAX")
    public DownloadPage openPage() {
        open(ProjectConfig.downloadUrl());
        return this;
    }

    @Step("Проверяем загрузку страницы скачивания MAX")
    public DownloadPage shouldBeLoaded() {
        shouldNotBeRateLimited();
        $("main").shouldBe(visible);
        return this;
    }

    @Step("Выбираем платформу {platform}")
    public DownloadPage selectPlatform(String platform) {
        $$("button")
                .findBy(exactText(platform))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Проверяем контент платформы: {expectedTitle}")
    public DownloadPage shouldShowPlatformContent(String expectedTitle, String expectedText) {
        $("h1")
                .shouldBe(visible)
                .shouldHave(exactText(expectedTitle));
        $("main").shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверяем ссылку загрузки {linkName}")
    public DownloadPage shouldHaveDownloadLink(String linkName, List<String> expectedUrls) {
        $("main").$$("a")
                .findBy(exactText(linkName))
                .shouldHave(attributeMatching("href", expectedUrlRegex(expectedUrls)))
                .shouldBe(visible);
        return this;
    }

    @Step("Проверяем дополнительный канал загрузки Android")
    public DownloadPage shouldHaveAlternativeAndroidDownloadLink() {
        boolean hasGooglePlay = hasVisibleLink(
                "Google Play",
                "https://play.google.com/store/apps/details?id=ru.oneme.app"
        );
        boolean hasGostVersion = hasVisibleLink(
                "MAX (ГОСТ)",
                "https://trk.mail.ru/c/bfy4n8"
        );

        assertTrue(
                hasGooglePlay || hasGostVersion,
                "Не найдена корректная ссылка Google Play или MAX (ГОСТ)"
        );
        return this;
    }

    private boolean hasVisibleLink(String linkName, String expectedUrl) {
        var link = $("main").$$("a").findBy(exactText(linkName));
        return link.exists()
                && link.isDisplayed()
                && expectedUrl.equals(link.getAttribute("href"));
    }

    private String expectedUrlRegex(List<String> expectedUrls) {
        return expectedUrls.stream()
                .map(Pattern::quote)
                .collect(joining("|", "(?:", ")"));
    }
}
