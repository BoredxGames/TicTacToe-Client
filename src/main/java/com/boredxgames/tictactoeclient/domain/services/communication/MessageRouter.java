/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.communication;

/**
 *
 * @author Hazem
 */
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.AuthResponseEntity;
import com.boredxgames.tictactoeclient.domain.model.AvailablePlayersInfo;
import com.boredxgames.tictactoeclient.domain.model.GameRequestInfo;
import com.boredxgames.tictactoeclient.domain.model.GameResponseInfo;
import com.boredxgames.tictactoeclient.domain.model.GameStartInfo;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.INTERNAL_SERVER_ERROR;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.INVALID_CREDENTIAL;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.LOGIN_SUCCESS;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.REGISTERATION_SUCCESS;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.USERNAME_NOT_FOUND;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.USER_IS_ONLINE;

import com.boredxgames.tictactoeclient.domain.services.game.OnlineGameManager;
import com.boredxgames.tictactoeclient.presentation.AuthenticationController;
import com.boredxgames.tictactoeclient.presentation.HomeController;
import com.google.gson.Gson;
import javafx.application.Platform;

public class MessageRouter {

    private static MessageRouter instance;
    private static ServerConnectionManager connection;
    private Gson gson = new Gson();
    private static HomeController homeController;
    private MessageRouter() {
        connection = ServerConnectionManager.getInstance();
    }

    public static MessageRouter getInstance() {
        if (instance == null) {
            instance = new MessageRouter();
        }
        return instance;
    }

    public void navigateMessage(String response) {
        Message message = gson.fromJson(response, Message.class);
        MessageType messageType = message.getHeader().getMsgType();

        assert messageType != null;
        switch (messageType) {
            case REQUEST -> handleRequest(message);
            case RESPONSE -> handleResponse(message);
            case EVENT -> handleEvent(message);
            case ERROR -> handleError(message);
            default -> System.out.println("Unknown MessageType: " + messageType);
        }
            }
    public static void setHomeController(HomeController controller) {
        homeController = controller;
    }

    private Message handleRequest(Message msg) {
        Action action = msg.getHeader().getAction();

        assert action != null;
        return switch (action) {
//            case SEND_GAME_UPDATE ->
//            {
//            }


            default -> {
                System.out.println("Unknown Action: " + action);
                yield new Message();
            }
        };
    }

    
    private void handleResponse(Message msg) {
        Action action = msg.getHeader().getAction();
           System.out.println(action);
       
        switch (action) {
            case LOGIN_SUCCESS ->
            {
                System.out.println("Login Success");
                AuthResponseEntity responseData = gson.fromJson(msg.getData(),AuthResponseEntity.class);
               ServerConnectionManager.getInstance().setPlayer(responseData);
                System.out.println(responseData);
                NavigationManager.navigate(Screens.Home, NavigationAction.REPLACE);

            }
            case REGISTERATION_SUCCESS->{
                System.out.println("Registration success");
                AuthResponseEntity responseData = gson.fromJson(msg.getData(),AuthResponseEntity.class);
               AuthenticationController.showUserAlert("Registration success");

            }
            case USERNAME_NOT_FOUND->{
                System.out.println("Username not found");
                AuthenticationController.showUserAlert("Username not found");
                        
            }
            case GET_AVAILABLE_PLAYERS -> {
                AvailablePlayersInfo info = gson.fromJson(msg.getData(), AvailablePlayersInfo.class);
            if (homeController != null) {
                Platform.runLater(() -> homeController.updatePlayersList(info));
            }
        }
            case GAME_RESPONSE -> {
                GameResponseInfo info = gson.fromJson(msg.getData(), GameResponseInfo.class);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.handleServerGameResponse(info));
                }
            }

            default -> {
                System.out.println("Unknown Action: " + action);

            }
        };
    }

    private void handleEvent(Message msg) {
        Action action = msg.getHeader().getAction();
           System.out.println(action);

        switch (action) {
           case REQUEST_GAME -> {
                GameRequestInfo info = gson.fromJson(msg.getData(), GameRequestInfo.class);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showIncomingGameRequest(info));
                }

            }
            case GAME_START -> {
                GameStartInfo info = gson.fromJson(msg.getData(), GameStartInfo.class);
              NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);

            }

            default -> {
                System.out.println("Unknown Action: " + action);

            }
        };
    }
    
    private void handleError(Message msg) {
        System.out.println("my time has come");
        Action action = msg.getHeader().getAction();
           System.out.println(action);
       
        switch (action) {
           
            case USERNAME_NOT_FOUND->{
                System.out.println("Username not found");
                AuthenticationController.showUserAlert("Username not found");
                        
            }

            case INTERNAL_SERVER_ERROR->{
              String errorMessage = "Server error. Please try again.";
                 System.out.println("Internal Server Error");
                AuthenticationController.showUserAlert("Internal Server Error");
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showErrorAlert(errorMessage));
                }

            }
            case PLAYER_BUSY -> {
                String errorMsg = "That player is currently in a game.";
                System.out.println(errorMsg);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showErrorAlert(errorMsg));
                }
            }
            case PENDING_REQUEST_EXISTS -> {
                String errorMsg = "You already have a request pending.";
                System.out.println(errorMsg);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showErrorAlert(errorMsg));
                }
            }
            case TARGET_HAS_PENDING_REQUEST -> {
                String errorMsg = "That player is considering another challenge.";
                System.out.println(errorMsg);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showErrorAlert(errorMsg));
                }
            }
            case ROOM_NOT_FOUND -> {
                String errorMsg = "Game room no longer exists.";
                System.out.println(errorMsg);
                if (homeController != null) {
                    Platform.runLater(() -> homeController.showErrorAlert(errorMsg));
                }
            }
             case INVALID_CREDENTIAL->{
                 System.out.println("INVALID_CREDENTIAL");
                AuthenticationController.showUserAlert("INVALID CREDENTIAL");

            }
            case USER_IS_ONLINE->{
                 System.out.println("User alread logged in");
                AuthenticationController.showUserAlert("User alread logged in");

            }
            case USERNAME_ALREADY_EXIST->{
                 System.out.println("USERNAME_ALREADY_EXIST");
                AuthenticationController.showUserAlert("USERNAME ALREADY EXIST");

            }
            default -> {
                System.out.println("Unknown Action: " + action);

            }
        }
    }

}