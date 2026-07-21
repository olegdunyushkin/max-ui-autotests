package helpers;

import com.codeborne.selenide.Selenide;
import config.ProjectConfig;
import io.qameta.allure.Attachment;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.sessionId;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.logging.LogType.BROWSER;

public class Attachments {

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] screenshot(String name) {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/html", fileExtension = ".html")
    public static byte[] pageSource() {
        return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    @Attachment(value = "Current URL", type = "text/plain")
    public static String currentUrl() {
        return getWebDriver().getCurrentUrl();
    }

    @Attachment(value = "Browser information", type = "text/plain")
    public static String browserInformation() {
        if (!(getWebDriver() instanceof HasCapabilities driver)) {
            return "Capabilities недоступны";
        }

        Capabilities capabilities = driver.getCapabilities();
        return "Browser: " + capabilities.getBrowserName() + "\n"
                + "Version: " + capabilities.getBrowserVersion() + "\n"
                + "Platform: " + capabilities.getPlatformName() + "\n"
                + "Window: " + getWebDriver().manage().window().getSize();
    }

    @Attachment(value = "Navigation timing", type = "application/json", fileExtension = ".json")
    public static String navigationTiming() {
        if (!(getWebDriver() instanceof JavascriptExecutor executor)) {
            return "{}";
        }

        Object timing = executor.executeScript(
                "return JSON.stringify(performance.getEntriesByType('navigation')[0] || {})"
        );
        return String.valueOf(timing);
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String text(String name, String value) {
        return value;
    }

    public static void browserConsoleLogs() {
        try {
            text("Browser console logs", String.join("\n", Selenide.getWebDriverLogs(BROWSER)));
        } catch (Exception exception) {
            text("Browser console logs", "Логи браузера недоступны: " + exception.getMessage());
        }
    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public static String video() {
        String videoUrl = ProjectConfig.videoUrl() + sessionId() + ".mp4";

        return "<html><body><video width='100%' height='100%' controls autoplay>"
                + "<source src='" + videoUrl + "' type='video/mp4'></video></body></html>";
    }
}
