package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.*;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageRouter;
import com.boredxgames.tictactoeclient.domain.services.game.OnlinGameSession;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class HomeController implements Initializable {

    @FXML private StackPane rootStack; 
    @FXML private BorderPane mainContent;
    @FXML private Pane backgroundPane; 
    @FXML private Label usernameLabel; 
    @FXML private Label scoreLabel;    
    @FXML private PlayersController playersViewController; 
@FXML private LeaderboardController leaderboardViewController; 
    private Alert currentIncomingRequestAlert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        MessageRouter.setHomeController(this);

        AuthResponseEntity currentUser = ServerConnectionManager.getInstance().getPlayer();
        if (currentUser != null) {
            if(usernameLabel != null) usernameLabel.setText(currentUser.getUserName());
            if(scoreLabel != null) scoreLabel.setText("Score: " + currentUser.getScore());
        }

      
         OnlinGameSession.getInstance().requestLeaderboard();
                OnlinGameSession.getInstance().requestAvailablePlayers();


    }

 public void updatePlayersList(AvailablePlayersInfo info) {
        if (playersViewController != null) {
        playersViewController.updateList(info);
    }
    }

    public void handleServerGameResponse(GameResponseInfo info) {
        OnlinGameSession.getInstance().setWaiting(false);

        if (info.isAccepted()) {
            System.out.println("Opponent Accepted. Waiting for GAME_START event...");
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Challenge Declined");
                alert.setHeaderText(null);
                alert.setContentText("Player " + info.getResponderUserName()+ " declined your challenge.");
                alert.showAndWait();
                
                OnlinGameSession.getInstance().requestAvailablePlayers();
            });
        }
    }

    public void showIncomingGameRequest(GameRequestInfo info) {
        Platform.runLater(() -> {
            if (currentIncomingRequestAlert != null && currentIncomingRequestAlert.isShowing()) {
                currentIncomingRequestAlert.close();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Incoming Challenge");
            alert.setHeaderText("Game Request");
            alert.setContentText(info.getRequesterUserName()+ " wants to play Tic-Tac-Toe!");

            ButtonType acceptBtn = new ButtonType("Accept");
            ButtonType declineBtn = new ButtonType("Decline");
            alert.getButtonTypes().setAll(acceptBtn, declineBtn);
            
            this.currentIncomingRequestAlert = alert; 

            alert.showAndWait().ifPresent(type -> {
                this.currentIncomingRequestAlert = null;
                boolean accepted = (type == acceptBtn);
                OnlinGameSession.getInstance().sendGameResponse(info, accepted);
            });
        });
    }

   
    public void dismissIncomingRequest() {
        Platform.runLater(() -> {
            if (currentIncomingRequestAlert != null && currentIncomingRequestAlert.isShowing()) {
                currentIncomingRequestAlert.setResult(ButtonType.CANCEL);
                currentIncomingRequestAlert.close();
                currentIncomingRequestAlert = null;
            }
        });
    }

    public void showErrorAlert(String message) {
        OnlinGameSession.getInstance().setWaiting(false);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
            
            OnlinGameSession.getInstance().requestAvailablePlayers();
        });
    }
    
    @FXML
    private void onSignOut() {
        ServerConnectionManager.getInstance().disconnect();
        ServerConnectionManager.getInstance().setPlayer(null); 
        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
    }
    
    public void updateLeaderboardUI(AvailablePlayersInfo info) {
    if (leaderboardViewController != null) {
        leaderboardViewController.updateLeaderboard(info);
    }
}
    @FXML
    private void openSettings(ActionEvent event) {
        System.out.println("Settings Button Clicked");
        NavigationManager.navigate(Screens.SETTINGS, NavigationAction.PUSH);
    }
}