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
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.INTERNAL_SERVER_ERROR;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.INVALID_CREDENTIAL;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.LOGIN_SUCCESS;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.REGISTERATION_SUCCESS;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.USERNAME_NOT_FOUND;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.USER_IS_ONLINE;
import com.boredxgames.tictactoeclient.presentation.AuthenticationController;
import com.google.gson.Gson;

public class MessageRouter {

    private static MessageRouter instance;
    private static ServerConnectionManager connection;
    private Gson gson = new Gson();
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
        Message responseMessage;
        switch (messageType) {
            case REQUEST -> responseMessage = handleRequest(message);
            case RESPONSE -> handleResponse(message);
            case EVENT -> {
            }
            case ERROR -> handleError(message);
            default -> System.out.println("Unknown MessageType: " + messageType);
        }
        // Handle event messages
            }

    private Message handleRequest(Message msg) {
        Action action = msg.getHeader().getAction();

        assert action != null;
        return switch (action) {

            default: {
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
                System.out.println(responseData);
                NavigationManager.navigate(Screens.SECONDARY, NavigationAction.REPLACE);

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
                System.out.println("Internal Server Error");
                AuthenticationController.showUserAlert("Internal Server Error");
                
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
