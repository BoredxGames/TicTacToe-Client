/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sheri
 */
public class GameBoard {
        private final char[][] board;
    private static final int BOARD_SIZE = 3;

    public static final char PLAYER_X = 'X';
    public static final char PLAYER_O = 'O';
    public static final char EMPTY = '-';

    private char currentPlayer;
    private GameState gameState;
    private int movesCount;

    public enum GameState {
        IN_PROGRESS,
        X_WINS,
        O_WINS,
        DRAW
    }

    public GameBoard() {
        this(PLAYER_X);
    }

    public GameBoard(char startingPlayer) {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        resetGame(startingPlayer);
    }

    public void resetGame(char startingPlayer) {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = EMPTY;

        currentPlayer = startingPlayer;
        gameState = GameState.IN_PROGRESS;
        movesCount = 0;
    }

    public boolean makeMove(int row, int col) {
        if (!isValidMove(row, col)) return false;

        board[row][col] = currentPlayer;
        movesCount++;
        updateGameState();

        if (gameState == GameState.IN_PROGRESS) switchPlayer();
        return true;
    }

    void forceMove(int row, int col, char player) {
        board[row][col] = player;
        movesCount++;
    }

    public boolean isValidMove(int row, int col) {
        if (gameState != GameState.IN_PROGRESS) return false;
        if (row < 0 || col < 0 || row >= BOARD_SIZE || col >= BOARD_SIZE) return false;
        return board[row][col] == EMPTY;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private void updateGameState() {
        if (checkWin(PLAYER_X)) gameState = GameState.X_WINS;
        else if (checkWin(PLAYER_O)) gameState = GameState.O_WINS;
        else if (movesCount == BOARD_SIZE * BOARD_SIZE) gameState = GameState.DRAW;
    }

    public boolean checkWin(char player) {
        for (int i = 0; i < BOARD_SIZE; i++)
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;

        for (int j = 0; j < BOARD_SIZE; j++)
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) return true;

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;

        return false;
    }

    public List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (board[i][j] == EMPTY) moves.add(new int[]{i, j});
        return moves;
    }

    public char getWinner() {
        if (gameState == GameState.X_WINS) return PLAYER_X;
        else if (gameState == GameState.O_WINS) return PLAYER_O;
        return EMPTY;
    }

    public char getCellValue(int r, int c) {
        return board[r][c];
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }
}
