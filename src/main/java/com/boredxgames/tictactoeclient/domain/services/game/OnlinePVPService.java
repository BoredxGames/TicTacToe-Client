package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
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

    public boolean shouldPlayFirst() {

        System.out.println(" Check equality"+OnlineGameState.info.getPlayer1().equalsIgnoreCase(ServerConnectionManager.getInstance().getPlayer().getId()));

        
        if (OnlineGameState.info.getPlayer1().equalsIgnoreCase(ServerConnectionManager.getInstance().getPlayer().getId())) {
            return true;
        }
        return false;
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
        Object moveData = moveInfo.getMove();
        Move move;

        if (moveData instanceof String) {
            move = gson.fromJson((String) moveData, Move.class);
        } else {
            String json = gson.toJson(moveData);
            move = gson.fromJson(json, Move.class);
        }

        if (moveListener != null) {
            moveListener.accept(move);
        }
    }

    @Override
    public void makeMove(Move move, char currentPlayer) {

        ServerConnectionManager connectionManager = ServerConnectionManager.getInstance();
        AuthResponseEntity player = connectionManager.getPlayer();
        GameStartInfo sessionInfo = OnlineGameState.info;
        MoveInfo info = MoveInfo.createMoveInfo(sessionInfo.getRoomId(), player.getId(), move);

        Message msg = Message.createMessage(
                MessageType.RESPONSE,
                Action.SEND_GAME_UPDATE,
                info
        );
        connectionManager.sendMessage(msg);
    }
    
    public void sendGameEnd(String winnerId) {
                ServerConnectionManager connectionManager = ServerConnectionManager.getInstance();

        if (OnlineGameState.info == null) return;

        String roomId = OnlineGameState.info.getRoomId();
        GameEndInfo endInfo = new GameEndInfo(roomId, winnerId);

        Message msg = Message.createMessage(
                MessageType.RESPONSE,
                Action.GAME_END, 
                endInfo
        );
        
        connectionManager.sendMessage(msg);
             NavigationManager.navigate(Screens.Home, NavigationAction.REPLACE);
    }
    @Override
    public void makeMove(Move move, char currentPlayer, GameBoard board) {

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
