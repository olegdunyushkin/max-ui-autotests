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
import pages.MaxLandingPage;
import testdata.LinkData;
import testdata.ViewportData;

import java.util.stream.Stream;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Owner("Oleg Dunyushkin")
@Epic("MAX")
@Feature("Главная страница")
@DisplayName("Тесты главной страницы MAX")
public class MaxLandingTests extends TestBase {

    private final MaxLandingPage landingPage = new MaxLandingPage();

    @BeforeEach
    void openLandingPage() {
        landingPage.openPage();
    }

    @Test
    @Story("Основной контент")
    @Tag("smoke")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Главная страница содержит информацию о возможностях MAX")
    void mainPageShouldContainProductInformation() {
        landingPage.shouldBeLoaded()
                .shouldShowProductInformation();
    }

    @Test
    @Story("Метаданные и семантика")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Главная страница содержит корректные метаданные и семантические блоки")
    void mainPageShouldHaveMetadataAndSemanticStructure() {
        landingPage.shouldBeLoaded()
                .shouldHaveMetadataAndSemanticStructure();
    }

    @MethodSource("landingPageLinks")
    @ParameterizedTest(name = "Ссылка «{0}» имеет корректный адрес")
    @Story("Навигация")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Навигационные ссылки ведут на корректные адреса")
    void landingPageLinkShouldHaveCorrectUrl(LinkData link) {
        landingPage.shouldBeLoaded()
                .shouldHaveLink(link.name(), link.url());
    }

    @MethodSource("viewports")
    @ParameterizedTest(name = "{0}: контент помещается по ширине")
    @Story("Адаптивная вёрстка")
    @Tag("regression")
    @Tag("responsive")
    @Severity(NORMAL)
    @DisplayName("Главная страница корректно отображается на разных разрешениях")
    void mainPageShouldFitViewport(ViewportData viewport) {
        landingPage.shouldBeLoaded()
                .setViewport(viewport.width(), viewport.height())
                .shouldFitViewport();
    }

    static Stream<LinkData> landingPageLinks() {
        return Stream.of(
                new LinkData("Для бизнеса", "https://business.max.ru/"),
                new LinkData("Скачать приложение", "https://download.max.ru/#desktop"),
                new LinkData(
                        "RuStore",
                        "https://trk.mail.ru/c/idir06"
                ),
                new LinkData(
                        "AppGallery",
                        "https://trk.mail.ru/c/ns6e62"
                ),
                new LinkData("Для компьютера", "https://download.max.ru#desktop"),
                new LinkData("Открыть веб-версию", "https://web.max.ru"),
                new LinkData("Цифровой ID", "https://go.max.ru/digitalId"),
                new LinkData("Для разработчиков", "https://dev.max.ru/docs"),
                new LinkData("Ответы на вопросы", "https://help.max.ru/"),
                new LinkData(
                        "Техническая документация",
                        "https://st.max.ru/docs/tekhnicheskaya-dokumentatsiya.zip"
                ),
                new LinkData("Брендбук MAX", "https://go.max.ru/brandbook"),
                new LinkData("Политика конфиденциальности", "https://legal.max.ru/pp"),
                new LinkData("Пользовательское соглашение", "https://legal.max.ru/ps"),
                new LinkData("Карьера", "https://team.vk.company/career-max/")
        );
    }

    static Stream<ViewportData> viewports() {
        return Stream.of(
                new ViewportData("Мобильный телефон", 375, 812),
                new ViewportData("Планшет", 768, 1024),
                new ViewportData("Ноутбук", 1440, 900)
        );
    }
}
