package testdata;

public record LocalizationData(
        String language,
        String languageButtonLabel,
        String title,
        String description,
        String phoneLoginButton,
        String helpLink
) {

    @Override
    public String toString() {
        return language;
    }
}
