package tests;

import config.TestBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.DownloadPage;
import testdata.PlatformData;
import testdata.PlatformLinkData;

import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Owner("Oleg Dunyushkin")
@Epic("MAX")
@Feature("Загрузка приложения")
@DisplayName("Тесты страницы загрузки MAX")
public class DownloadTests extends TestBase {

    private final DownloadPage downloadPage = new DownloadPage();

    @BeforeEach
    void openDownloadPage() {
        downloadPage.openPage();
    }

    @Test
    @Story("Выбор платформы")
    @Tag("smoke")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Для Android отображается подходящий вариант MAX")
    void androidPlatformShouldHaveExpectedContent() {
        downloadPage.shouldBeLoaded()
                .selectPlatform("Android")
                .shouldShowPlatformContent(
                        "MAX для Android",
                        "Для устройств с Android 8.0 и выше"
                );
    }

    @MethodSource("additionalPlatforms")
    @ParameterizedTest(name = "{0}: отображается подходящий контент")
    @Story("Выбор платформы")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Для дополнительных платформ отображается подходящий вариант MAX")
    void platformShouldHaveExpectedContent(PlatformData platform) {
        downloadPage.shouldBeLoaded()
                .selectPlatform(platform.platform())
                .shouldShowPlatformContent(platform.title(), platform.expectedText());
    }

    @MethodSource("platformLinks")
    @ParameterizedTest(name = "{0}: ссылка ведёт на корректный адрес")
    @Story("Ссылки загрузки")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Ссылки загрузки соответствуют выбранной платформе")
    void platformDownloadLinkShouldHaveCorrectUrl(PlatformLinkData link) {
        downloadPage.shouldBeLoaded()
                .selectPlatform(link.platform())
                .shouldHaveDownloadLink(link.linkName(), link.urls());
    }

    @Test
    @Story("Ссылки загрузки")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Для Android доступен дополнительный канал загрузки")
    void androidShouldHaveAlternativeDownloadLink() {
        downloadPage.shouldBeLoaded()
                .selectPlatform("Android")
                .shouldHaveAlternativeAndroidDownloadLink();
    }

    static Stream<PlatformData> additionalPlatforms() {
        return Stream.of(
                new PlatformData(
                        "iPhone",
                        "Настройте уведомления в MAX на iPhone",
                        "Настроить"
                ),
                new PlatformData(
                        "Компьютер",
                        "MAX для компьютера",
                        "Для устройств с Windows 10 (1809) и выше"
                )
        );
    }

    static Stream<PlatformLinkData> platformLinks() {
        return Stream.of(
                new PlatformLinkData(
                        "Android",
                        "RuStore",
                        List.of(
                                "https://trk.mail.ru/c/idir06",
                                "https://www.rustore.ru/catalog/app/ru.oneme.app"
                        )
                ),
                new PlatformLinkData(
                        "Android",
                        "AppGallery",
                        List.of(
                                "https://trk.mail.ru/c/ns6e62",
                                "https://appgallery.huawei.com/#/app/C113469599"
                        )
                ),
                new PlatformLinkData("iPhone", "Настроить", "https://web.max.ru/"),
                new PlatformLinkData(
                        "Компьютер",
                        "Windows",
                        List.of(
                                "https://trk.mail.ru/c/zjjak0",
                                "https://trk.mail.ru/c/nm7zj0",
                                "https://download.max.ru/win/release/MAX.msi"
                        )
                ),
                new PlatformLinkData(
                        "Компьютер",
                        "macOS",
                        List.of(
                                "https://trk.mail.ru/c/la7zf18",
                                "https://download.max.ru/mac/release/MAX.dmg"
                        )
                ),
                new PlatformLinkData(
                        "Компьютер",
                        "Linux repos",
                        "https://download.max.ru/linux-repos"
                ),
                new PlatformLinkData("Компьютер", "Веб-версия", "https://web.max.ru/"),
                new PlatformLinkData("Компьютер", "Веб-версия (ГОСТ)", "https://pro.max.ru/"),
                new PlatformLinkData(
                        "Компьютер",
                        "MSI для организаций",
                        List.of(
                                "https://trk.mail.ru/c/nm7zj0",
                                "https://download.max.ru/win/release/MAX.msi"
                        )
                )
        );
    }
}
