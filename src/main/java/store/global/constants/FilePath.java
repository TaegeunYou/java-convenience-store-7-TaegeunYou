package store.global.constants;

public enum FilePath {
    PRODUCTS("src/main/resources/products.md"),
    PROMOTIONS("src/main/resources/promotions.md");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
