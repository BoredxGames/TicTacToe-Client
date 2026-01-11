/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.authentication;

import com.boredxgames.tictactoeclient.domain.model.AuthRequestEntity;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.communication.Action;
import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageType;

/**
 *
 * @author Hazem
 */
public class AuthenticationService {

    private static AuthenticationService instance;
    private ServerConnectionManager connection;

    private AuthenticationService() {
       
        connection = ServerConnectionManager.getInstance();
    }

    public static AuthenticationService getInstance() {
         if(instance==null)
            instance = new AuthenticationService();
        return instance;
    }

    public void requestLogin(String username, String password) {
        System.out.println("here");
         AuthRequestEntity responseData = AuthRequestEntity.createAuthEntity(username, password);
        Message loginRequest = Message.createMessage(MessageType.REQUEST, Action.LOGIN, responseData);
        System.out.println(loginRequest.toString());
       
        connection.sendMessage(loginRequest);

    }

    public void requestRegister(String username, String password) {
        System.out.println("here");
        AuthRequestEntity reqEntity = AuthRequestEntity.createAuthEntity(username, password);
        Message registerRequest = Message.createMessage(MessageType.REQUEST, Action.REGISTER, reqEntity);
        connection.sendMessage(registerRequest);
    }

    public void handleAuthenticationResponse(Message message) {
        if (message.getData() != null) {
          
            
            //TODO navigate to online mode screen
            System.out.println("");
            
        } else {
            System.out.println("Authentication faild, no response data");
        }
    }

}
