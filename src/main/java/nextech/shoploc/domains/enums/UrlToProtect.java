package nextech.shoploc.domains.enums;

import lombok.Getter;

@Getter
public enum UrlToProtect {
    ADMIN("/admins/"),
    CLIENT("/clients/"),
    MERCHANT("/merchants/");

    private final String url;

    UrlToProtect(String url) {
        this.url = url;
    }

}
