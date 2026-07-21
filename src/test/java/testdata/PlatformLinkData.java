package testdata;

import java.util.List;

public record PlatformLinkData(String platform, String linkName, List<String> urls) {

    public PlatformLinkData(String platform, String linkName, String url) {
        this(platform, linkName, List.of(url));
    }

    @Override
    public String toString() {
        return platform + ": " + linkName;
    }
}
