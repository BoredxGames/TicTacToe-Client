package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.localization.LocalizationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class GameModeScreenController {

    @FXML
    private VBox offlineCard;
    @FXML
    private VBox onlineCard;
    @FXML
    private Button settingsBtn;
    @FXML
    private FlowPane cardsPane;
    @FXML
    private Label logoLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Label offlineTitleLabel;
    @FXML
    private Label offlineDescLabel;
    @FXML
    private Label offlineActionLabel;
    @FXML
    private Label onlineTitleLabel;
    @FXML
    private Label onlineDescLabel;
    @FXML
    private Label onlineActionLabel;
    @FXML
    private Label footerLabel;

    public void initialize() {
        updateTexts();
        setupActions();
    }

    private void setupActions() {

        // عند الضغط على الكارد الخاص بالـ Offline
        offlineCard.setOnMouseClicked(e -> {
            System.out.println("Offline Mode Selected");
            // NavigationManager.navigate(Screens.OFFLINE, NavigationAction.PUSH);
        });

        onlineCard.setOnMouseClicked(e -> {
            System.out.println("Online Mode Selected");

            NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE_ALL);

        });

    }

    @FXML
    private void openSettings(ActionEvent event) {
        System.out.println("Settings Button Clicked");
        NavigationManager.navigate(Screens.SETTINGS, NavigationAction.PUSH);
    }

    private String safeGet(ResourceBundle bundle, String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    public void updateTexts() {
        ResourceBundle bundle = LocalizationManager.getLocalizationBundle();

        if (titleLabel != null) {
            titleLabel.setText(safeGet(bundle, "modes.title", "Select Mode"));
        }
        if (subtitleLabel != null) {
            subtitleLabel.setText(safeGet(bundle, "modes.subtitle", "Choose how to play"));
        }

        if (offlineTitleLabel != null) {
            offlineTitleLabel.setText(safeGet(bundle, "modes.offline.title", "Offline"));
        }
        if (offlineDescLabel != null) {
            offlineDescLabel.setText(safeGet(bundle, "modes.offline.desc", "Play against AI or Local Player"));
        }
        if (offlineActionLabel != null) {
            offlineActionLabel.setText(safeGet(bundle, "modes.offline.button", "Start"));
        }

        if (onlineTitleLabel != null) {
            onlineTitleLabel.setText(safeGet(bundle, "modes.online.title", "Online"));
        }
        if (onlineDescLabel != null) {
            onlineDescLabel.setText(safeGet(bundle, "modes.online.desc", "Play with friends online"));
        }
        if (onlineActionLabel != null) {
            onlineActionLabel.setText(safeGet(bundle, "modes.online.button", "Start"));
        }

        if (footerLabel != null) {
            footerLabel.setText(safeGet(bundle, "footer.text", "Tic-Tac-Toe v2.0.4 • Build 8821"));
        }
        if (logoLabel != null) {
            logoLabel.setText(safeGet(bundle, "logo.text", "Tic-Tac-Toe"));
        }
    }
}
