package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement russianLanguageButton = $("button[aria-label='Изменить язык']");
    private final SelenideElement anyLanguageButton = $(
            "button[aria-label='Изменить язык'], "
                    + "button[aria-label='Change language'], "
                    + "button[aria-label='Cambiar idioma']"
    );
    private final SelenideElement pageTitle = $("h3");
    private final SelenideElement phoneInput = $("input[type='text']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement countryDialog = $("[role='dialog']");
    private final SelenideElement countrySearchInput = $("input[placeholder='Найти страну']");

    @Step("Открываем веб-версию MAX на русском языке")
    public LoginPage openPage() {
        open(System.getProperty("webUrl", "https://web.max.ru"));
        anyLanguageButton.shouldBe(visible);

        if (!russianLanguageButton.exists()) {
            anyLanguageButton.click();
            $$("[role='menuitem']")
                    .findBy(exactText("Русский"))
                    .shouldBe(visible)
                    .click();
        }

        russianLanguageButton.shouldBe(visible);
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
                .shouldHave(attribute("href", "https://help.max.ru/"));
        return this;
    }

    @Step("Переходим к входу по номеру телефона")
    public LoginPage switchToPhoneLogin() {
        $$("button")
                .findBy(exactText("Войти по номеру телефона"))
                .shouldBe(visible)
                .click();
        pageTitle.shouldHave(exactText("С каким номером телефона хотите войти?"));
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
        countryDialog.shouldBe(visible);
        return this;
    }

    @Step("Выбираем страну {countryName} с кодом {countryCode}")
    public LoginPage selectCountry(String countryName, String countryCode) {
        countryDialog.$$("button")
                .findBy(text(countryName))
                .shouldHave(text(countryCode))
                .click();
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

    @Step("Переключаем язык интерфейса на {language}")
    public LoginPage switchLanguage(String language) {
        russianLanguageButton.shouldBe(visible).click();
        $$("[role='menuitem']")
                .findBy(exactText(language))
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Проверяем переведённый заголовок {expectedTitle} и кнопку {expectedButton}")
    public LoginPage shouldHaveLocalizedContent(String expectedTitle, String expectedButton) {
        pageTitle.shouldBe(visible).shouldHave(exactText(expectedTitle));
        $$("button")
                .findBy(exactText(expectedButton))
                .shouldBe(visible);
        return this;
    }
}
