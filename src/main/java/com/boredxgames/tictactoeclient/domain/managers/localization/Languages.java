package com.boredxgames.tictactoeclient.domain.managers.localization;

public enum Languages {
    ARABIC("ar", "EG"),
    ENGLISH("en", "US");

    private final String code;
    private final String country;

    Languages(String code, String country){
        this.code = code;
        this.country = country;
    }

    public  String getCode() {
        return code;
    }
    public String getCountry() {
        return country;
    }
}
