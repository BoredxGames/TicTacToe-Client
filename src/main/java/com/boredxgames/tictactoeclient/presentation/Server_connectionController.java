package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.utils.Config;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    private Thread connectionThread;
    public static String pendingErrorMessage = null;
    
    private final Map<String, String> serverPresets = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
         
        Platform.runLater(() -> { 
            BackgroundAnimation.animateCardEntry(contentContainer);
            BackgroundAnimation.startWarpAnimation(
                backgroundPane,
                backgroundPane.getWidth(),
                backgroundPane.getHeight()
            );
        });
        
        serverPresets.put("Local Server", "127.0.0.1");
        serverPresets.put("Network Server", "192.168.1.3");

        ipComboBox.getItems().addAll(serverPresets.keySet());
        ipComboBox.getSelectionModel().select("Local Server");
        ipComboBox.setEditable(true);

        if (pendingErrorMessage != null) {
            statusLabel.setText("Status: " + pendingErrorMessage);
            statusLabel.setStyle("-fx-text-fill: #ff5555;");
            pendingErrorMessage = null;
        }

       
    }

    @FXML
    private void connect() {
        String input = ipComboBox.getValue();
        if (input == null || input.trim().isEmpty()) return;

        String ip = serverPresets.getOrDefault(input, input);
        
        statusLabel.setText("Status: Connecting to " + ip + "...");
        statusLabel.setStyle("-fx-text-fill: white;");
        connectButton.setDisable(true);

        connectionThread = new Thread(() -> {
            try {
                ServerConnectionManager.getInstance().connect(ip, Config.SERVER_PORT);

                Platform.runLater(() -> {
                    statusLabel.setText("Status: Connected to " + ip + "!");
                    
                    if (!ipComboBox.getItems().contains(input)) {
                         ipComboBox.getItems().add(input);
                    }
                    
                    NavigationManager.navigate(Screens.AUTHENTICATION, NavigationAction.REPLACE);
                });
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    statusLabel.setText("Status: Connection failed: " + ex.getMessage());
                    connectButton.setDisable(false);
                });
            }
        });

        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    @FXML
    private void onBackClicked() {
        if (connectionThread != null && connectionThread.isAlive()) {
            connectionThread.interrupt();
        }
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);
    }
}