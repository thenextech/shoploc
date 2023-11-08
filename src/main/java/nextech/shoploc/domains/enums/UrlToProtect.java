package nextech.shoploc.domains.enums;

public enum UrlToProtect {
    ADMIN("/admins/"),
    CLIENT("/clients/"),
    MERCHANT("/merchants/");

    private final String url;

    UrlToProtect(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
