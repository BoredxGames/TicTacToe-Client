/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;


import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;

/**
 *
 * @author sheri
 */
public class BoardUtils {
    public static GameBoard copy(GameBoard original) {
        GameBoard copy = new GameBoard(original.getCurrentPlayer());
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                char cell = original.getCellValue(i, j);
                if (cell != GameBoard.EMPTY) copy.forceMove(i, j, cell);
            }
        return copy;
    }
}
