/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

/**
 *
 * @author sheri
 */
public class MinimaxEngine {

    public static int[] bestMove(GameBoard board, char player) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        char opponent = (player == GameBoard.PLAYER_X) ? GameBoard.PLAYER_O : GameBoard.PLAYER_X;

        for (int[] move : board.getAvailableMoves()) {
            GameBoard copy = BoardUtils.copy(board);
            copy.forceMove(move[0], move[1], player);

            int score = minimax(copy, false, 0, player, opponent);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int minimax(GameBoard board, boolean isMax, int depth, char player, char opponent) {
        if (board.checkWin(player)) {
            return 10 - depth;
        }
        if (board.checkWin(opponent)) {
            return depth - 10;
        }
        if (board.getAvailableMoves().isEmpty()) {
            return 0;
        }

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int[] move : board.getAvailableMoves()) {
            GameBoard copy = BoardUtils.copy(board);
            copy.forceMove(move[0], move[1], isMax ? player : opponent);
            int score = minimax(copy, !isMax, depth + 1, player, opponent);
            best = isMax ? Math.max(best, score) : Math.min(best, score);
        }

        return best;
    }
}
