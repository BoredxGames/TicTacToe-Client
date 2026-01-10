package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.AuthResponseEntity;
import com.boredxgames.tictactoeclient.domain.model.AvailablePlayersInfo;
import com.boredxgames.tictactoeclient.domain.model.GameRequestInfo;
import com.boredxgames.tictactoeclient.domain.model.GameResponseInfo;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageRouter;
import com.boredxgames.tictactoeclient.domain.services.game.GameService;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class HomeController implements Initializable {

    @FXML private BorderPane rootPane;
    @FXML private Label usernameLabel; 
    @FXML private Label scoreLabel;    
    @FXML private PlayersController playersViewController; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MessageRouter.setHomeController(this);

        AuthResponseEntity currentUser = ServerConnectionManager.getInstance().getPlayer();
        System.out.println("currrrrent : "+currentUser.getUserName());
        if (currentUser != null) {
            if(usernameLabel != null) usernameLabel.setText(currentUser.getUserName());
            if(scoreLabel != null) scoreLabel.setText("score " + currentUser.getScore() + " P");
        }
        Platform.runLater(() -> {
        if (rootPane.getScene() != null) {
            rootPane.getScene().setFill(javafx.scene.paint.Color.valueOf("#111022"));
        }

        javafx.scene.Node headerBackground = rootPane.lookup(".tab-header-background");
        if (headerBackground != null) {
            headerBackground.setStyle("-fx-background-color: transparent;");
        }
        
        javafx.scene.Node viewport = rootPane.lookup(".viewport");
        if (viewport != null) {
            viewport.setStyle("-fx-background-color: transparent;");
        }
    });
                GameService.getInstance().requestAvailablePlayers();
                
                
    }

    public void updatePlayersList(AvailablePlayersInfo info) {
        if (playersViewController != null) {
            playersViewController.updateList(info);
        }
    }


public void handleServerGameResponse(GameResponseInfo info) {
        GameService.getInstance().setWaiting(false);

        if (info.isAccepted()) {
            System.out.println("Opponent Accepted Starting Game");
          
        } else {
            System.out.println("Opponent Declined.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Challenge Declined");
            alert.setHeaderText(null);
            alert.setContentText("Player " + info.getResponderUserName()+ " declined your challenge.");
            alert.showAndWait();
            
            GameService.getInstance().requestAvailablePlayers();
        }
    }

    public void showIncomingGameRequest(GameRequestInfo info) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Incoming Challenge");
            alert.setHeaderText("Game Request");
            alert.setContentText(info.getRequesterUserName()+ " wants to play Tic-Tac-Toe!");

            ButtonType acceptBtn = new ButtonType("Accept");
            ButtonType declineBtn = new ButtonType("Decline");

            alert.getButtonTypes().setAll(acceptBtn, declineBtn);

            alert.showAndWait().ifPresent(type -> {
                boolean accepted = (type == acceptBtn);
                GameService.getInstance().sendGameResponse(info, accepted);
            });
        });
    }
    
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void onSignOut() {
        ServerConnectionManager.getInstance().disconnect();
        
        ServerConnectionManager.getInstance().setPlayer(null); 
        
        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
    }
}