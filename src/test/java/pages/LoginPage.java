package pages;

import com.codeborne.selenide.SelenideElement;
import config.ProjectConfig;
import io.qameta.allure.Step;

import java.util.Map;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static helpers.PageAvailability.shouldNotBeRateLimited;

public class LoginPage {

    private static final Map<String, String> LANGUAGE_BUTTON_LABELS = Map.of(
            "Русский", "Изменить язык",
            "English", "Change language",
            "Español", "Cambiar de idioma",
            "Português (Brasil)", "Alterar idioma"
    );

    private final SelenideElement languageButton = $(
            "button[aria-label='Изменить язык'], "
                    + "button[aria-label='Change language'], "
                    + "button[aria-label='Cambiar de idioma'], "
                    + "button[aria-label='Alterar idioma']"
    );
    private final SelenideElement pageTitle = $("h3");
    private final SelenideElement phoneInput = $("input[type='text']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement countryDialog = $("[role='dialog']");
    private final SelenideElement countrySearchInput = $("input[placeholder='Найти страну']");

    @Step("Открываем веб-версию MAX")
    public LoginPage openPage() {
        open(ProjectConfig.webUrl());
        return this;
    }

    @Step("Проверяем загрузку формы входа MAX")
    public LoginPage shouldBeLoaded() {
        shouldNotBeRateLimited();
        $("#boot-loader").shouldBe(hidden);
        languageButton.shouldBe(visible);
        return this;
    }

    @Step("Выбираем язык интерфейса {language}")
    public LoginPage selectLanguage(String language) {
        String expectedButtonLabel = LANGUAGE_BUTTON_LABELS.get(language);
        if (expectedButtonLabel == null) {
            throw new IllegalArgumentException("Неизвестный язык: " + language);
        }

        languageButton.shouldBe(visible);
        if (!expectedButtonLabel.equals(languageButton.getAttribute("aria-label"))) {
            languageButton.click();
            $$("[role='menuitem']")
                    .findBy(exactText(language))
                    .shouldBe(visible)
                    .click();
        }
        return this;
    }

    @Step("Проверяем форму входа по QR-коду")
    public LoginPage shouldShowQrLogin() {
        pageTitle.shouldHave(exactText("Войдите в MAX по QR-коду"));
        $$("button")
                .findBy(exactText("Войти по номеру телефона"))
                .shouldBe(visible);
        $("a[aria-label='Помощь']")
                .shouldBe(visible)
                .shouldHave(attributeMatching("href", "https://help\\.max\\.ru/?"));
        return this;
    }

    @Step("Переходим к входу по номеру телефона")
    public LoginPage switchToPhoneLogin() {
        $$("button")
                .findBy(exactText("Войти по номеру телефона"))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Проверяем форму входа по номеру телефона")
    public LoginPage shouldShowPhoneLogin() {
        pageTitle.shouldHave(exactText("С каким номером телефона хотите войти?"));
        phoneInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Возвращаемся к входу по QR-коду")
    public LoginPage switchToQrLogin() {
        $$("button")
                .findBy(exactText("Войти по QR-коду"))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Вводим номер телефона {phone}")
    public LoginPage setPhone(String phone) {
        phoneInput.shouldBe(visible).setValue(phone);
        return this;
    }

    @Step("Проверяем значение поля телефона: {expectedPhone}")
    public LoginPage shouldHavePhoneValue(String expectedPhone) {
        phoneInput.shouldHave(attribute("value", expectedPhone));
        return this;
    }

    @Step("Проверяем состояние кнопки Войти: enabled = {shouldBeEnabled}")
    public LoginPage shouldHaveSubmitButtonState(boolean shouldBeEnabled) {
        if (shouldBeEnabled) {
            submitButton.shouldBe(enabled);
        } else {
            submitButton.shouldBe(disabled);
        }
        return this;
    }

    @Step("Открываем список стран")
    public LoginPage openCountryDialog() {
        $$("button")
                .findBy(text("+7"))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Выбираем страну {countryName}")
    public LoginPage selectCountry(String countryName) {
        countryDialog.$$("button")
                .findBy(text(countryName))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Проверяем телефонный код {countryCode}")
    public LoginPage shouldHaveCountryCode(String countryCode) {
        $$("button")
                .findBy(text(countryCode))
                .shouldBe(visible);
        return this;
    }

    @Step("Ищем страну по запросу {query}")
    public LoginPage searchCountry(String query) {
        countrySearchInput.shouldBe(visible).setValue(query);
        return this;
    }

    @Step("Проверяем страну {countryName} с кодом {countryCode} в результатах")
    public LoginPage shouldHaveCountryResult(String countryName, String countryCode) {
        countryDialog.$$("button")
                .findBy(text(countryName))
                .shouldBe(visible)
                .shouldHave(text(countryCode));
        return this;
    }

    @Step("Проверяем, что страны не найдены")
    public LoginPage shouldHaveNoCountryResults() {
        countryDialog.$(".emptySearch")
                .shouldBe(visible)
                .shouldHave(text("Мы уже работаем над расширением списка"));
        return this;
    }

    @Step("Проверяем локализованный интерфейс")
    public LoginPage shouldHaveLocalizedContent(
            String expectedLanguageButtonLabel,
            String expectedTitle,
            String expectedDescription,
            String expectedPhoneButton,
            String expectedHelpLink
    ) {
        languageButton.shouldHave(attribute("aria-label", expectedLanguageButtonLabel));
        pageTitle.shouldBe(visible).shouldHave(exactText(expectedTitle));
        $$("p")
                .findBy(exactText(expectedDescription))
                .shouldBe(visible);
        $$("button")
                .findBy(exactText(expectedPhoneButton))
                .shouldBe(visible);
        $("a[aria-label='" + expectedHelpLink + "']")
                .shouldHave(attributeMatching("href", "https://help\\.max\\.ru/?"))
                .shouldBe(visible);
        return this;
    }

    @Step("Проверяем ссылку {linkText}")
    public LoginPage shouldHaveLegalLink(String linkText, String expectedUrl) {
        $$("a")
                .findBy(exactText(linkText))
                .shouldHave(attribute("href", expectedUrl))
                .shouldBe(visible);
        return this;
    }
}
