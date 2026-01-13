package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.authentication.AuthenticationService;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AuthenticationController implements Initializable {

    @FXML private ToggleButton loginBtn;
    @FXML private ToggleButton registerBtn;
    @FXML private Label serverStatus;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button submitButton;
    @FXML private Pane backgroundPane;
    @FXML private VBox contentContainer;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,15}$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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
private void onLoginSelected() {
    usernameField.clear();
        passwordField.clear();
        submitButton.setText("Login");
}

@FXML
private void onRegisterSelected() {
   usernameField.clear();
        passwordField.clear();
        submitButton.setText("Join-us");
}
    
   

    @FXML
    private void onSubmit() {
        if (!ServerConnectionManager.getInstance().isConnected()) {
            showUserAlert("Server is out of service");
            serverStatus.setText("Server Status: Offline");
            return;
        }

        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        String validationError = validateInput(username, password);
        if (validationError != null) {
            showUserAlert(validationError);
            return;
        }

        AuthenticationService authService = AuthenticationService.getInstance();

        if (loginBtn.isSelected()) {
            authService.requestLogin(username, password);
        } else {
            authService.requestRegister(username, password);
        }
    }

    private String validateInput(String username, String password) {
        if (username.isEmpty()) {
            return "Username is required.";
        }
        
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return "Username must be 3-15 characters and contain only letters, numbers, or underscores.";
        }

        if (password.isEmpty()) {
            return "Password is required.";
        }

        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }

        return null;
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
                    if ("Server is out of service".equals(message) || "Internal Server Error".equals(message)) {
                        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}