package com.boredxgames.tictactoeclient.domain.managers.navigation;

public enum Screens {
    PRIMARY("SplashScreen"),
    SECONDARY("GameModeScreen"),
    SETTINGS("SettingsScreen"),


    SERVER_CONNECTION("server_connection"),
    AUTHENTICATION("authentication"),
    PVP_SETUP("pvp_setup");

    private final String name;

    Screens(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
