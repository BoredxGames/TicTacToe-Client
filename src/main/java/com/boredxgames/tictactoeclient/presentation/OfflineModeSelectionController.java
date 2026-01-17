package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class OfflineModeSelectionController implements Initializable {

    
    @FXML
    private Label logoText;
    @FXML
    private Button settingsButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Label pvpCardTitle;
    @FXML
    private Label pvpCardDesc;
    @FXML
    private Label pvpCardBadge;
    @FXML
    private Label pvpActionText;
    @FXML
    private Label pveCardTitle;
    @FXML
    private Label pveCardDesc;
    @FXML
    private Label pveCardBadge;
    @FXML
    private Label pveActionText;
    @FXML
    private Button backButton;
    @FXML
    private Label backButtonText;
    
    private ResourceBundle resources;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        updateTexts();
    }
    @FXML
    private void handleSettings(ActionEvent event) {
        NavigationManager.navigate(Screens.SETTINGS, NavigationAction.PUSH);
    }
    @FXML
    private void handleBackToMainMenu(ActionEvent event) {
        NavigationManager.pop();
    }

    @FXML
    private void handlePlayerVsPlayer(MouseEvent event) {
        NavigationManager.navigate(Screens.PVP_SETUP, NavigationAction.PUSH);
    }

    @FXML
    private void handlePlayerVsCPU(MouseEvent event) {
        NavigationManager.navigate(Screens.DifficultySelection, NavigationAction.PUSH);
    }
    
    
    private void updateTexts() {
        if (resources == null) return;

        // Header / Logo
        logoText.setText(resources.getString("logo.text"));

        // Main labels
        titleLabel.setText(resources.getString("offline.title"));
        subtitleLabel.setText(resources.getString("offline.subtitle"));

        // PVP Card
        pvpCardTitle.setText(resources.getString("offline.pvp.title"));
        pvpCardDesc.setText(resources.getString("offline.pvp.description"));
        pvpCardBadge.setText(resources.getString("offline.pvp.badge"));
        pvpActionText.setText(resources.getString("offline.pvp.start"));

        // PVE Card
        pveCardTitle.setText(resources.getString("offline.pve.title"));
        pveCardDesc.setText(resources.getString("offline.pve.description"));
        pveCardBadge.setText(resources.getString("offline.pve.badge"));
        pveActionText.setText(resources.getString("offline.pve.start"));

        // Back button
        backButtonText.setText(resources.getString("offline.back"));
    }

}