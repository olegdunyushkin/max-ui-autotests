package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverConditions.title;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;
import static helpers.PageAvailability.shouldNotBeRateLimited;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaxLandingPage {

    private static final List<String> BENEFIT_TITLES = List.of(
            "Общайтесь без ограничений",
            "Звоните в высоком качестве",
            "Читайте любимых авторов",
            "Будьте в безопасности",
            "Пользуйтесь уникальными сервисами"
    );

    private final SelenideElement pageTitle = $("h1");

    @Step("Открываем главную страницу MAX")
    public MaxLandingPage openPage() {
        open("/");
        return this;
    }

    @Step("Проверяем загрузку главной страницы MAX")
    public MaxLandingPage shouldBeLoaded() {
        shouldNotBeRateLimited();
        $("main").shouldBe(visible);
        return this;
    }

    @Step("Проверяем основную информацию о возможностях MAX")
    public MaxLandingPage shouldShowProductInformation() {
        pageTitle
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

    @Step("Проверяем метаданные и семантическую структуру страницы")
    public MaxLandingPage shouldHaveMetadataAndSemanticStructure() {
        webdriver().shouldHave(title(
                "MAX – быстрое и легкое приложение для общения и решения повседневных задач"
        ));
        $("html").shouldHave(attribute("lang", "ru"));
        $("meta[name='description']")
                .shouldHave(attribute(
                        "content",
                        "MAX позволяет отправлять любые виды сообщений и звонить даже на слабых "
                                + "устройствах и при низкой скорости интернета."
                ));
        $$("h1").shouldHave(size(1));
        $("header").should(exist);
        $("main").should(exist);
        $("footer").should(exist);
        return this;
    }

    @Step("Проверяем ссылку {linkText}")
    public MaxLandingPage shouldHaveLink(String linkText, String expectedUrl) {
        $$("a")
                .filterBy(exactText(linkText))
                .filterBy(attribute("href", expectedUrl))
                .findBy(visible)
                .shouldBe(visible);
        return this;
    }

    @Step("Устанавливаем размер окна {width}x{height}")
    public MaxLandingPage setViewport(int width, int height) {
        getWebDriver().manage().window().setSize(new Dimension(width, height));
        return this;
    }

    @Step("Проверяем, что основная информация видна и страница помещается по ширине")
    public MaxLandingPage shouldFitViewport() {
        pageTitle.shouldBe(visible);

        Long viewportWidth = executeJavaScript("return window.innerWidth");
        Long pageWidth = executeJavaScript("return document.documentElement.scrollWidth");

        assertTrue(
                pageWidth <= viewportWidth,
                "Страница шире окна браузера: pageWidth=" + pageWidth + ", viewportWidth=" + viewportWidth
        );
        return this;
    }
}
