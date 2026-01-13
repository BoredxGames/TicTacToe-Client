/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;

import java.util.List;
import java.util.Random;

/**
 *
 * @author sheri
 */
public class AIService {
        private static final Random random = new Random();
        private static AIDifficulty diffiulty = AIDifficulty.EASY;
        
    public static int[] nextMove(GameBoard board, AIDifficulty difficulty) {
        if (board.getAvailableMoves().isEmpty()) return null;

        return switch (difficulty) {
            case EASY -> randomMove(board);
            case MEDIUM -> mediumMove(board);
            case HARD -> hardMove(board);
        };
    }

    public static AIDifficulty getDiffiulty() {
        return diffiulty;
    }

    public static void setDiffiulty(AIDifficulty diffiulty) {
        AIService.diffiulty = diffiulty;
    }
    
    

    private static int[] randomMove(GameBoard board) {
        List<int[]> moves = board.getAvailableMoves();
        return moves.get(random.nextInt(moves.size()));
    }

    private static int[] mediumMove(GameBoard board) {
        if (random.nextBoolean()) return hardMove(board);
        return randomMove(board);
    }

    private static int[] hardMove(GameBoard board) {
        return MinimaxEngine.bestMove(board, board.getCurrentPlayer());
    }
}
