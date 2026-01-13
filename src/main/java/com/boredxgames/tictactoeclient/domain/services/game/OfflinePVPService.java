/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services.game;

import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.model.Move;

/**
 *
 * @author sheri
 */
public class OfflinePVPService implements GameService{

    @Override
    public void makeMove(Move move, char currentPlayer) {
    }

    @Override
    public void makeMove(Move move, char currentPlayer, GameBoard board) {
        board.makeMove(move.getRow(), move.getCol(), currentPlayer);
    }

    @Override
    public Move getNextMove(GameBoard board, char currentPlayer) {

        return null;
    }

    @Override
    public GameState getOutcome(GameBoard board) {
        
        return board.getGameState();
    }
}
