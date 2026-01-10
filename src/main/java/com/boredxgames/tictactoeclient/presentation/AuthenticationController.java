package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.authentication.AuthenticationService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AuthenticationController implements Initializable {

    @FXML
    private ToggleGroup accessTabs;
    @FXML
    private ToggleButton loginBtn;
    @FXML
    private ToggleButton registerBtn;
    @FXML
    private Label serverStatus;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML

    private Button submitButton;

    @FXML
    private Pane backgroundPane;
    @FXML
    private VBox contentContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        accessTabs.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == loginBtn) {
                switchToLoginMode();
            } else {
                switchToRegisterMode();
            }
        });

        if (loginBtn.isSelected()) {
            switchToLoginMode();
        } else {
            switchToRegisterMode();
        }

        BackgroundAnimation.animateCardEntry(contentContainer);
        backgroundPane.setMouseTransparent(true);

        Platform.runLater(() -> {
            BackgroundAnimation.startWarpAnimation(
                    backgroundPane,
                    backgroundPane.getWidth(),
                    backgroundPane.getHeight()
            );
        });

    }

    private void switchToLoginMode() {
        submitButton.setText("Login");
    }

    private void switchToRegisterMode() {
        usernameField.clear();
        passwordField.clear();
        submitButton.setText("Join-us");
    }

    @FXML
    private void onSubmit() {
        if (!ServerConnectionManager.getInstance().isConnected()) {
            showUserAlert("Server is out of service");
            serverStatus.setText("Server Status: Offline");

        }
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showUserAlert("Validation Error: Please fill all fields");
            System.out.println("Validation Error: Please fill all fields");
            return;
        }

        AuthenticationService authService = AuthenticationService.getInstance();

        if (loginBtn.isSelected()) {
            System.out.println("Submitting Login for: " + username);
            authService.requestLogin(username, password);
        } else {
            System.out.println("Submitting Registration for: " + username);
            authService.requestRegister(username, password);
        }
    }

    @FXML
    private void onBackClicked() {

        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
    }

    public static void showUserAlert(String message) {
        Platform.runLater(() -> {
            try {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText(null);
                alert.setContentText(message);

                javafx.stage.Window.getWindows().stream()
                        .filter(javafx.stage.Window::isShowing)
                        .findFirst()
                        .ifPresent(window -> alert.initOwner(window));

                alert.showAndWait().ifPresent(type -> {
                    if (message == "Server is out of srevice"
                            || message == "Internal Server Error") {
                        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
