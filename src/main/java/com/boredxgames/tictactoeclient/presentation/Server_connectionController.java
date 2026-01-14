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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class Server_connectionController implements Initializable {

    @FXML
    private ComboBox<String> ipComboBox;
    @FXML
    private Button connectButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Pane backgroundPane;
    @FXML
    private VBox contentContainer;

    private Thread connectionThread;

    private final Map<String, String> serverPresets = new HashMap<>();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private HBox topBar;
    @FXML
    private Label logoLabel;
    @FXML
    private Region topBarSpacer;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private Label backLabel;
    @FXML
    private VBox cardContainer;
    @FXML
    private VBox headerVBox;
    @FXML
    private StackPane iconStackPane;
    @FXML
    private SVGPath iconSVG;
    @FXML
    private Label cardTitleLabel;
    @FXML
    private Label cardSubtitleLabel;
    @FXML
    private VBox inputContainerVBox;
    @FXML
    private Label ipLabel;
    @FXML
    private HBox ipHBox;
    @FXML
    private HBox statusHBox;
    @FXML
    private SVGPath statusSVG;
    @FXML
    private Label footerLabel;

    private ResourceBundle rb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rb = resources;
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

        serverPresets.put("Local Server", "127.0.0.1");
        serverPresets.put("Network Server", "10.145.0.124");

        ipComboBox.getItems().addAll(serverPresets.keySet());
        ipComboBox.getSelectionModel().select("Local Server");
        ipComboBox.setEditable(true);

    }

    @FXML
    private void connect() {
        String input = ipComboBox.getValue();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

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

                    stopConnectionThread();
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
        stopConnectionThread();
        NavigationManager.navigate(Screens.GAME_MODE, NavigationAction.REPLACE_ALL);
    }

    private void stopConnectionThread() {
        if (connectionThread != null && connectionThread.isAlive()) {
            connectionThread.interrupt();
        }
    }

    private void updateTexts() {
        logoLabel.setText(rb.getString("server.title"));
        cardTitleLabel.setText(rb.getString("server.title"));
        cardSubtitleLabel.setText(rb.getString("server.subtitle"));
        backLabel.setText(rb.getString("server.back"));
        ipLabel.setText(rb.getString("server.ip.label"));
        connectButton.setText(rb.getString("server.connect.button"));
        statusLabel.setText(rb.getString("server.status.waiting"));
        footerLabel.setText(rb.getString("server.footer"));
    }
}
