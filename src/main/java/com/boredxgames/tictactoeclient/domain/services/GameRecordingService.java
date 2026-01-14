/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boredxgames.tictactoeclient.domain.services;

import com.boredxgames.tictactoeclient.domain.model.GameRecord;
import com.boredxgames.tictactoeclient.domain.model.RecordedMove;
import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hazem
 */
public class GameRecordingService {

    private static final String HEADER_SIGNATURE = "TICTACTOE_REC_V1";
    private static final String DIRECTORY_PATH = "recordings";

    public void saveGame(GameBoard board, String p1Name, String p2Name, String filename) throws IOException {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!filename.endsWith(".dat")) {
            filename += ".dat";
        }

        File file = new File(directory, filename);

        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {

            dos.writeUTF(HEADER_SIGNATURE);

            dos.writeUTF(LocalDateTime.now().toString());
            dos.writeUTF(p1Name);
            dos.writeUTF(p2Name);
            
            // 3. Winner
            dos.writeChar(board.getWinner());

            // 4. Moves
            List<RecordedMove> history = board.getMoveHistory();
            dos.writeInt(history.size());

            for (RecordedMove move : history) {
                dos.writeByte(move.row());
                dos.writeByte(move.col());
                dos.writeChar(move.player());
            }
        }
    }

    public GameRecord readGame(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File not found");
        }

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {

            String header = dis.readUTF();
            if (!HEADER_SIGNATURE.equals(header)) {
                throw new IOException("Invalid file format");
            }

            String date = dis.readUTF();
            String p1Name = dis.readUTF();
            String p2Name = dis.readUTF();
            char winner = dis.readChar();

            int moveCount = dis.readInt();
            List<RecordedMove> moves = new ArrayList<>(moveCount);

            for (int i = 0; i < moveCount; i++) {
                int row = dis.readByte();
                int col = dis.readByte();
                char player = dis.readChar();
                moves.add(new RecordedMove(row, col, player));
            }

            return new GameRecord(date, p1Name, p2Name, winner, moves);
        }
    }
    
    public List<File> getAllRecordings() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) {
            return new ArrayList<>();
        }
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".dat"));
        return files != null ? List.of(files) : new ArrayList<>();
    }
}