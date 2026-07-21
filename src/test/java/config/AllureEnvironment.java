package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public final class AllureEnvironment {

    private static final AtomicBoolean CREATED = new AtomicBoolean();

    private AllureEnvironment() {
    }

    public static void create() {
        if (!CREATED.compareAndSet(false, true)) {
            return;
        }

        Path resultsDirectory = Path.of(ProjectConfig.allureResultsDirectory());

        try {
            Files.createDirectories(resultsDirectory);
            writeEnvironment(resultsDirectory);
            copyCategories(resultsDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("Не удалось подготовить данные для Allure", exception);
        }
    }

    private static void writeEnvironment(Path resultsDirectory) throws IOException {
        Properties environment = new Properties();
        environment.setProperty("Base URL", ProjectConfig.baseUrl());
        environment.setProperty("Web URL", ProjectConfig.webUrl());
        environment.setProperty("Download URL", ProjectConfig.downloadUrl());
        environment.setProperty("Browser", ProjectConfig.browser());
        environment.setProperty("Browser version", valueOrDefault(ProjectConfig.browserVersion(), "default"));
        environment.setProperty("Browser size", ProjectConfig.browserSize());
        environment.setProperty("Headless", String.valueOf(ProjectConfig.headless()));
        environment.setProperty("Page load strategy", ProjectConfig.pageLoadStrategy());
        environment.setProperty("Page load timeout", ProjectConfig.pageLoadTimeout() + " ms");
        environment.setProperty("Request interval", ProjectConfig.requestInterval() + " ms");
        environment.setProperty("Parallel", System.getProperty("parallel", "false"));
        environment.setProperty("Threads", System.getProperty("threads", "3"));
        environment.setProperty("Launch", ProjectConfig.isRemote() ? "Selenoid" : "Local");
        environment.setProperty("Java", System.getProperty("java.version"));
        environment.setProperty("Operating system", System.getProperty("os.name"));

        try (OutputStream output = Files.newOutputStream(resultsDirectory.resolve("environment.properties"))) {
            environment.store(output, "MAX UI test environment");
        }
    }

    private static void copyCategories(Path resultsDirectory) throws IOException {
        try (InputStream input = AllureEnvironment.class.getResourceAsStream("/categories.json")) {
            if (input != null) {
                Files.copy(
                        input,
                        resultsDirectory.resolve("categories.json"),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        }
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value.isBlank() ? defaultValue : value;
    }
}
