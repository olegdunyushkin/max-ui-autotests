package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DownloadPage {

    @Step("Открываем страницу загрузки MAX")
    public DownloadPage openPage() {
        open("https://download.max.ru/");
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

    @Step("Проверяем заголовок {expectedTitle} и ссылку {expectedLink}")
    public DownloadPage shouldShowPlatformContent(String expectedTitle, String expectedLink) {
        $("h1")
                .shouldBe(visible)
                .shouldHave(exactText(expectedTitle));
        $$("a")
                .findBy(exactText(expectedLink))
                .shouldBe(visible);
        return this;
    }
}
