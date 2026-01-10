package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.services.GameBoard;
import com.boredxgames.tictactoeclient.domain.services.GameBoard.GameState;

public abstract class GameManager {

    protected GameBoard gameBoard;
    protected GameStateListener listener;

    public interface GameStateListener {
        void onBoardUpdated();
        void onGameOver(GameState state, char winner);
        void onError(String message);
    }

    public GameManager(GameStateListener listener) {
        this.listener = listener;
        this.gameBoard = new GameBoard();
    }

    public abstract void makeMove(int row, int col);

    public void resetGame() {
        gameBoard.resetGame();
        if (listener != null) {
            listener.onBoardUpdated();
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public char getCurrentPlayer() {
        return gameBoard.getCurrentPlayer();
    }

    protected void checkGameState() {
        if (listener == null) return;

        GameState state = gameBoard.getGameState();
        if (state != GameState.IN_PROGRESS) {
            listener.onGameOver(state, gameBoard.getWinner());
        }
    }
}