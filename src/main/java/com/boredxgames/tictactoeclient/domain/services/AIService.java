/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

import com.boredxgames.tictactoeclient.domain.services.GameBoard;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sheri
 */
public class AIService {

    private static final Random random = new Random();

    public static int[] nextMove(GameBoard board, AIDifficulty difficulty) {
        return switch (difficulty) {
            case EASY ->
                randomMove(board);
            case MEDIUM ->
                mediumMove(board);
            case HARD ->
                hardMove(board);
        };
    }

    // EASY
    private static int[] randomMove(GameBoard board) {
        List<int[]> moves = board.getAvailableMoves();
        return moves.get(random.nextInt(moves.size()));
    }

    // MEDIUM
    private static int[] mediumMove(GameBoard board) {
        if (random.nextBoolean()) {
            return hardMove(board);
        }
        return randomMove(board);
    }

    // HARD
    private static int[] hardMove(GameBoard board) {
        return MinimaxEngine.bestMove(board);
    }
}
