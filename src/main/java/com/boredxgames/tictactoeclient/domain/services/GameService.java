package com.boredxgames.tictactoeclient.domain.services;

import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.model.Move;
import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;

/**
 * @author Tasneem
 */
public interface GameService {
    void makeMove(Move move, char currentPlayer);
    Move getNextMove(GameBoard board, char currentPlayer);
    GameState getOutcome(GameBoard board);
}
