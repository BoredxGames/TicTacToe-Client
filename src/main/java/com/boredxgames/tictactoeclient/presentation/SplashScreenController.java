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
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;

    @FXML
    public void initialize() {

        updateTexts();
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

    private String safeGet(ResourceBundle bundle, String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void updateTexts() {
        ResourceBundle bundle = LocalizationManager.getLocalizationBundle();
        if (titleLabel != null) {
            titleLabel.setText(safeGet(bundle, "app.title", "Tic-Tac-Toe"));
        }
        if (subtitleLabel != null) {
            subtitleLabel.setText(safeGet(bundle, "app.edition", "v2.0.4"));
        }
    }
}
