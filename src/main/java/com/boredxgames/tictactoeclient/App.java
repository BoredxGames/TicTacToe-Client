package com.boredxgames.tictactoeclient;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.theme.ThemeManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = NavigationManager.init();
        ThemeManager.init(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

/*
* 0- player_1 using player_id is x and starts the game
* -- if my plauer_id != player_1 id then i'm blocked
* 1- forward move
* 2- in forward move send game status (winner, draw, In-Progress)
* 3- winner -> update score for player
* 
*
*
* */
/*
online game manager impls game manager

game manager:
- forward move
- can play



*/