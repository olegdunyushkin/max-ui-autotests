package tests;

import config.TestBase;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.LoginPage;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Owner("Oleg Dunyushkin")
@Feature("MAX")
@Story("Вход в веб-версию")
@DisplayName("Тесты публичной формы входа MAX")
public class LoginTests extends TestBase {

    private final LoginPage loginPage = new LoginPage();

    @Test
    @Tag("smoke")
    @Severity(CRITICAL)
    @DisplayName("Форма входа по QR-коду отображается при открытии веб-версии")
    void qrLoginShouldBeDisplayed() {
        loginPage.openPage()
                .shouldShowQrLogin();
    }

    @Test
    @Tag("smoke")
    @Severity(CRITICAL)
    @DisplayName("Пользователь может переключаться между способами входа")
    void userCanSwitchBetweenLoginMethods() {
        loginPage.openPage()
                .switchToPhoneLogin()
                .shouldShowPhoneLogin()
                .switchToQrLogin()
                .shouldShowQrLogin();
    }

    @CsvSource({
            "1, false",
            "999123456, false",
            "9991234567, true"
    })
    @ParameterizedTest(name = "Номер {0}: активность кнопки Войти — {1}")
    @Tag("regression")
    @Severity(CRITICAL)
    @DisplayName("Состояние кнопки Войти зависит от длины номера")
    void submitButtonStateShouldDependOnPhoneLength(String phone, boolean shouldBeEnabled) {
        loginPage.openPage()
                .switchToPhoneLogin()
                .setPhone(phone)
                .shouldHaveSubmitButtonState(shouldBeEnabled);
    }

    @CsvSource({
            "Беларусь, +375",
            "Армения, +374",
            "Индия, +91"
    })
    @ParameterizedTest(name = "Для страны {0} используется код {1}")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("При выборе страны изменяется телефонный код")
    void countrySelectionShouldChangePhoneCode(String countryName, String countryCode) {
        loginPage.openPage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .selectCountry(countryName)
                .shouldHaveCountryCode(countryCode);
    }

    @Test
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Поиск страны показывает подходящий результат")
    void countrySearchShouldShowExpectedResult() {
        loginPage.openPage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .searchCountry("Беларусь")
                .shouldHaveCountryResult("Беларусь", "+375");
    }

    @Test
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Поиск неизвестной страны не показывает результатов")
    void unknownCountrySearchShouldShowNoResults() {
        loginPage.openPage()
                .switchToPhoneLogin()
                .openCountryDialog()
                .searchCountry("Несуществующая страна")
                .shouldHaveNoCountryResults();
    }

    @CsvSource({
            "Русский, Войдите в MAX по QR-коду, Войти по номеру телефона",
            "English, Sign in to MAX via QR code, Sign in with phone number"
    })
    @ParameterizedTest(name = "Для языка {0} интерфейс отображается корректно")
    @Tag("regression")
    @Severity(NORMAL)
    @DisplayName("Переключение языка изменяет интерфейс формы входа")
    void languageSwitchShouldChangeInterface(
            String language,
            String expectedTitle,
            String expectedButton
    ) {
        loginPage.openPage()
                .switchLanguage(language)
                .shouldHaveLocalizedContent(expectedTitle, expectedButton);
    }
}
