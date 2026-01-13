/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.model.Move;
import com.boredxgames.tictactoeclient.domain.services.AIService;

/**
 *
 * @author sheri
 */
public class OfflinePVEAIService implements GameService {

    @Override
    public void makeMove(Move move, char currentPlayer, GameBoard board) {
                if (move != null) {
            board.makeMove(move.getRow(), move.getCol(), currentPlayer);
        }

    }

    @Override
    public Move getNextMove(GameBoard board, char currentPlayer) {
       if (currentPlayer == GameBoard.PLAYER_O) {
            int[] aiMove = AIService.nextMove(board, AIService.getDiffiulty());
            if (aiMove != null) {
                return new Move(aiMove[0], aiMove[1]);
            }
        }
        return null;
    }

    @Override
    public GameState getOutcome(GameBoard board) {
        return board.getGameState();
    }

}
