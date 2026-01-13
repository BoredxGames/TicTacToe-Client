/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.model;

import java.util.List;

/**
 *
 * @author Hazem
 */
public record GameRecord(
    String date,
    String player1,
    String player2,
    char winner,
    List<RecordedMove> moves
) {
    public String getResultDescription() {
        if (winner == 'X') return player1 + " (X) Won";
        if (winner == 'O') return player2 + " (O) Won";
        return "Draw";
    }
}