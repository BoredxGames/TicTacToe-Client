/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

import com.boredxgames.tictactoeclient.domain.services.GameBoard;

/**
 *
 * @author sheri
 */
public class MinimaxEngine {

private static final char AI = GameBoard.PLAYER_O;
    private static final char HUMAN = GameBoard.PLAYER_X;

    public static int[] bestMove(GameBoard board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : board.getAvailableMoves()) {
            GameBoard copy = BoardUtils.copy(board);
            copy.forceMove(move[0], move[1], AI);

            int score = minimax(copy, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int minimax(GameBoard board, boolean isMax) {

        if (board.checkWin(AI)) return 10;
        if (board.checkWin(HUMAN)) return -10;
        if (board.getAvailableMoves().isEmpty()) return 0;

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : board.getAvailableMoves()) {
            GameBoard copy = BoardUtils.copy(board);
            copy.forceMove(move[0], move[1], isMax ? AI : HUMAN);

            int score = minimax(copy, !isMax);
            best = isMax ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }
}
