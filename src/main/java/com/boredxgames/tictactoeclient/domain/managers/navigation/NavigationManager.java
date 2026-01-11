package com.boredxgames.tictactoeclient.domain.managers.navigation;

import com.boredxgames.tictactoeclient.App;
import com.boredxgames.tictactoeclient.domain.managers.localization.LocalizationManager;
import com.boredxgames.tictactoeclient.domain.managers.state.StatefulController;
import com.boredxgames.tictactoeclient.domain.managers.theme.ThemeManager;
import java.io.IOException;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class NavigationManager {

    private NavigationManager() {
    }

    private static Scene scene;
    private static ScreenNavigationEntry current;
    private static final Stack<ScreenNavigationEntry> screenStack = new Stack<>();

    public static Scene init() throws IOException {

         // شاشة البداية
        current = new ScreenNavigationEntry(Screens.PRIMARY, null, null);

        Parent root = initRoot(current.screen().getName());
        scene = new Scene(root, 640, 480);

        // طبق الثيم على الـ Scene الرئيسي وفعل listener لأي تغيير مستقبلي
        ThemeManager.init(scene);

        scene = new Scene(initRoot(current.screen().getName()), 1600, 900);
        return scene;
    }

    public static void navigate(Screens screen, NavigationAction action) {
        switch (action) {
            case PUSH -> push(screen);
            case REPLACE -> replace(screen);
            case REPLACE_ALL -> replaceAll(screen);
        }

        System.out.println("Navigated To --" + screen.getName() + " Screen-- Using: " + action);
    }

    public static void pop() {
        if (screenStack.isEmpty()) return;

        current = screenStack.pop();
        loadScreen(current);
    }

    private static void push(Screens screen) {
        screenStack.push(saveState(current));

        current = new ScreenNavigationEntry(screen, null, null);
        loadScreen(current);
    }

    private static void replace(Screens screen) {
        current = new ScreenNavigationEntry(screen, null, null);
        loadScreen(current);
    }

    private static void replaceAll(Screens screen) {
        screenStack.clear();

        current = new ScreenNavigationEntry(screen, null, null);
        loadScreen(current);
    }

    private static void loadScreen(ScreenNavigationEntry entry) {
        try {
            FXMLLoader loader = getLoader(entry.screen().getName());

            Parent root = loader.load();
            Object controller = loader.getController();

            if (controller instanceof StatefulController sc && entry.state() != null) {
                sc.restoreState(entry.state());
            }

            scene.setRoot(root);

            ThemeManager.applyThemeTo(scene);
            
            current = new ScreenNavigationEntry(entry.screen(), entry.state(), controller);

        } catch (IOException e) {
            System.out.println("Failed to load screen entry: " + entry + " <-------> Exception: " + e);
        }
    }

    private static Parent initRoot(String fxml) throws IOException {

        FXMLLoader loader = getLoader(fxml);

        Parent root = loader.load();

        current = new ScreenNavigationEntry(Screens.PRIMARY, null, loader.getController());

        return root;
    }


    private static FXMLLoader getLoader(String fxml) throws IOException {

        return new FXMLLoader(
                App.class.getResource("/fxml/" + fxml + ".fxml"), LocalizationManager.getLocalizationBundle()
        );
    }


    private static ScreenNavigationEntry saveState(ScreenNavigationEntry entry) {
        if (entry.controller() instanceof StatefulController sc) {
            return new ScreenNavigationEntry(
                    entry.screen(),
                    sc.saveState(),
                    sc
            );
        }
        return entry;
    }
}
