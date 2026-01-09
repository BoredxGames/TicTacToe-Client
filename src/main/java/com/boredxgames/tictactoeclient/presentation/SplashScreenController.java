package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.localization.LocalizationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.launch;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreenController {

    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private Label titleLabel; // Added for localization
    @FXML
    private Label subtitleLabel; // Added for localization

    @FXML
    public void initialize() {
        
        updateTexts(); // Added to set localized texts
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    double progress = progressBar.getProgress();
                    progressBar.setProgress(progress + 0.01);
                })
        );

        timeline.setCycleCount(100);

        timeline.setOnFinished(e
                -> NavigationManager.navigate(
                        Screens.SECONDARY,
                        NavigationAction.REPLACE
                )
        );

        timeline.play();
    }
    
    
     // Added method to update title/subtitle based on current locale
    private void updateTexts() {
        ResourceBundle bundle = LocalizationManager.getLocalizationBundle(); // Get current language bundle
        if (titleLabel != null) {
            titleLabel.setText(bundle.getString("app.title"));
        }
        if (subtitleLabel != null) {
            subtitleLabel.setText(bundle.getString("app.edition"));
        }
    }
}
