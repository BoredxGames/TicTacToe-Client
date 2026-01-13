package com.boredxgames.tictactoeclient.domain.model;

import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;

/**
 * @author Tasneem
 */
public class Move {

    private final int row;
    private final int col;
    private final GameBoard board;

    public Move(int row, int col, GameBoard board) {
        this.row = row;
        this.col = col;
        this.board = board;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public GameBoard getBoard() {
        return board;
    }
}
