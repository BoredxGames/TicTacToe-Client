package com.boredxgames.tictactoeclient;

import com.boredxgames.tictactoeclient.domain.managers.audio.AudioManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = NavigationManager.init();
        stage.setScene(scene);
        stage.show();

        // شغل الموسيقى تلقائياً
        AudioManager.playDefaultMusic();

        // فعل الكليك على أي مكان
        AudioManager.attachGlobalClickSoundToScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
