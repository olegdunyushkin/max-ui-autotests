package helpers;

import io.qameta.allure.Step;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.junit.jupiter.api.Assertions.fail;

public final class PageAvailability {

    private static final String RATE_LIMIT_PAGE_TITLE = "У вас большие запросы!";
    private static final AtomicBoolean ENVIRONMENT_UNAVAILABLE = new AtomicBoolean();

    private PageAvailability() {
    }

    public static boolean isUnavailable() {
        return ENVIRONMENT_UNAVAILABLE.get();
    }

    @Step("Проверяем доступность тестового окружения MAX")
    public static void shouldNotBeRateLimited() {
        if (RATE_LIMIT_PAGE_TITLE.equals(getWebDriver().getTitle())) {
            ENVIRONMENT_UNAVAILABLE.set(true);
            fail(
                    "MAX ограничил доступ из-за большого количества запросов: "
                            + getWebDriver().getCurrentUrl()
            );
        }
    }
}
