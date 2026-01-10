package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.utils.Config;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Server_connectionController implements Initializable {

    @FXML private ComboBox<String> ipComboBox;
    @FXML private Button connectButton;
    @FXML private Label statusLabel;
    @FXML private Pane backgroundPane; 
    @FXML private VBox contentContainer; 

    private final Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ipComboBox.getItems().addAll("localhost");
       BackgroundAnimation.animateCardEntry(contentContainer);
     Platform.runLater(() -> {
            BackgroundAnimation.startWarpAnimation(
                backgroundPane, 
                backgroundPane.getWidth(), 
                backgroundPane.getHeight()
            );
            
        });
    
    }


@FXML
private void connect() {
    String ip = ipComboBox.getEditor().getText();

        try {
            statusLabel.setText("Status: Connecting to " + ip + "...");
            ServerConnectionManager.getInstance().connect(Config.SERVER_IP, Config.SERVER_PORT);
            NavigationManager.navigate(Screens.AUTHENTICATION, NavigationAction.REPLACE);

            statusLabel.setText("Status: Connected to " + ip + "!");
            
            if (!ipComboBox.getItems().contains(ip)) {
                ipComboBox.getItems().add(ip);
            }
        } catch (IOException ex) {
            statusLabel.setText("Status: Connection failed: " + ex.getMessage());
        
    } 
}

    
    @FXML
    private void onBackClicked() {
        System.out.println("Back clicked");
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);

    }
}