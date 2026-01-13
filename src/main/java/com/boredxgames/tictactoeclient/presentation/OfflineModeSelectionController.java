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


public class OfflineModeSelectionController implements Initializable {
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

}