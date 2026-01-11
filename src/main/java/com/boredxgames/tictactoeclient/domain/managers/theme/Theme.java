package com.boredxgames.tictactoeclient.domain.managers.theme;

import com.boredxgames.tictactoeclient.App;

public enum Theme {

    NEON_BLUE("/css/themes/neon_blue.css"),
    CYBER_PINK("/css/themes/cyber_pink.css"),
    TOXIC_GREEN("/css/themes/toxic_green.css");

    private final String cssPath;

    Theme(String cssPath) {
        this.cssPath = cssPath;
    }

    public String getCssPath() {
        return App.class
                .getResource(cssPath)
                .toExternalForm();
    }
}
