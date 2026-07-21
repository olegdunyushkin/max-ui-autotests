package config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attachments;
import helpers.EnvironmentAvailabilityCondition;
import helpers.RequestRateLimiter;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

@ExtendWith(EnvironmentAvailabilityCondition.class)
public class TestBase {

    @BeforeAll
    static void configureBrowser() {
        AllureEnvironment.create();

        Configuration.baseUrl = ProjectConfig.baseUrl();
        Configuration.browser = ProjectConfig.browser();
        Configuration.browserVersion = ProjectConfig.browserVersion();
        Configuration.browserSize = ProjectConfig.browserSize();
        Configuration.headless = ProjectConfig.headless();
        Configuration.screenshots = false;
        Configuration.pageLoadStrategy = ProjectConfig.pageLoadStrategy();
        Configuration.pageLoadTimeout = ProjectConfig.pageLoadTimeout();
        Configuration.timeout = ProjectConfig.timeout();

        if (ProjectConfig.isRemote()) {
            Configuration.remote = ProjectConfig.remoteUrl();

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true,
                    "enableLog", true,
                    "name", "MAX UI tests"
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }

    @BeforeEach
    void addAllureListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
        RequestRateLimiter.waitBeforeRequest();
    }

    @AfterEach
    void addAttachments() {
        try {
            if (WebDriverRunner.hasWebDriverStarted()) {
                try {
                    Attachments.screenshot("Скриншот после теста");
                    Attachments.pageSource();
                    Attachments.currentUrl();
                    Attachments.browserInformation();
                    Attachments.navigationTiming();
                    Attachments.browserConsoleLogs();

                    if (ProjectConfig.isRemote()) {
                        Attachments.video();
                    }
                } catch (RuntimeException exception) {
                    Attachments.text("Ошибка добавления вложений", String.valueOf(exception.getMessage()));
                }
            }
        } finally {
            closeWebDriver();
            SelenideLogger.removeListener("AllureSelenide");
        }
    }
}
