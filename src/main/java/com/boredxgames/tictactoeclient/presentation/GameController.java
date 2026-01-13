package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationParameterAware;
import com.boredxgames.tictactoeclient.domain.model.GameMode;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.model.GameNavigationParams;
import com.boredxgames.tictactoeclient.domain.model.Move;
import com.boredxgames.tictactoeclient.domain.services.game.GameBoard;
import com.boredxgames.tictactoeclient.domain.model.GameState;
import com.boredxgames.tictactoeclient.domain.services.game.GameService;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVEAIService;
import com.boredxgames.tictactoeclient.domain.services.game.OfflinePVPService;
import javafx.animation.PauseTransition;
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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private Button mainMenuButton;

    private GameBoard gameBoard;
    private GameMode gameMode;
    private int playerScore = 0;
    private int opponentScore = 0;
    private boolean isPlayerTurn = true;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    private GameService gameService;

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
    }

    @Override
    public void setNavigationParameter(Object parameter) {
        if (parameter instanceof GameNavigationParams params) {
            this.player1Name = params.player1();
            this.player2Name = params.player2();
            this.gameMode = params.mode();
        } else {
            this.gameMode = GameMode.OFFLINE_PVP;
        }

        applyPlayerInfo();
        applyGameModeSettings();
        resetGame();
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
            NavigationManager.navigate(Screens.GAME_MODE, NavigationAction.REPLACE_ALL);
        });

        backButton.setOnAction(e -> {
            NavigationManager.navigate(Screens.GAME_MODE, NavigationAction.REPLACE_ALL);
        });

        settingsButton.setOnAction(e -> {

            NavigationManager.navigate(Screens.SETTINGS, NavigationAction.REPLACE_ALL);
        });
    }

    private void handleCellClick(int row, int col) {
        if (!gameBoard.isValidMove(row, col)) {
            return;
        }

        char currentPlayer = gameBoard.getCurrentPlayer();

        switch (gameMode) {
            case OFFLINE_PVP -> {
                // Player vs Player

                gameBoard.makeMove(row, col, currentPlayer);
                updateCell(row, col, currentPlayer);

                if (!checkGameEnd()) {

                    updateTurnIndicator();
                }
            }

            case OFFLINE_PVE -> {
                // Player X moves
                gameBoard.makeMove(row, col, GameBoard.PLAYER_X);
                updateCell(row, col, GameBoard.PLAYER_X);
                if (checkGameEnd()) {
                    return;
                }

                // AI O moves
                disableBoard();
                PauseTransition pause = new PauseTransition(Duration.millis(500)); // AI "thinking"
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

            case ONLINE_PVP -> {
                // Player sends move to server instead of executing locally

                //   sendMoveToServer(row, col);
            }
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

        Text symbol = new Text();
        symbol.getStyleClass().add("material-icon");

        String iconPath;
        if (player == GameBoard.PLAYER_X) {
            iconPath = "/assets/icons/close.png";
        } else {
            iconPath = "/assets/icons/circle.png";
        }

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

        if (gameMode == GameMode.OFFLINE_PVP) {
            if (isPlayerX) {
                setActiveCard(playerCard, opponentCard);
            } else {
                setActiveCard(opponentCard, playerCard);
            }
        } else if (gameMode == GameMode.OFFLINE_PVE) {
            if (isPlayerX) {
                setActiveCard(playerCard, opponentCard);
            } else {
                setActiveCard(opponentCard, playerCard);
            }
        } else {
            if (isPlayerTurn) {
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

        switch (state) {
            case X_WINS -> {
                playerScore++;
                playerScoreLabel.setText(String.valueOf(playerScore));
                playVictoryVideo("you_win");
            }
            case O_WINS -> {
                opponentScore++;
                opponentScoreLabel.setText(String.valueOf(opponentScore));
                playVictoryVideo("loser");
            }
            case DRAW -> playVictoryVideo("handshake");
        }

        PauseTransition pause = new PauseTransition(Duration.millis(800));
        pause.setOnFinished(e -> showGameOverModal(state));
        pause.play();
    }

    private void showGameOverModal(GameState state) {
        switch (state) {
            case X_WINS -> {
                modalTitle.setText("Victory!");
                modalMessage.setText(player1Name + " wins!");
            }
            case O_WINS -> {
                modalTitle.setText("Defeat!");
                modalMessage.setText(player2Name + " wins!");
            }
            case DRAW -> {
                modalTitle.setText("Draw!");
                modalMessage.setText("Both players played well.");
            }
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
        }
    }

    public void resetGame() {
        gameBoard.resetGame();

        for (Button[] row : cells) {
            for (Button cell : row) {
                cell.setGraphic(null);
                cell.setDisable(false);
                cell.getStyleClass().remove("cell-winning");
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
        enableBoard();
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
}
