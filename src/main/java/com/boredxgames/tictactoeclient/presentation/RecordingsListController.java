package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.GameMode;
import com.boredxgames.tictactoeclient.domain.model.GameNavigationParams;
import com.boredxgames.tictactoeclient.domain.model.GameRecord;
import com.boredxgames.tictactoeclient.domain.services.GameRecordingService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RecordingsListController implements Initializable {

    @FXML private VBox listContainer;
    private final GameRecordingService recordingService = new GameRecordingService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRecordings();
    }

    private void loadRecordings() {
        listContainer.getChildren().clear();
        List<File> files = recordingService.getAllRecordings();

        if (files.isEmpty()) {
            Label empty = new Label("No recordings found.");
            empty.setStyle("-fx-text-fill: #888; -fx-font-size: 16px;");
            listContainer.getChildren().add(empty);
            return;
        }

        for (File file : files) {
            try {
                GameRecord record = recordingService.readGame(file);
                listContainer.getChildren().add(createCard(file, record));
            } catch (IOException e) {
                System.out.println("Corrupt file: " + file.getName());
            }
        }
    }

    private HBox createCard(File file, GameRecord record) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 8; -fx-padding: 15;");

        Label icon = new Label("📼");
        icon.setStyle("-fx-font-size: 20px;");

        VBox info = new VBox(4);
        Label title = new Label(record.player1() + " vs " + record.player2());
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label date = new Label(record.date().substring(0, 10) + " • " + record.getResultDescription());
        date.setStyle("-fx-text-fill: #aaa; -fx-font-size: 11px;");

        info.getChildren().addAll(title, date);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button watchBtn = new Button("Watch");
        watchBtn.setStyle("-fx-background-color: #4f5ef7; -fx-text-fill: white; -fx-cursor: hand;");
        watchBtn.setOnAction(e -> watchReplay(record));

        Button deleteBtn = new Button("🗑");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff4444; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            file.delete();
            loadRecordings();
        });

        card.getChildren().addAll(icon, info, spacer, watchBtn, deleteBtn);
        return card;
    }

    private void watchReplay(GameRecord record) {
        GameNavigationParams params = new GameNavigationParams(
            record.player1(),
            record.player2(),
            GameMode.REPLAY,
            record
        );
        NavigationManager.navigate(Screens.GAME, NavigationAction.PUSH, params);
    }

    @FXML
    private void onBackClicked() {
        NavigationManager.pop();
    }
}