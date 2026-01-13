package com.boredxgames.tictactoeclient.domain.model;
/**
 * @author Tasneem
 */

public record GameNavigationParams(
    String player1,
    String player2,
    GameMode mode,
    GameRecord replayData
) {
    public GameNavigationParams(String player1, String player2, GameMode mode) {
        this(player1, player2, mode, null);
    }
}