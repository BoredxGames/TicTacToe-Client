/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.GameMode;
import com.boredxgames.tictactoeclient.domain.model.GameNavigationParams;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Hazem
 */
public class Pvp_setupController implements Initializable {

    @FXML
    private Pane backgroundPane;
    @FXML
    private VBox contentContainer;
    @FXML
    private ToggleGroup starterGroup;
    @FXML
    private TextField player1Field;
    @FXML
    private TextField player2Field;
    @FXML
    private Button startGameBtn;
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.getChildren().clear();

        Platform.runLater(() -> {
            BackgroundAnimation.animateCardEntry(contentContainer);
            BackgroundAnimation.startWarpAnimation(
                    backgroundPane,
                    backgroundPane.getWidth(),
                    backgroundPane.getHeight()
            );
        });
    }

    @FXML
    private void onBackClicked(ActionEvent event) {
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);
    }

    @FXML
    private void onStartGameClicked(ActionEvent event) {
        String playerOneName = player1Field.getText().trim();
        String playerTwoName = player2Field.getText().trim();

        if (playerOneName.isEmpty()) {
            playerOneName = "Player 1";
        }

        if (playerTwoName.isEmpty()) {
            playerTwoName = "Player 2";
        }

        GameNavigationParams params = new GameNavigationParams(
                playerOneName,
                playerTwoName,
                GameMode.OFFLINE_PVP
        );

        NavigationManager.navigate(Screens.GAME, NavigationAction.REPLACE, params);
    }
}
