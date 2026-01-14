/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.model;

/**
 *
 * @author moham
 */
public class GameEndInfo {

    private String roomId;
    private String winnerId; // If this is null, it means DRAW

    public GameEndInfo() {
    }

    public GameEndInfo(String roomId, String winnerId) {
        this.roomId = roomId;
        this.winnerId = winnerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getWinnerId() {
        return winnerId;
    }
}

