package pages;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MaxLandingPage {

    private static final List<String> BENEFIT_TITLES = List.of(
            "Общайтесь без ограничений",
            "Звоните в высоком качестве",
            "Читайте любимых авторов",
            "Будьте в безопасности",
            "Пользуйтесь уникальными сервисами"
    );

    @Step("Открываем главную страницу MAX")
    public MaxLandingPage openPage() {
        open("/");
        return this;
    }

    @Step("Проверяем основную информацию о возможностях MAX")
    public MaxLandingPage shouldShowProductInformation() {
        $("h1")
                .shouldBe(visible)
                .shouldHave(text("Максимум возможностей для общения"));

        ElementsCollection benefitTitles = $$(".possibilities h3")
                .shouldHave(size(BENEFIT_TITLES.size()));

        for (int index = 0; index < BENEFIT_TITLES.size(); index++) {
            benefitTitles.get(index)
                    .scrollTo()
                    .shouldBe(visible)
                    .shouldHave(exactText(BENEFIT_TITLES.get(index)));
        }
        return this;
    }

    @Step("Проверяем ссылку {linkText} на магазин приложений")
    public MaxLandingPage shouldHaveApplicationStoreLink(String linkText) {
        $$("a")
                .findBy(exactText(linkText))
                .shouldBe(visible)
                .shouldHave(attributeMatching("href", "https://trk\\.mail\\.ru/c/.+"));
        return this;
    }

    @Step("Проверяем ссылку {linkText}")
    public MaxLandingPage shouldHaveLink(String linkText, String expectedUrl) {
        $$("a")
                .findBy(exactText(linkText))
                .shouldBe(visible)
                .shouldHave(attribute("href", expectedUrl));
        return this;
    }
}
