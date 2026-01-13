/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.model;

import com.boredxgames.tictactoeclient.domain.services.communication.Action;
import com.boredxgames.tictactoeclient.domain.services.communication.Header;
import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageType;
import com.google.gson.Gson;

/**
 *
 * @author mahmoud
 */
public class MoveInfo {
    private final String roomId;
    private final String playerId;
    private final String move;

    public MoveInfo(String playerId, String move, String roomId) {
        this.playerId = playerId;
        this.move = move;
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getMove() {
        return move;
    }

    public static MoveInfo createMoveInfo(String roomId, String playerId , Object data)
    {
        return new MoveInfo(playerId , toJson(data), roomId);
    }

    static private String toJson(Object data)
    {
        Gson gson = new Gson();
        return  gson.toJson(data);

    }
}
