package testdata;

public record PhoneInputData(
        String description,
        String input,
        String formattedValue,
        boolean submitEnabled
) {

    @Override
    public String toString() {
        return description;
    }
}
