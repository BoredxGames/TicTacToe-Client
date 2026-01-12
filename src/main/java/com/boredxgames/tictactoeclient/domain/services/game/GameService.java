package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.Move;
import com.boredxgames.tictactoeclient.domain.services.GameBoard;
/**
 * @author Tasneem
 */
public interface GameService {
    void makeMove(Move move, char currentPlayer);
    Move getNextMove(GameBoard board, char currentPlayer);
    GameBoard.GameState getOutcome(GameBoard board);
}
