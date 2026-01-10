/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.model;

import java.util.Vector;

/**
 *
 * @author mahmoud
 */
public class AvailablePlayersInfo {
 public Vector<PlayerEntity> onlinePlayers;
    public Vector<PlayerEntity> inGamePlayers;
    public Vector<PlayerEntity> pendingPlayers;

    public AvailablePlayersInfo(Vector<PlayerEntity> online, Vector<PlayerEntity> inGame, Vector<PlayerEntity> pending) {
        this.onlinePlayers = online;
        this.inGamePlayers = inGame;
        this.pendingPlayers = pending;
    }

    public Vector<PlayerEntity> getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(Vector<PlayerEntity> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public Vector<PlayerEntity> getInGamePlayers() {
        return inGamePlayers;
    }

    public void setInGamePlayers(Vector<PlayerEntity> inGamePlayers) {
        this.inGamePlayers = inGamePlayers;
    }

    public Vector<PlayerEntity> getPendingPlayers() {
        return pendingPlayers;
    }

    public void setPendingPlayers(Vector<PlayerEntity> pendingPlayers) {
        this.pendingPlayers = pendingPlayers;
    }


}