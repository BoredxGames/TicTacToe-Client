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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

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
    @FXML
    private HBox pveBadgeHBox;
    @FXML
    private Label pveDotLabel;
    @FXML
    private Label pveTextLabel;
    @FXML
    private Label mainTitleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private FlowPane cardsFlowPane;
    @FXML
    private VBox easyCardVBox;
    @FXML
    private StackPane easyIconArea;
    @FXML
    private StackPane easyIconCircle;
    @FXML
    private SVGPath easySVG;
    @FXML
    private VBox easyCardContentVBox;
    @FXML
    private HBox easyCardHeaderHBox;
    @FXML
    private Label easyCardTitleLabel;
    @FXML
    private Region easyCardSpacer;
    @FXML
    private Label easyCardDifficultyLabel;
    @FXML
    private Label easyCardDescLabel;
    @FXML
    private StackPane mediumCardStackPane;
    @FXML
    private VBox mediumCardVBox;
    @FXML
    private StackPane mediumIconArea;
    @FXML
    private StackPane mediumIconCircle;
    @FXML
    private SVGPath mediumSVG;
    @FXML
    private VBox mediumCardContentVBox;
    @FXML
    private HBox mediumCardHeaderHBox;
    @FXML
    private Label mediumCardTitleLabel;
    @FXML
    private Region mediumCardSpacer;
    @FXML
    private Label mediumCardDifficultyLabel;
    @FXML
    private Label mediumCardDescLabel;
    @FXML
    private Label recommendedLabel;
    @FXML
    private VBox hardCardVBox;
    @FXML
    private StackPane hardIconArea;
    @FXML
    private StackPane hardIconCircle;
    @FXML
    private SVGPath hardSVG;
    @FXML
    private VBox hardCardContentVBox;
    @FXML
    private HBox hardCardHeaderHBox;
    @FXML
    private Label hardCardTitleLabel;
    @FXML
    private Region hardCardSpacer;
    @FXML
    private Label hardCardDifficultyLabel;
    @FXML
    private Label hardCardDescLabel;

    /**
     * Initializes the controller class.
     */
    private ResourceBundle rb;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        updateTexts();
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

        private void updateTexts() {
        // PVE Badge
        pveTextLabel.setText(rb.getString("difficulty.mode.pve"));
        
        mainTitleLabel.setText(rb.getString("difficulty.title"));
        subtitleLabel.setText(rb.getString("difficulty.subtitle"));

        // EASY CARD
        easyCardTitleLabel.setText(rb.getString("difficulty.easy.title"));
        easyCardDifficultyLabel.setText(rb.getString("difficulty.easy.level"));
        easyCardDescLabel.setText(rb.getString("difficulty.easy.desc"));
        easyBtn.setText(rb.getString("difficulty.easy.button"));

        // MEDIUM CARD
        mediumCardTitleLabel.setText(rb.getString("difficulty.medium.title"));
        mediumCardDifficultyLabel.setText(rb.getString("difficulty.medium.level"));
        mediumCardDescLabel.setText(rb.getString("difficulty.medium.desc"));
        mediumBtn.setText(rb.getString("difficulty.medium.button"));
        recommendedLabel.setText(rb.getString("difficulty.medium.recommended"));

        // HARD CARD
        hardCardTitleLabel.setText(rb.getString("difficulty.hard.title"));
        hardCardDifficultyLabel.setText(rb.getString("difficulty.hard.level"));
        hardCardDescLabel.setText(rb.getString("difficulty.hard.desc"));
        hardBtn.setText(rb.getString("difficulty.hard.button"));

        // Back label
        backLabel.setText(rb.getString("difficulty.back"));
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