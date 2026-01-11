package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.services.authentication.AuthenticationService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class AuthenticationController implements Initializable {

  
    @FXML private ToggleGroup accessTabs;
    @FXML private ToggleButton loginBtn;
    @FXML private ToggleButton registerBtn;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField; 
    @FXML private SVGPath eyeIcon; 
    @FXML private Button submitButton;

  
    @FXML private Pane backgroundPane;
    @FXML private VBox contentContainer; 

   

    private boolean isPasswordVisible = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        accessTabs.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == loginBtn) switchToLoginMode();
            else switchToRegisterMode();
        });
        
        if (loginBtn.isSelected()) switchToLoginMode(); 
        else switchToRegisterMode();

       
        setupPasswordSync(passwordField, passwordTextField);
        
      
        Platform.runLater(() -> {
            double w = backgroundPane.getWidth() > 0 ? backgroundPane.getWidth() : 1000;
            double h = backgroundPane.getHeight() > 0 ? backgroundPane.getHeight() : 750;
            BackgroundAnimation.startBackgroundAnimation(backgroundPane, w, h);
            BackgroundAnimation.animateCardEntry(contentContainer);
        });
    }

    private void switchToLoginMode() {
        submitButton.setText("Login ➔");
    }

    private void switchToRegisterMode() {
        submitButton.setText("Enter Arena ➔");
    }

    private void setupPasswordSync(PasswordField pf, TextField tf) {
        if(pf == null || tf == null) return;
        pf.textProperty().addListener((obs, o, n) -> tf.setText(n));
        tf.textProperty().addListener((obs, o, n) -> pf.setText(n));
    }

    @FXML
    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        
        if (isPasswordVisible) {
            passwordTextField.setVisible(true); passwordTextField.setManaged(true);
            passwordField.setVisible(false); passwordField.setManaged(false);
        } else {
            passwordField.setVisible(true); passwordField.setManaged(true);
            passwordTextField.setVisible(false); passwordTextField.setManaged(false);
        }
    }

   @FXML
    private void onSubmit() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText(); 

        if (username.isEmpty() || password.isEmpty()) {
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
        System.out.println("Back clicked - Returning to Server Connection");
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

                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        });
    }
    
    
}