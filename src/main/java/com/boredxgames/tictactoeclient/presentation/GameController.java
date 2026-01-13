package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationParameterAware;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.GameMode;
import com.boredxgames.tictactoeclient.domain.model.GameNavigationParams;
import com.boredxgames.tictactoeclient.domain.model.GameRecord;
import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.model.Move;
import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;
import com.boredxgames.tictactoeclient.domain.services.game.GameService;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVEAIService;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVPService;
import com.boredxgames.tictactoeclient.domain.services.storage.GameRecordingService;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameController implements Initializable, NavigationParameterAware {

    public GridPane gameGrid;

    @FXML private Button backButton;
    @FXML private Button settingsButton;
    @FXML private HBox difficultyBadge;
    @FXML private Label difficultyLabel;

    @FXML private VBox playerCard;
    @FXML private VBox opponentCard;
    @FXML private Label playerNameLabel;
    @FXML private Label opponentNameLabel;
    @FXML private Label playerScoreLabel;
    @FXML private Label opponentScoreLabel;
    @FXML private Label opponentTypeLabel;

    @FXML private Button cell00, cell01, cell02;
    @FXML private Button cell10, cell11, cell12;
    @FXML private Button cell20, cell21, cell22;
    private Button[][] cells;

    @FXML private StackPane modalOverlay;
    @FXML private Text modalIcon;
    @FXML private Label modalTitle;
    @FXML private Label modalMessage;
    @FXML private MediaView victoryVideo;
    @FXML private Button playAgainButton;
    @FXML private Button changeDifficultyButton;
    @FXML private Button mainMenuButton;
    @FXML private Button saveGameButton;

    private GameBoard gameBoard;
    private GameMode gameMode;
    private int playerScore = 0;
    private int opponentScore = 0;
    private boolean isPlayerTurn = true;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    private GameService gameService;
    private final GameRecordingService recordingService = new GameRecordingService();
    private GameRecord replayData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cells = new Button[][]{
            {cell00, cell01, cell02},
            {cell10, cell11, cell12},
            {cell20, cell21, cell22}
        };
        gameBoard = new GameBoard();
        setupCellHandlers();
        setupButtonHandlers();
    }

    @Override
    public void setNavigationParameter(Object parameter) {
        if (parameter instanceof GameNavigationParams params) {
            this.player1Name = params.player1();
            this.player2Name = params.player2();
            this.gameMode = params.mode();
            this.replayData = params.replayData();
        } else {
            this.gameMode = GameMode.OFFLINE_PVP;
            this.player1Name = "Player 1";
            this.player2Name = "Player 2";
        }

        applyPlayerInfo();
        applyGameModeSettings();
        resetGame();

        if (gameMode == GameMode.REPLAY && replayData != null) {
            startReplay();
        }
    }

    private void applyPlayerInfo() {
        playerNameLabel.setText(player1Name);
        opponentNameLabel.setText(player2Name);
    }

    private void applyGameModeSettings() {
        switch (gameMode) {
            case OFFLINE_PVP -> {
                opponentTypeLabel.setText("PLAYER 2");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                changeDifficultyButton.setVisible(false);
                changeDifficultyButton.setManaged(false);
                gameService = new OfflinePVPService();
            }
            case OFFLINE_PVE -> {
                opponentTypeLabel.setText("CPU (O)");
                difficultyBadge.setVisible(true);
                difficultyBadge.setManaged(true);
                changeDifficultyButton.setVisible(true);
                changeDifficultyButton.setManaged(true);
                gameService = new OfflinePVEAIService();
            }
            case ONLINE_PVP -> {
                opponentTypeLabel.setText("ONLINE PLAYER");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                changeDifficultyButton.setVisible(false);
                changeDifficultyButton.setManaged(false);
            }
            case REPLAY -> {
                opponentTypeLabel.setText("REPLAY");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                changeDifficultyButton.setVisible(false);
                changeDifficultyButton.setManaged(false);
                if (saveGameButton != null) {
                    saveGameButton.setVisible(false);
                    saveGameButton.setManaged(false);
                }
                disableBoard();
            }
        }
    }

    private void setupCellHandlers() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row;
                final int c = col;
                cells[row][col].setOnAction(e -> handleCellClick(r, c));
            }
        }
    }

    private void setupButtonHandlers() {
        playAgainButton.setOnAction(e -> resetGame());

        mainMenuButton.setOnAction(e -> {
            NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE_ALL);
        });

        backButton.setOnAction(e -> NavigationManager.pop());
        settingsButton.setOnAction(e -> NavigationManager.navigate(Screens.SETTINGS, NavigationAction.REPLACE_ALL));
        changeDifficultyButton.setOnAction(e -> {});
        
        if (saveGameButton != null) {
            saveGameButton.setOnAction(e -> handleSaveGame());
        }
    }

    private void handleSaveGame() {
        String p1 = player1Name.replaceAll("[^a-zA-Z0-9]", "");
        String p2 = player2Name.replaceAll("[^a-zA-Z0-9]", "");
        long timestamp = System.currentTimeMillis();
        
        String filename = String.format("REC_%s_vs_%s_%d", p1, p2, timestamp);

        try {
            recordingService.saveGame(
                gameBoard, 
                player1Name, 
                player2Name, 
                filename
            );
            
            saveGameButton.setText("Saved!");
            saveGameButton.setDisable(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleCellClick(int row, int col) {
        if (!gameBoard.isValidMove(row, col)) return;
        if (gameMode == GameMode.REPLAY) return;

        char currentPlayer = gameBoard.getCurrentPlayer();

        switch (gameMode) {
            case OFFLINE_PVP -> {
                gameBoard.makeMove(row, col, currentPlayer);
                updateCell(row, col, currentPlayer);
                if (!checkGameEnd()) {
                    updateTurnIndicator();
                }
            }
            case OFFLINE_PVE -> {
                gameBoard.makeMove(row, col, GameBoard.PLAYER_X);
                updateCell(row, col, GameBoard.PLAYER_X);
                if (checkGameEnd()) return;

                disableBoard();
                PauseTransition pause = new PauseTransition(Duration.millis(500));
                pause.setOnFinished(e -> {
                    Move aiMove = gameService.getNextMove(gameBoard, GameBoard.PLAYER_O);
                    gameService.makeMove(aiMove, GameBoard.PLAYER_O, gameBoard);
                    if (aiMove != null) {
                        updateCell(aiMove.getRow(), aiMove.getCol(), GameBoard.PLAYER_O);
                    }
                    if (!checkGameEnd()) {
                        enableBoard();
                        gameBoard.switchPlayer();
                        updateTurnIndicator();
                    }
                });
                pause.play();
            }
        }
    }

    private void startReplay() {
        disableBoard();
        Timeline timeline = new Timeline();
        int delay = 0;

        for (var move : replayData.moves()) {
            delay += 1000;
            KeyFrame frame = new KeyFrame(Duration.millis(delay), e -> {
                gameBoard.forceMove(move.row(), move.col(), move.player());
                updateCell(move.row(), move.col(), move.player());
                
                gameBoard.switchPlayer();
                updateTurnIndicator();
                checkGameEnd(); 
            });
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }

    private boolean checkGameEnd() {
        GameState state = gameBoard.getGameState();
        if (state != GameState.IN_PROGRESS) {
            handleGameEnd();
            return true;
        }
        return false;
    }

    private void updateCell(int row, int col, char player) {
        Button cell = cells[row][col];
        String iconPath = (player == GameBoard.PLAYER_X) ? "/assets/icons/close.png" : "/assets/icons/circle.png";

        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
        ImageView imgView = new ImageView(img);

        imgView.setFitWidth(32);
        imgView.setFitHeight(32);
        imgView.setPreserveRatio(true);

        cell.setGraphic(imgView);
        cell.setDisable(true);
    }

    private void updateTurnIndicator() {
        char current = gameBoard.getCurrentPlayer();
        boolean isPlayerX = (current == GameBoard.PLAYER_X);

        if (gameMode == GameMode.OFFLINE_PVP || gameMode == GameMode.OFFLINE_PVE || gameMode == GameMode.REPLAY) {
            if (isPlayerX) {
                setActiveCard(playerCard, opponentCard);
            } else {
                setActiveCard(opponentCard, playerCard);
            }
        }
    }

    private void setActiveCard(VBox activeCard, VBox inactiveCard) {
        activeCard.getStyleClass().add("active-card");
        inactiveCard.getStyleClass().remove("active-card");
    }

    private void handleGameEnd() {
        GameState state = gameBoard.getGameState();

        int[] winningLine = gameBoard.getWinningLine();
        if (winningLine != null) {
            for (int i = 0; i < winningLine.length; i += 2) {
                int row = winningLine[i];
                int col = winningLine[i + 1];
                cells[row][col].getStyleClass().add("cell-winning");
            }
        }

        if (state == GameState.X_WINS) {
            if (gameMode != GameMode.REPLAY) {
                playerScore++;
                playerScoreLabel.setText(String.valueOf(playerScore));
            }
            playVictoryVideo();
        } else if (state == GameState.O_WINS) {
            if (gameMode != GameMode.REPLAY) {
                opponentScore++;
                opponentScoreLabel.setText(String.valueOf(opponentScore));
            }
        }

        PauseTransition pause = new PauseTransition(Duration.millis(800));
        pause.setOnFinished(e -> showGameOverModal(state));
        pause.play();
    }

    private void showGameOverModal(GameState state) {
        switch (state) {
            case X_WINS:
                modalIcon.setText("emoji_events");
                modalTitle.setText("Victory!");
                modalMessage.setText(gameMode == GameMode.OFFLINE_PVE
                        ? "The CPU didn't stand a chance against your moves."
                        : "Player 1 wins the game!");
                break;
            case O_WINS:
                modalIcon.setText("sentiment_dissatisfied");
                modalTitle.setText("Defeat!");
                modalMessage.setText(gameMode == GameMode.OFFLINE_PVE
                        ? "The CPU outsmarted you this time."
                        : "Player 2 wins the game!");
                break;
            case DRAW:
                modalIcon.setText("handshake");
                modalTitle.setText("Draw!");
                modalMessage.setText("It's a tie! Both players played well.");
                break;
        }

        if (saveGameButton != null) {
            saveGameButton.setText("Save Replay");
            saveGameButton.setDisable(false);
            
            boolean canSave = (gameMode == GameMode.OFFLINE_PVE || gameMode == GameMode.OFFLINE_PVP);
            saveGameButton.setVisible(canSave);
            saveGameButton.setManaged(canSave);
        }
        
        if (gameMode == GameMode.REPLAY) {
            playAgainButton.setVisible(false);
            playAgainButton.setManaged(false);
        } else {
            playAgainButton.setVisible(true);
            playAgainButton.setManaged(true);
        }

        modalOverlay.setVisible(true);
    }

    private void playVictoryVideo() {
        try {
            String videoPath = Objects.requireNonNull(getClass().getResource("/assets/videos/you_win.mp4")).toExternalForm();
            Media media = new Media(videoPath);
            MediaPlayer player = new MediaPlayer(media);
            victoryVideo.setMediaPlayer(player);
            victoryVideo.setVisible(true);
            player.setAutoPlay(true);
            player.setOnEndOfMedia(() -> victoryVideo.setVisible(false));
        } catch (Exception e) {
            System.out.println("Error getting the video: " + e);
        }
    }

    public void resetGame() {
        gameBoard.resetGame();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                cells[row][col].setGraphic(null);
                cells[row][col].setDisable(false);
                cells[row][col].getStyleClass().remove("cell-winning");
            }
        }
        modalOverlay.setVisible(false);
        updateTurnIndicator();
        enableBoard();
    }

    private void disableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard.getCellValue(row, col) == GameBoard.EMPTY) cells[row][col].setDisable(true);
            }
        }
    }

    private void enableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard.getCellValue(row, col) == GameBoard.EMPTY) cells[row][col].setDisable(false);
            }
        }
    }
}