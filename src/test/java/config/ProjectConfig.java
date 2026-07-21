package config;

public final class ProjectConfig {

    private ProjectConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("baseUrl", "https://max.ru");
    }

    public static String webUrl() {
        return System.getProperty("webUrl", "https://web.max.ru");
    }

    public static String downloadUrl() {
        return System.getProperty("downloadUrl", "https://download.max.ru");
    }

    public static String browser() {
        return System.getProperty("browser", "chrome");
    }

    public static String browserVersion() {
        return System.getProperty("browserVersion", "");
    }

    public static String browserSize() {
        return System.getProperty("browserSize", "1920x1080");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }

    public static long timeout() {
        return Long.parseLong(System.getProperty("timeout", "15000"));
    }

    public static long requestInterval() {
        return Long.parseLong(System.getProperty("requestInterval", "5000"));
    }

    public static String pageLoadStrategy() {
        return System.getProperty("pageLoadStrategy", "normal");
    }

    public static long pageLoadTimeout() {
        return Long.parseLong(System.getProperty("pageLoadTimeout", "30000"));
    }

    public static String remoteUrl() {
        return System.getProperty("remoteUrl", "");
    }

    public static String videoUrl() {
        return System.getProperty("videoUrl", "https://selenoid.autotests.cloud/video/");
    }

    public static String allureResultsDirectory() {
        return System.getProperty("allure.results.directory", "build/allure-results");
    }

    public static boolean isRemote() {
        return !remoteUrl().isBlank();
    }
}
