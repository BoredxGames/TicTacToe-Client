package com.boredxgames.tictactoeclient.domain.managers.theme;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;

public final class ThemeManager {

    private ThemeManager() {
    }

    public static final String commonPath = "/css/themes/";

    private static final ObjectProperty<Theme> currentTheme
            = new SimpleObjectProperty<>(Theme.NEON_BLUE);
    private static Scene scene;

    public static void init(Scene scene) {
        ThemeManager.scene = scene;
        reloadTheme(currentTheme.get());
        currentTheme.addListener((obs, oldTheme, newTheme)
                -> reloadTheme(newTheme)
        );
    }

    private static void reloadTheme(Theme theme) {
        scene.getStylesheets().removeIf(
                s -> s.contains(commonPath)
        );

        scene.getStylesheets().add(theme.getCssPath());
    }

//    public static void applyThemeTo(Scene newScene) {
//        // نحذف أي ثيم قديم
//        newScene.getStylesheets().removeIf(s -> s.contains(commonPath));
//        // نضيف الثيم الحالي
//        newScene.getStylesheets().add(currentTheme.get().getCssPath());
//    }
// 
    
    public static void applyThemeTo(Scene newScene) {
    if (newScene == null) return;
    if (currentTheme.get() == null) return; // safety check

    newScene.getStylesheets().removeIf(s -> s.contains(commonPath));
    newScene.getStylesheets().add(currentTheme.get().getCssPath());
}
    // Setters & Getters
    public static void setTheme(Theme theme) {
        if (theme == null || theme == currentTheme.get() || scene == null) {
            return;
        }
        currentTheme.set(theme);
    }

    public static Theme getTheme() {
        return currentTheme.get();
    }

    public static ObjectProperty<Theme> themeProperty() {
        return currentTheme;
    }
}
