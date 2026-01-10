package com.boredxgames.tictactoeclient.domain.managers.navigation;

public enum Screens {
    PRIMARY("primary"),
    SECONDARY("secondary"),
    SETTINGS("settings"),
    SERVER_CONNECTION("server_connection"),
    AUTHENTICATION("authentication"),
     Home("home");
   

    private final String name;

    Screens(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
