package helpers;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
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

    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        return getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
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
        String videoUrl = System.getProperty(
                "videoUrl",
                "https://selenoid.autotests.cloud/video/"
        ) + sessionId() + ".mp4";

        return "<html><body><video width='100%' height='100%' controls autoplay>"
                + "<source src='" + videoUrl + "' type='video/mp4'></video></body></html>";
    }
}
