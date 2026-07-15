package tests;

import config.TestBase;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.MaxLandingPage;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Owner("Oleg Dunyushkin")
@Feature("MAX")
@Story("Главная страница")
@DisplayName("Тесты главной страницы MAX")
public class MaxLandingTests extends TestBase {

    private final MaxLandingPage landingPage = new MaxLandingPage();

    @Test
    @Tag("smoke")
    @Severity(CRITICAL)
    @DisplayName("Главная страница содержит информацию о возможностях MAX")
    void mainPageShouldContainProductInformation() {
        landingPage.openPage()
                .shouldShowProductInformation();
    }

    @Test
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Ссылки на главной странице имеют корректные адреса")
    void landingPageLinksShouldHaveCorrectUrls() {
        landingPage.openPage()
                .shouldHaveApplicationStoreLink("App Store")
                .shouldHaveApplicationStoreLink("Google Play")
                .shouldHaveApplicationStoreLink("RuStore")
                .shouldHaveApplicationStoreLink("AppGallery")
                .shouldHaveLink("Для разработчиков", "https://dev.max.ru/docs")
                .shouldHaveLink("Ответы на вопросы", "https://help.max.ru/")
                .shouldHaveLink("Политика конфиденциальности", "https://legal.max.ru/pp")
                .shouldHaveLink("Пользовательское соглашение", "https://legal.max.ru/ps");
    }
}
