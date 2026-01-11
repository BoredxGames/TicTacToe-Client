/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.communication;


public enum Action {
    LOGIN(10),
    REGISTER(20),
    REQUEST_GAME(30),
    GAME_RESPONSE(35),
    GAME_START(40),
    SEND_GAME_UPDATE(50),
    GAME_END(55),
    USERNAME_NOT_FOUND(60),
    REGISTERATION_SUCCESS(70),
    INTERNAL_SERVER_ERROR(80),
    INVALID_CREDENTIAL(90),
    LOGIN_SUCCESS(100),
    USERNAME_ALREADY_EXIST(110),
    PLAYER_BUSY(120),
    PENDING_REQUEST_EXISTS(130),
    TARGET_HAS_PENDING_REQUEST(140),
    ROOM_NOT_FOUND(150),
    INVALID_OPPONENT(160),
    NO_PENDING_REQUEST(170),
    GET_AVAILABLE_PLAYERS(180),
    USER_IS_ONLINE(190),
    GET_LEADERBOARD(200);


    final int id;

    Action(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Action valueOf(int id) {
        for (Action action : Action.values()) {
            if (action.getId() == id) {
                return action;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Action{" + "ordinal=" + ordinal() + ", name=" + name() + ", id=" + id + '}';
    }


}