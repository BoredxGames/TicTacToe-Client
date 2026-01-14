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
import com.boredxgames.tictactoeclient.domain.model.OnlineGameState;
import com.boredxgames.tictactoeclient.domain.services.GameService;
import com.boredxgames.tictactoeclient.domain.services.communication.MessageRouter;
import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVEAIService;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVPService;
import com.boredxgames.tictactoeclient.domain.services.game.OnlinePVPService;
import com.boredxgames.tictactoeclient.domain.services.storage.GameRecordingService;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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

    @FXML
    private Button backButton;
    @FXML
    private Button settingsButton;
    @FXML
    private HBox difficultyBadge;
    @FXML
    private Label difficultyLabel;

    @FXML
    private VBox playerCard;
    @FXML
    private VBox opponentCard;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label opponentNameLabel;
    @FXML
    private Label playerScoreLabel;
    @FXML
    private Label opponentScoreLabel;
    @FXML
    private Label opponentTypeLabel;

    @FXML
    private Button cell00, cell01, cell02;
    @FXML
    private Button cell10, cell11, cell12;
    @FXML
    private Button cell20, cell21, cell22;
    private Button[][] cells;

    @FXML
    private StackPane modalOverlay;
    @FXML
    private Text modalIcon;
    @FXML
    private Label modalTitle;
    @FXML
    private Label modalMessage;
    @FXML
    private MediaView victoryVideo;
    @FXML
    private Button playAgainButton;
    @FXML
    private Button changeDifficultyButton;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button saveGameButton;

    private GameBoard gameBoard;
    private GameMode gameMode;
    private int playerScore = 0;
    private int opponentScore = 0;
    private boolean isPlayerTurn = true;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    private GameService gameService;
    private char localPlayerId;
    private final GameRecordingService recordingService = new GameRecordingService();
    private GameRecord replayData;

    private MediaPlayer currentMediaPlayer;

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
        MessageRouter.setGameController(this);
    }

    public void showErrorAlert(String message) {
        Platform.runLater(() -> {
            modalIcon.setText("error");
            modalTitle.setText("Connection Error");
            modalMessage.setText(message);

            playAgainButton.setVisible(false);
            playAgainButton.setManaged(false);

            if (saveGameButton != null) {
                saveGameButton.setVisible(false);
                saveGameButton.setManaged(false);
            }

            mainMenuButton.setVisible(true);
            mainMenuButton.setManaged(true);
            modalOverlay.setVisible(true);
            disableBoard();
        });
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
                opponentTypeLabel.setText(player2Name);
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);

                gameService = new OfflinePVPService();
            }
            case OFFLINE_PVE -> {
                opponentTypeLabel.setText("CPU (O)");
                difficultyBadge.setVisible(true);
                difficultyBadge.setManaged(true);

                gameService = new OfflinePVEAIService();
            }
            case ONLINE_PVP -> {
                opponentTypeLabel.setText(player2Name);
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                backButton.setVisible(false);
                backButton.setManaged(false);

                boolean amIPlayer1 = OnlinePVPService.getInstance().shouldPlayFirst();
                localPlayerId = amIPlayer1 ? GameBoard.PLAYER_X : GameBoard.PLAYER_O;

                System.out.println("I am Player: " + localPlayerId);

                gameService = OnlinePVPService.getInstance().setMoveListener((move) -> {
                    Platform.runLater(() -> {
                        char remotePlayer = (localPlayerId == GameBoard.PLAYER_X) ? GameBoard.PLAYER_O : GameBoard.PLAYER_X;

                        if (gameBoard.makeMove(move.getRow(), move.getCol(), remotePlayer)) {
                            updateCell(move.getRow(), move.getCol(), remotePlayer);

                            if (gameBoard.getGameState() != GameState.IN_PROGRESS) {
                                handleGameEnd();
                            } else {
                                updateTurnIndicator();
                                enableBoard();
                            }
                        }
                    });
                });

                if (amIPlayer1) {
                    enableBoard();
                } else {
                    disableBoard();
                }
                updateTurnIndicator();
            }
            case REPLAY -> {
                opponentTypeLabel.setText("REPLAY");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
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
            if (gameMode == GameMode.OFFLINE_PVE || gameMode == GameMode.OFFLINE_PVP) {
                NavigationManager.navigate(Screens.GAME_MODE, NavigationAction.REPLACE_ALL);
            } else {
                NavigationManager.navigate(Screens.Home, NavigationAction.REPLACE_ALL);
            }
        });

        backButton.setOnAction(e -> {
            if (gameMode == GameMode.OFFLINE_PVE || gameMode == GameMode.OFFLINE_PVP || gameMode == GameMode.REPLAY) {
                NavigationManager.navigate(Screens.GAME_MODE, NavigationAction.REPLACE_ALL);
            }
        });

        settingsButton.setOnAction(e -> NavigationManager.navigate(Screens.SETTINGS, NavigationAction.REPLACE_ALL));

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
        if (!gameBoard.isValidMove(row, col)) {
            return;
        }
        if (gameMode == GameMode.REPLAY) {
            return;
        }

        if (gameMode == GameMode.ONLINE_PVP && gameBoard.getCurrentPlayer() != localPlayerId) {
            return;
        }

        switch (gameMode) {
            case OFFLINE_PVP ->
                handleOfflinePvp(row, col);
            case OFFLINE_PVE ->
                handleOfflinePve(row, col);
            case ONLINE_PVP ->
                handleOnlinePvp(row, col);
        }
    }

    private void handleOfflinePvp(int row, int col) {
        char currentPlayer = gameBoard.getCurrentPlayer();

        performMove(row, col, currentPlayer);

        if (!checkGameEnd()) {
            updateTurnIndicator();
        }
    }

    private void handleOfflinePve(int row, int col) {
        performMove(row, col, GameBoard.PLAYER_X);

        if (checkGameEnd()) {
            return;
        }
        updateTurnIndicator();
        scheduleAiTurn();
    }

    private void handleOnlinePvp(int row, int col) {
        performMove(row, col, localPlayerId);

        Move move = new Move(row, col);
        gameService.makeMove(move, localPlayerId);

        disableBoard();

        if (gameBoard.getGameState() != GameState.IN_PROGRESS) {
            handleGameEnd();
        } else {
            updateTurnIndicator();
        }
    }

    private void performMove(int row, int col, char player) {
        gameBoard.makeMove(row, col, player);
        updateCell(row, col, player);

    }

    private void scheduleAiTurn() {
        disableBoard();

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> executeAiMove());
        pause.play();
    }

    private void executeAiMove() {
        Move aiMove = gameService.getNextMove(gameBoard, GameBoard.PLAYER_O);

        if (aiMove != null) {
            performMove(aiMove.getRow(), aiMove.getCol(), GameBoard.PLAYER_O);
        }

        if (!checkGameEnd()) {

            updateTurnIndicator();
            enableBoard();
        }
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
        char currentPlayer = gameBoard.getCurrentPlayer();

        switch (gameMode) {

            case ONLINE_PVP -> {
                if (currentPlayer == localPlayerId) {
                    setActiveCard(playerCard, opponentCard);
                } else {
                    setActiveCard(opponentCard, playerCard);
                }
            }

            case OFFLINE_PVP, OFFLINE_PVE, REPLAY -> {
                if (currentPlayer == GameBoard.PLAYER_X) {
                    setActiveCard(playerCard, opponentCard);
                } else {
                    setActiveCard(opponentCard, playerCard);
                }
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

        char winnerChar = GameBoard.EMPTY;
        if (state == GameState.X_WINS) {
            winnerChar = GameBoard.PLAYER_X;
        } else if (state == GameState.O_WINS) {
            winnerChar = GameBoard.PLAYER_O;
        }

        if (gameMode != GameMode.REPLAY) {
            if (winnerChar == GameBoard.PLAYER_X) {
                playerScore++;
                playerScoreLabel.setText(String.valueOf(playerScore));
            } else if (winnerChar == GameBoard.PLAYER_O) {
                opponentScore++;
                opponentScoreLabel.setText(String.valueOf(opponentScore));
            }
        }

        String videoToPlay = "draw";

        if (state == GameState.DRAW) {
            {
                videoToPlay = "draw";
                if (gameMode == GameMode.ONLINE_PVP) {
                    OnlinePVPService.getInstance().sendGameEnd(null);
                }
            }

        } else if (gameMode == GameMode.ONLINE_PVP) {
            boolean amIWinner = (winnerChar == localPlayerId);
            videoToPlay = amIWinner ? "victory" : "defeat";

            String winnerId = "DRAW";
            if (state == GameState.X_WINS) {
                winnerId = OnlineGameState.info.getPlayer1();
            } else if (state == GameState.O_WINS) {
                winnerId = OnlineGameState.info.getPlayer2();
            } else {
                winnerId = null;
            }
            System.out.println("sending sendgin " + winnerId);
            OnlinePVPService.getInstance().sendGameEnd(winnerId);
        } else {
            videoToPlay = (winnerChar == GameBoard.PLAYER_X) ? "victory" : "defeat";
        }

        playVictoryVideo(videoToPlay);
        showGameOverModal(state);
//        PauseTransition pause = new PauseTransition(Duration.millis(800));
//        pause.setOnFinished(e -> showGameOverModal(state));
//        pause.play();
    }

    private void showGameOverModal(GameState state) {
        String title = "";
        String message = "";
        String icon = "";

        if (state == GameState.DRAW) {
            title = "Draw!";
            message = "It's a tie! Both players played well.";
        } else {
            char winner = (state == GameState.X_WINS) ? GameBoard.PLAYER_X : GameBoard.PLAYER_O;

            if (gameMode == GameMode.ONLINE_PVP) {
                if (winner == localPlayerId) {
                    title = "Victory!";
                    message = "You won the match!";
                } else {
                    title = "Defeat!";
                    message = "Better luck next time.";
                }
            } else {
                if (winner == GameBoard.PLAYER_X) {
                    title = "Victory!";
                    message = (gameMode == GameMode.OFFLINE_PVE)
                            ? "The CPU didn't stand a chance."
                            : "Player 1 wins!";
                } else {
                    title = (gameMode == GameMode.OFFLINE_PVE) ? "Defeat!" : "Player 2 Wins!";
                    message = (gameMode == GameMode.OFFLINE_PVE)
                            ? "The CPU outsmarted you."
                            : "Player 2 takes the round.";
                }
            }
        }

        modalIcon.setText(icon);
        modalTitle.setText(title);
        modalMessage.setText(message);

        if (saveGameButton != null) {
            saveGameButton.setText("Save Replay");
            saveGameButton.setDisable(false);
            boolean canSave = (gameMode == GameMode.OFFLINE_PVE || gameMode == GameMode.OFFLINE_PVP);
            saveGameButton.setVisible(canSave);
            saveGameButton.setManaged(canSave);
        }

        if (gameMode == GameMode.REPLAY || gameMode == GameMode.ONLINE_PVP) {
            playAgainButton.setVisible(false);
            playAgainButton.setManaged(false);
        } else {
            playAgainButton.setVisible(true);
            playAgainButton.setManaged(true);
        }

        modalOverlay.setVisible(true);
    }

    private void playVictoryVideo(String videoName) {
        try {
            String videoPath = Objects.requireNonNull(
                    getClass().getResource("/assets/videos/" + videoName + ".mp4")
            ).toExternalForm();

            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                currentMediaPlayer.dispose();
            }

            Media media = new Media(videoPath);
            currentMediaPlayer = new MediaPlayer(media);

            victoryVideo.setMediaPlayer(currentMediaPlayer);
            currentMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            currentMediaPlayer.setAutoPlay(true);
            victoryVideo.setVisible(true);

        } catch (Exception e) {
            System.out.println("Video error: " + e.getMessage());
            e.printStackTrace();
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

        if (currentMediaPlayer != null) {
            currentMediaPlayer.stop();
            currentMediaPlayer.dispose();
            currentMediaPlayer = null;
        }

        victoryVideo.setVisible(false);
        victoryVideo.setMediaPlayer(null);

        updateTurnIndicator();

        if (gameMode == GameMode.ONLINE_PVP) {
            if (localPlayerId == GameBoard.PLAYER_X) {
                enableBoard();
            } else {
                disableBoard();
            }
        } else {
            enableBoard();
        }
    }

    private void disableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard.getCellValue(row, col) == GameBoard.EMPTY) {
                    cells[row][col].setDisable(true);
                }
            }
        }
    }

    private void enableBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (gameBoard.getCellValue(row, col) == GameBoard.EMPTY) {
                    cells[row][col].setDisable(false);
                }
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
}
