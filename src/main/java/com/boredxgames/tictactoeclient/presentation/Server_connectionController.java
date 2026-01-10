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
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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

       BackgroundAnimation.startBackgroundAnimation(backgroundPane, 1920, 1080);
        BackgroundAnimation.animateCardEntry(contentContainer);

        animateCardEntry();
    }

    private void animateCardEntry() {
        contentContainer.setOpacity(0);
        contentContainer.setTranslateY(500);

        TranslateTransition tt = new TranslateTransition(Duration.millis(1500), contentContainer);
        tt.setToY(0);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1000), contentContainer);
        ft.setToValue(1);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.play();
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