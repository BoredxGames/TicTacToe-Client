/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

/**
 *
 * @author mahmoud
 */


import com.boredxgames.tictactoeclient.domain.model.AuthResponseEntity;
import com.boredxgames.tictactoeclient.domain.model.GameRequestInfo;
import com.boredxgames.tictactoeclient.domain.model.GameResponseInfo;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.communication.Action;
import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageType;

public class GameService {

    private static GameService instance;
    private final ServerConnectionManager connection;
    private boolean isWaitingForResponse = false;

    private GameService() {
        this.connection = ServerConnectionManager.getInstance();
    }

    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public boolean isWaiting() {
        return isWaitingForResponse;
    }

    public void setWaiting(boolean waiting) {
        this.isWaitingForResponse = waiting;
        System.out.println("DEBUG: User waiting state set to: " + waiting);
    }

    public void requestAvailablePlayers() {
        Message msg = Message.createMessage(MessageType.REQUEST, Action.GET_AVAILABLE_PLAYERS, "");
        connection.sendMessage(msg);
    }

    public void sendGameRequest(String targetPlayerId) {
        if (isWaitingForResponse) {
            System.out.println("Blocked: Already waiting for a response.");
            return;
        }

        AuthResponseEntity me = connection.getPlayer();
        if (me == null) return;

        GameRequestInfo requestInfo = new GameRequestInfo(me.getId(), me.getUserName(), targetPlayerId);
        Message msg = Message.createMessage(MessageType.REQUEST, Action.REQUEST_GAME, requestInfo);
        
        System.out.println("Sending Game Request to: " + targetPlayerId);
        connection.sendMessage(msg);
                setWaiting(true);
    }

    public void sendGameResponse(GameRequestInfo originalRequest, boolean accepted) {
        AuthResponseEntity me = connection.getPlayer();

        GameResponseInfo responseInfo = new GameResponseInfo(
                originalRequest.getRequesterId(), 
                originalRequest.getTargetId(),
                me.getUserName(),
                accepted
        );

        Message msg = Message.createMessage(MessageType.RESPONSE, Action.GAME_RESPONSE, responseInfo);
        connection.sendMessage(msg);
    }
}