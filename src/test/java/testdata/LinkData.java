package testdata;

public record LinkData(String name, String url) {

    @Override
    public String toString() {
        return name;
    }
}
