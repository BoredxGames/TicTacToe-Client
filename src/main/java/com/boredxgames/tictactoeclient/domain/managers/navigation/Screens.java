package com.boredxgames.tictactoeclient.domain.managers.navigation;

public enum Screens {
    PRIMARY("SplashScreen"),
    SECONDARY("GameModeScreen"),
    SETTINGS("SettingsScreen"),
    GAME_MODE("GameModeScreen"),
    OFFLINE_MODE_SELECTION("offline_mode_selection"),
    SERVER_CONNECTION("server_connection"),
    AUTHENTICATION("authentication"),
    GAME("game_screen"),
    Home("home"),
    PVP_SETUP("pvp_setup"),
    DifficultySelection("difficulty_selection");


    private final String name;

    Screens(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
