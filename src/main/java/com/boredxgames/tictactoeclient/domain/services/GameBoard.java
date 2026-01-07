package com.boredxgames.tictactoeclient.domain.services;

/**
 *
 * @author Tasneem
 */
public class GameBoard {
    private final char[][] board;
    private final int BOARD_SIZE = 3;

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
        initializeBoard();
        currentPlayer = startingPlayer;
        gameState = GameState.IN_PROGRESS;
        movesCount = 0;
    }
    
    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    
    public void resetGame() {
        resetGame(PLAYER_X);
    }
    
    public void resetGame(char startingPlayer) {
        initializeBoard();
        currentPlayer = startingPlayer;
        gameState = GameState.IN_PROGRESS;
        movesCount = 0;
    }
    
    public boolean makeMove(int row, int col) {
        return makeMove(row, col, currentPlayer);
    }
    
    public boolean makeMove(int row, int col, char player) {
        if (!isValidMove(row, col)) {
            return false;
        } 
        
        board[row][col] = player;
        movesCount++;
        
        currentPlayer = player;
        updateGameState();
        return true;
    }
    
    public boolean isValidMove(int row, int col) {
        if(gameState != GameState.IN_PROGRESS) {
            return false;
        }
        
        if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        
        return board[row][col] == EMPTY;
    }
    
    
    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }
    
    private void updateGameState() {
        if (checkWin(PLAYER_X)) {
            gameState = GameState.X_WINS;
        } else if (checkWin(PLAYER_O)) {
            gameState = GameState.O_WINS;
        } else if (isBoardFull()) {
            gameState = GameState.DRAW;
        }
    }
    
    public boolean isBoardFull() {
        return movesCount == BOARD_SIZE * BOARD_SIZE;
    }
    
    public boolean checkWin(char player) {
        // rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        
        // col
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        
        // diagonal left -> right
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        
        // diagonal right -> left
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        
        return false;
    }
    
    public int[] getWinningLine() {
        if (gameState == GameState.IN_PROGRESS || gameState == GameState.DRAW) {
            return null;
        }
        
        char winner = (gameState == GameState.X_WINS) ? PLAYER_X : PLAYER_O;
        
        // row
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] == winner && board[i][1] == winner && board[i][2] == winner) {
                return new int[]{i, 0, i, 1, i, 2};
            }
        }
        
        // col
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j] == winner && board[1][j] == winner && board[2][j] == winner) {
                return new int[]{0, j, 1, j, 2, j};
            }
        }
        
        // diagonal left -> right
        if (board[0][0] == winner && board[1][1] == winner && board[2][2] == winner) {
            return new int[]{0, 0, 1, 1, 2, 2};
        }
        
        // diagonal right -> left
        if (board[0][2] == winner && board[1][1] == winner && board[2][0] == winner) {
            return new int[]{0, 2, 1, 1, 2, 0};
        }
        
        return null;
    }
    
    public char getWinner() {
        if (gameState == GameState.X_WINS) {
            return PLAYER_X;
        } else if (gameState == GameState.O_WINS) {
            return PLAYER_O;
        }
        return EMPTY;
    }
    
    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public char getCellValue(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return EMPTY;
        }
        return board[row][col];
    }
}
