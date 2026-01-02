package com.boredxgames.tictactoeclient.domain.managers.theme;

import com.boredxgames.tictactoeclient.App;

public enum Theme {

    PURPLE("/css/themes/purple.css"),
    GREEN("/css/themes/green.css"),
    RED("/css/themes/red.css");

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
