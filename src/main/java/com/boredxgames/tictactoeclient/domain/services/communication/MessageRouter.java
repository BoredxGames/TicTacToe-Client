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
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.LOGIN_SUCCESS;
import static com.boredxgames.tictactoeclient.domain.services.communication.Action.USERNAME_NOT_FOUND;
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
            default -> {
                System.out.println("Unknown Action: " + action);

            }
        };
    }

}
