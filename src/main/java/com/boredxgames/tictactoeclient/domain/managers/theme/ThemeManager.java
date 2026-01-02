package com.boredxgames.tictactoeclient.domain.managers.theme;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;

public final class ThemeManager {
    private ThemeManager() {}

    public static final String commonPath = "/css/themes/";

    private static final ObjectProperty<Theme> currentTheme =
            new SimpleObjectProperty<>(Theme.PURPLE);
    private static Scene scene;


    public static void init(Scene scene) {
        ThemeManager.scene = scene;
        reloadTheme(currentTheme.get());
        currentTheme.addListener((obs, oldTheme, newTheme) ->
                reloadTheme(newTheme)
        );
    }


    private static void reloadTheme(Theme theme) {
        scene.getStylesheets().removeIf(
                s -> s.contains(commonPath)
        );

        scene.getStylesheets().add(theme.getCssPath());
    }









    // Setters & Getters

    public static void setTheme(Theme theme) {
        if (theme == null || theme == currentTheme.get()) return;
        currentTheme.set(theme);
    }

    public static Theme getTheme() {
        return currentTheme.get();
    }

    public static ObjectProperty<Theme> themeProperty() {
        return currentTheme;
    }
}

