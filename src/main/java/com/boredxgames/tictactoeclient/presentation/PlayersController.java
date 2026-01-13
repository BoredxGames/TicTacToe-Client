package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.model.AvailablePlayersInfo;
import com.boredxgames.tictactoeclient.domain.model.PlayerEntity;
import com.boredxgames.tictactoeclient.domain.services.GameService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class PlayersController {

    @FXML 
    private VBox playersContainer; 

    public void updateList(AvailablePlayersInfo info) {
        playersContainer.getChildren().clear();

        if (info.getOnlinePlayers() != null) {
            for (PlayerEntity p : info.getOnlinePlayers()) {
                playersContainer.getChildren().add(createPlayerCard(p, "ONLINE", "online", true));
            }
        }
   
        if (info.getInGamePlayers() != null) {
            for (PlayerEntity p : info.getInGamePlayers()) {
                playersContainer.getChildren().add(createPlayerCard(p, "IN GAME", "ingame", false));
            }
        }
    
        if (info.getPendingPlayers() != null) {
            for (PlayerEntity p : info.getPendingPlayers()) {
                playersContainer.getChildren().add(createPlayerCard(p, "Pending", "pending", false));
            }
        }

        if (playersContainer.getChildren().isEmpty()) {
            Label placeholder = new Label("No other players connected.");
            placeholder.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px; -fx-padding: 20;");
            playersContainer.getChildren().add(placeholder);
        }
    }

    private HBox createPlayerCard(PlayerEntity p, String statusText, String type, boolean canChallenge) {
        HBox card = new HBox(16);
        card.getStyleClass().add("player-card");
        card.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(card, Priority.ALWAYS); 
        card.setMaxWidth(Double.MAX_VALUE);

        StackPane avatarStack = new StackPane();
        Circle avatarBg = new Circle(24);
        avatarBg.setStyle("-fx-fill: #334155;"); 
        
        Circle statusDot = new Circle(6);
        statusDot.getStyleClass().add("status-dot-" + type);
        StackPane.setAlignment(statusDot, Pos.BOTTOM_RIGHT);
        avatarStack.getChildren().addAll(avatarBg, statusDot);

        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        
        Label nameLabel = new Label(p.getUsername());
        nameLabel.getStyleClass().add("player-name");
        
        HBox statusLine = new HBox(6);
        statusLine.setAlignment(Pos.CENTER_LEFT);
        Label statusLabel = new Label(statusText);
        statusLabel.getStyleClass().add("status-text-" + type);
        Label eloLabel = new Label("• " + p.getScore() + " pts"); 
        eloLabel.getStyleClass().add("player-subtext");
        
        statusLine.getChildren().addAll(statusLabel, eloLabel);
        infoBox.getChildren().addAll(nameLabel, statusLine);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button actionBtn = new Button();
        
        if (canChallenge) {
            actionBtn.setText("Challenge ⚔");
            actionBtn.getStyleClass().add("btn-challenge");
            
            actionBtn.setOnAction(e -> {
                System.out.println("response tyepeeeeeeeeeeeeeeeeeee : "+GameService.getInstance().isWaiting());
                if (GameService.getInstance().isWaiting()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Please Wait");
                    alert.setHeaderText(null);
                    alert.setContentText("You already have a pending request. Please wait for the response.");
                    alert.showAndWait();
                    return;
                }
                
                actionBtn.setDisable(true);
                actionBtn.setText("Sent...");
                GameService.getInstance().sendGameRequest(p.getId());
            });
            
        } else {
            actionBtn.setText(statusText);
            actionBtn.getStyleClass().add("btn-disabled");
            actionBtn.setDisable(true);
        }

        card.getChildren().addAll(avatarStack, infoBox, spacer, actionBtn);
        return card;
    }
}