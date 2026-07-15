package tests;

import config.TestBase;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.DownloadPage;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@Owner("Oleg Dunyushkin")
@Feature("MAX")
@Story("Загрузка приложения")
@DisplayName("Тесты страницы загрузки MAX")
public class DownloadTests extends TestBase {

    private final DownloadPage downloadPage = new DownloadPage();

    @CsvSource({
            "Android, MAX для Android, Google Play",
            "iPhone, Настройте уведомления в MAX на iPhone, Настроить",
            "Компьютер, MAX для компьютера, Windows"
    })
    @ParameterizedTest(name = "{0}: отображается раздел {1}")
    @Tag("smoke")
    @Severity(CRITICAL)
    @DisplayName("Для каждой платформы отображается подходящий вариант MAX")
    void platformShouldHaveDownloadOption(
            String platform,
            String expectedTitle,
            String expectedLink
    ) {
        downloadPage.openPage()
                .selectPlatform(platform)
                .shouldShowPlatformContent(expectedTitle, expectedLink);
    }
}
