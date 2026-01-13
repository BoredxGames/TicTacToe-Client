package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.model.Move;

/**
 * @author Tasneem
 */
public interface GameService {
    void makeMove(Move move, char currentPlayer, GameBoard board);
    Move getNextMove(GameBoard board, char currentPlayer);
    GameState getOutcome(GameBoard board);
}
