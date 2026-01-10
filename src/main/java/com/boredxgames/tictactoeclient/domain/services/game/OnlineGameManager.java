package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.MoveInfo;

import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;

import com.boredxgames.tictactoeclient.domain.services.communication.Action;

import com.boredxgames.tictactoeclient.domain.services.communication.Message;

import com.boredxgames.tictactoeclient.domain.services.communication.MessageType;


public class OnlineGameManager extends GameManager {

    private final String roomId;
    private final char localPlayerSymbol;
    private final String playerId;

    public OnlineGameManager(GameStateListener listener, String roomId, char localPlayerSymbol, String playerId) {
        super(listener);
        this.roomId = roomId;
        this.localPlayerSymbol = localPlayerSymbol;
        this.playerId = playerId;
    }

    @Override
    public void makeMove(int row, int col) {
        if (gameBoard.getCurrentPlayer() != localPlayerSymbol) {
            if (listener != null) {
                listener.onError("It is not your turn!");
            }
            return;
        }

        if (!gameBoard.isValidMove(row, col)) {
            if (listener != null) listener.onError("Invalid Move");
            return;
        }

        int[] moveData = new int[]{row, col};
        MoveInfo moveInfo = new MoveInfo(playerId, moveData, roomId);
        Message message = Message.createMessage(MessageType.REQUEST, Action.SEND_GAME_UPDATE, moveInfo);

        ServerConnectionManager.getInstance().sendMessage(message);

        boolean success = gameBoard.makeMove(row, col, localPlayerSymbol);
        if (success) {
            if (listener != null) {
                listener.onBoardUpdated();
                checkGameState();
            }
        }
    }

    public void onNetworkMoveReceived(int row, int col, char playerSymbol) {
        if (gameBoard.getCellValue(row, col) == playerSymbol) {
            return;
        }

        boolean success = gameBoard.makeMove(row, col, playerSymbol);

        if (success) {
            if (listener != null) {
                listener.onBoardUpdated();
                checkGameState();
            }
        } else {
            if (listener != null) listener.onError("Game desynchronized.");
        }
    }

    @Override
    public void resetGame() {
        throw new UnsupportedOperationException("Can't reset an online game.");
    }
}