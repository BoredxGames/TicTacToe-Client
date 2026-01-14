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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

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
    @FXML
    private HBox topBar;
    @FXML
    private Label logoLabel;
    @FXML
    private HBox navLinks;
    @FXML
    private VBox titleContainer;
    @FXML
    private SVGPath pvpIcon;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private VBox setupCard;
    @FXML
    private Label player1Label;
    @FXML
    private Label player1Badge;
    @FXML
    private HBox player1Container;
    @FXML
    private StackPane vsSeparator;
    @FXML
    private Label vsLabel;
    @FXML
    private Label player2Label;
    @FXML
    private Label player2Badge;
    @FXML
    private HBox player2Container;
    @FXML
    private Label starterLabel;
    @FXML
    private HBox starterToggleContainer;
    @FXML
    private ToggleButton player1StarterBtn;
    @FXML
    private ToggleButton player2StarterBtn;

    private ResourceBundle langBundle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.langBundle = rb;
        updateTexts();

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

    private void updateTexts() {
        if (langBundle == null) {
            return;
        }

        logoLabel.setText(langBundle.getString("pvp.logo"));
        titleLabel.setText(langBundle.getString("pvp.title"));
        subtitleLabel.setText(langBundle.getString("pvp.subtitle"));

        player1Label.setText(langBundle.getString("pvp.player1.label"));
        player1Badge.setText(langBundle.getString("pvp.player1.badge"));
        player1Field.setPromptText(langBundle.getString("pvp.player1.prompt"));

        player2Label.setText(langBundle.getString("pvp.player2.label"));
        player2Badge.setText(langBundle.getString("pvp.player2.badge"));
        player2Field.setPromptText(langBundle.getString("pvp.player2.prompt"));

        vsLabel.setText(langBundle.getString("pvp.vs"));

        starterLabel.setText(langBundle.getString("pvp.starter.label"));
        player1StarterBtn.setText(langBundle.getString("pvp.starter.player1"));
        player2StarterBtn.setText(langBundle.getString("pvp.starter.player2"));

        startGameBtn.setText(langBundle.getString("pvp.start"));
        backBtn.setText(langBundle.getString("pvp.back"));
    }
}
