package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.localization.LocalizationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class GameModeScreenController {

    @FXML private VBox offlineCard;
    @FXML private VBox onlineCard;
    @FXML private Button settingsBtn;
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

        
        offlineCard.setOnMouseClicked(e -> {
            System.out.println("Offline Mode Selected");
           // NavigationManager.navigate(Screens.OFFLINE, NavigationAction.PUSH);
        });

        
        onlineCard.setOnMouseClicked(e -> {
            System.out.println("Online Mode Selected");
            
            //NavigationManager.navigate(Screens.ONLINE, NavigationAction.PUSH);
        });

    }

    @FXML
    private void openSettings(ActionEvent event) {
        System.out.println("Settings Button Clicked");
    NavigationManager.navigate(Screens.SETTINGS, NavigationAction.PUSH);
    }
    
    public void updateTexts() {
    ResourceBundle bundle = LocalizationManager.getLocalizationBundle();
    
    if (titleLabel != null) titleLabel.setText(bundle.getString("modes.title"));
    if (subtitleLabel != null) subtitleLabel.setText(bundle.getString("modes.subtitle"));
    
    if (offlineTitleLabel != null) offlineTitleLabel.setText(bundle.getString("modes.offline.title"));
    if (offlineDescLabel != null) offlineDescLabel.setText(bundle.getString("modes.offline.desc"));
    if (offlineActionLabel != null) offlineActionLabel.setText(bundle.getString("modes.offline.button"));
    
    if (onlineTitleLabel != null) onlineTitleLabel.setText(bundle.getString("modes.online.title"));
    if (onlineDescLabel != null) onlineDescLabel.setText(bundle.getString("modes.online.desc"));
    if (onlineActionLabel != null) onlineActionLabel.setText(bundle.getString("modes.online.button"));
}
}
