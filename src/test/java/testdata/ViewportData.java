package testdata;

public record ViewportData(String name, int width, int height) {

    @Override
    public String toString() {
        return name + " " + width + "x" + height;
    }
}
