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
import com.boredxgames.tictactoeclient.domain.services.AIDifficulty;
import com.boredxgames.tictactoeclient.domain.services.AIService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author mahmoud
 */
public class Difficulty_selectionController implements Initializable {

    @FXML
    private Pane backgroundPane;
    @FXML
    private Button easyBtn;
    @FXML
    private Button mediumBtn;
    @FXML
    private Button hardBtn;
    @FXML
    private Label backLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.getChildren().clear();

        Platform.runLater(() -> {
            BackgroundAnimation.startWarpAnimation(
                    backgroundPane,
                    backgroundPane.getWidth(),
                    backgroundPane.getHeight()
            );
        });

        easyBtn.setOnAction(e -> startGameWithDifficulty("EASY"));
        mediumBtn.setOnAction(e -> startGameWithDifficulty("MEDIUM"));
        hardBtn.setOnAction(e -> startGameWithDifficulty("HARD"));

        backLabel.setOnMouseClicked(e -> goBack());
    }

    private void startGameWithDifficulty(String difficulty) {
        System.out.println("Starting game with difficulty: " + difficulty);

        switch (difficulty) {
            case "MEDIUM":
                AIService.setDiffiulty(AIDifficulty.MEDIUM);
                break;
            case "HARD":
                AIService.setDiffiulty(AIDifficulty.HARD);
                break;
            default:
                AIService.setDiffiulty(AIDifficulty.EASY);
        }
        GameNavigationParams params = new GameNavigationParams(
                "Player",
                "CPU",
                GameMode.OFFLINE_PVE
        );
        NavigationManager.navigate(Screens.GAME, NavigationAction.REPLACE, params);

    }

    private void goBack() {
        System.out.println("Back to previous screen");
        NavigationManager.navigate(Screens.GAME, NavigationAction.REPLACE);
    }
}
