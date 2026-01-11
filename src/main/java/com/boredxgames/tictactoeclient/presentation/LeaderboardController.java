package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.model.AuthResponseEntity;
import com.boredxgames.tictactoeclient.domain.model.AvailablePlayersInfo;
import com.boredxgames.tictactoeclient.domain.model.PlayerEntity;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class LeaderboardController implements Initializable {

    @FXML private VBox leaderboardContainer;
    
    @FXML private HBox myRankBar;
    @FXML private Label myRankLabel;
    @FXML private Label myNameLabel;
    @FXML private Label myScoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void updateLeaderboard(AvailablePlayersInfo info) {
        Platform.runLater(() -> {
            leaderboardContainer.getChildren().clear();
            
            Vector<PlayerEntity> allPlayers = new Vector<>();
            if (info.getOnlinePlayers() != null) allPlayers.addAll(info.getOnlinePlayers());
            if (info.getInGamePlayers() != null) allPlayers.addAll(info.getInGamePlayers());
            if (info.getPendingPlayers() != null) allPlayers.addAll(info.getPendingPlayers());
            
            allPlayers.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

            AuthResponseEntity me = ServerConnectionManager.getInstance().getPlayer();
            boolean foundMe = false;

            int rank = 1;
            for (PlayerEntity p : allPlayers) {
                if (me != null && p.getId().equals(me.getId())) {
                    foundMe = true;
                    if (myRankBar != null) updateMyFooter(rank, p);
                }

                leaderboardContainer.getChildren().add(createRow(p, rank));
                rank++;
            }
            
            if (myRankBar != null) myRankBar.setVisible(foundMe);
        });
    }

    private void updateMyFooter(int rank, PlayerEntity p) {
        if (myRankLabel != null) myRankLabel.setText(String.valueOf(rank));
        if (myNameLabel != null) myNameLabel.setText(p.getUsername());
        if (myScoreLabel != null) myScoreLabel.setText(String.valueOf(p.getScore()));
    }

    private HBox createRow(PlayerEntity p, int rank) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("leaderboard-row");
        
        Label rankLabel = new Label();
        rankLabel.setPrefWidth(60);
        
        if (rank == 1) {
            rankLabel.setText("🏆"); 
            rankLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 20px;");
        } else if (rank == 2) {
            rankLabel.setText("🥈");
            rankLabel.setStyle("-fx-text-fill: #C0C0C0; -fx-font-size: 20px;");
        } else if (rank == 3) {
            rankLabel.setText("🥉");
            rankLabel.setStyle("-fx-text-fill: #CD7F32; -fx-font-size: 20px;");
        } else {
            rankLabel.setText(String.valueOf(rank));
            rankLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px; -fx-font-weight: bold;");
        }

        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        
        Circle avatar = new Circle(18);
        avatar.getStyleClass().add("avatar-profile");
        
        Label nameLabel = new Label(p.getUsername());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        nameBox.getChildren().addAll(avatar, nameLabel);

        Label scoreLabel = new Label(p.getScore() + " PTS");
        scoreLabel.setPrefWidth(100);
        scoreLabel.setAlignment(Pos.CENTER_RIGHT);
        scoreLabel.setStyle("-fx-text-fill: #00ff9d; -fx-font-family: 'Monospaced'; -fx-font-weight: bold;");

        row.getChildren().addAll(rankLabel, nameBox, scoreLabel);
        return row;
    }
}