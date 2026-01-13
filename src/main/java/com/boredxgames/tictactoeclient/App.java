package com.boredxgames.tictactoeclient;

import com.boredxgames.tictactoeclient.domain.managers.audio.AudioManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.theme.ThemeManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = NavigationManager.init();
        ThemeManager.init(scene);
        stage.setScene(scene);
        stage.show();


        AudioManager.playDefaultMusic();


        AudioManager.attachGlobalClickSoundToScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}