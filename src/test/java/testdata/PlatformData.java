package testdata;

public record PlatformData(String platform, String title, String expectedText) {

    @Override
    public String toString() {
        return platform;
    }
}
