package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.*;
import com.boredxgames.tictactoeclient.domain.network.ServerConnectionManager;
import com.boredxgames.tictactoeclient.domain.services.GameService;
import com.boredxgames.tictactoeclient.domain.services.communication.Action;
import com.boredxgames.tictactoeclient.domain.services.communication.Message;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageType;
import com.google.gson.Gson;
import java.util.function.Consumer;

public class OnlinePVPService implements GameService {

    private static OnlinePVPService instance;
    private boolean isMyTurn = false;

    public void setupOnlineGame() {

        System.out.println(" Check equality"+OnlineGameState.info.getPlayer1().equalsIgnoreCase(ServerConnectionManager.getInstance().getPlayer().getId()));

        
        if (OnlineGameState.info.getPlayer1().equalsIgnoreCase(ServerConnectionManager.getInstance().getPlayer().getId())) {
            isMyTurn = true;
        }
    }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public boolean isIsMyTurn() {
        return isMyTurn;
    }

    public boolean checkTurn() {
        return isMyTurn;
    }

    public static OnlinePVPService getInstance() {
        if (instance == null) {
            instance = new OnlinePVPService();
        }
        return instance;
    }

    private OnlinePVPService() {
    }

    // specific listener to bridge Network -> UI
    private static Consumer<Move> moveListener;

    public OnlinePVPService setMoveListener(Consumer<Move> listener) {
        moveListener = listener;
        return this;
    }

    public static void onIncomingMove(MoveInfo moveInfo) {
        Gson gson = new Gson();
        Move move = gson.fromJson(moveInfo.getMove(), Move.class);
        if (moveListener != null) {
            moveListener.accept(move);
        }
    }

    @Override
    public void makeMove(Move move, char currentPlayer) {

        ServerConnectionManager connectionManager = ServerConnectionManager.getInstance();
        AuthResponseEntity player = connectionManager.getPlayer();
        GameStartInfo sessionInfo = OnlineGameState.info;
        // Prepare the data
        MoveInfo info = MoveInfo.createMoveInfo(sessionInfo.getRoomId(), player.getId(), move);

        // Send SEND_GAME_UPDATE action to server
        Message msg = Message.createMessage(
                MessageType.RESPONSE,
                Action.SEND_GAME_UPDATE,
                info
        );
        connectionManager.sendMessage(msg);
    }

    @Override
    public Move getNextMove(GameBoard board, char currentPlayer) {
        // In online play, we don't calculate the next move locally.
        // We wait for the server event instead.
        return null;
    }

    @Override
    public GameState getOutcome(GameBoard board) {
        return board.getGameState();
    }
}
