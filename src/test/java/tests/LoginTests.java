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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import pages.LoginPage;
import testdata.LinkData;
import testdata.LocalizationData;
import testdata.PhoneInputData;

import java.util.stream.Stream;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Owner("Oleg Dunyushkin")
@Epic("MAX")
@Feature("Вход в веб-версию")
@DisplayName("Тесты публичной формы входа MAX")
public class LoginTests extends TestBase {

    private final LoginPage loginPage = new LoginPage();

    @BeforeEach
    void openLoginPage() {
        loginPage.openPage();
    }

    @Test
    @Story("Вход по QR-коду")
    @Tag("smoke")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Форма входа по QR-коду отображается при открытии веб-версии")
    void qrLoginShouldBeDisplayed() {
        selectRussianLanguage()
                .shouldShowQrLogin();
    }

    @Test
    @Story("Переключение способа входа")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Пользователь может переключаться между способами входа")
    void userCanSwitchBetweenLoginMethods() {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .shouldShowPhoneLogin()
                .switchToQrLogin()
                .shouldShowQrLogin();
    }

    @MethodSource("phoneInputData")
    @ParameterizedTest(name = "{0}")
    @Story("Ввод номера телефона")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Поле телефона форматирует ввод и управляет состоянием кнопки Войти")
    void phoneInputShouldBeFormattedAndChangeSubmitState(PhoneInputData data) {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .setPhone(data.input())
                .shouldHavePhoneValue(data.formattedValue())
                .shouldHaveSubmitButtonState(data.submitEnabled());
    }

    @CsvSource({
            "Беларусь, +375",
            "Армения, +374",
            "Индия, +91",
            "Гренада, +1473"
    })
    @ParameterizedTest(name = "Для страны {0} используется код {1}")
    @Story("Выбор страны")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("При выборе страны изменяется телефонный код")
    void countrySelectionShouldChangePhoneCode(String countryName, String countryCode) {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .selectCountry(countryName)
                .shouldHaveCountryCode(countryCode);
    }

    @CsvSource({
            "Беларусь, Беларусь, +375",
            "бел, Беларусь, +375",
            "375, Беларусь, +375"
    })
    @ParameterizedTest(name = "Запрос «{0}» находит страну {1}")
    @Story("Поиск страны")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Страну можно найти по названию, части названия или телефонному коду")
    void countrySearchShouldShowExpectedResult(
            String query,
            String countryName,
            String countryCode
    ) {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .searchCountry(query)
                .shouldHaveCountryResult(countryName, countryCode);
    }

    @Test
    @Story("Поиск страны")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Поиск неизвестной страны не показывает результатов")
    void unknownCountrySearchShouldShowNoResults() {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .searchCountry("Несуществующая страна")
                .shouldHaveNoCountryResults();
    }

    @MethodSource("localizations")
    @ParameterizedTest(name = "Интерфейс отображается на языке: {0}")
    @Story("Локализация")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Форма входа поддерживает все доступные языки")
    void languageSwitchShouldChangeInterface(LocalizationData localization) {
        loginPage.shouldBeLoaded()
                .selectLanguage(localization.language())
                .shouldHaveLocalizedContent(
                        localization.languageButtonLabel(),
                        localization.title(),
                        localization.description(),
                        localization.phoneLoginButton(),
                        localization.helpLink()
                );
    }

    @MethodSource("legalLinks")
    @ParameterizedTest(name = "Ссылка «{0}» имеет корректный адрес")
    @Story("Юридическая информация")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Форма входа содержит корректные юридические ссылки")
    void phoneLoginShouldHaveLegalLinks(LinkData link) {
        selectRussianLanguage()
                .switchToPhoneLogin()
                .shouldHaveLegalLink(link.name(), link.url());
    }

    private LoginPage selectRussianLanguage() {
        return loginPage.shouldBeLoaded()
                .selectLanguage("Русский");
    }

    static Stream<PhoneInputData> phoneInputData() {
        return Stream.of(
                new PhoneInputData("Пустой номер: кнопка выключена", "", "", false),
                new PhoneInputData("Одна цифра: кнопка выключена", "1", "1", false),
                new PhoneInputData(
                        "Девять цифр: кнопка выключена",
                        "999123456",
                        "999 123 45 6",
                        false
                ),
                new PhoneInputData(
                        "Десять цифр: кнопка включена",
                        "9991234567",
                        "999 123 45 67",
                        true
                ),
                new PhoneInputData(
                        "Буквы и спецсимволы удаляются",
                        "abc9991234567!?",
                        "999 123 45 67",
                        true
                ),
                new PhoneInputData(
                        "Лишние цифры не вводятся",
                        "999123456789",
                        "999 123 45 67",
                        true
                )
        );
    }

    static Stream<LocalizationData> localizations() {
        return Stream.of(
                new LocalizationData(
                        "Русский",
                        "Изменить язык",
                        "Войдите в MAX по QR-коду",
                        "Наведите камеру на QR-код, чтобы войти в профиль или скачать приложение",
                        "Войти по номеру телефона",
                        "Помощь"
                ),
                new LocalizationData(
                        "English",
                        "Change language",
                        "Sign in to MAX via QR code",
                        "Scan the QR code to sign in to your profile or to download the app",
                        "Sign in with phone number",
                        "Help"
                ),
                new LocalizationData(
                        "Español",
                        "Cambiar de idioma",
                        "Inicia sesión en MAX vía el código QR",
                        "Apunta la cámara al código QR para iniciar sesión en tu perfil o descargar la aplicación",
                        "Acceder con número de teléfono",
                        "Ayuda"
                ),
                new LocalizationData(
                        "Português (Brasil)",
                        "Alterar idioma",
                        "Entre no MAX via QR code",
                        "Aponte a câmera para o QR code para entrar no perfil ou baixar o aplicativo",
                        "Entrar por número de telefone",
                        "Ajuda"
                )
        );
    }

    static Stream<LinkData> legalLinks() {
        return Stream.of(
                new LinkData("политику конфиденциальности", "https://legal.max.ru/pp"),
                new LinkData("пользовательское соглашение", "https://legal.max.ru/ps")
        );
    }
}
