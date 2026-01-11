package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.entity.GameMode;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.services.GameBoard;
import com.boredxgames.tictactoeclient.domain.services.GameBoard.GameState;
import javafx.animation.PauseTransition;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Tasneem
 */
public class GameController implements Initializable {

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
    private Text opponentIcon;
    @FXML
    private HBox turnMessage;
    @FXML
    private Label turnLabel;

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
    private Button playAgainButton;
    @FXML
    private Button changeDifficultyButton;
    @FXML
    private Button mainMenuButton;

    private GameBoard gameBoard;
    private GameMode gameMode;
    private int playerScore = 0;
    private int opponentScore = 0;
    private final boolean isPlayerTurn = true;


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
        setGameMode(GameMode.OFFLINE_PVP);
        resetGame();
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;

        switch (mode) {
            case OFFLINE_PVP:
                opponentNameLabel.setText("Player 2 (O)");
                opponentTypeLabel.setText("PLAYER 2");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                changeDifficultyButton.setVisible(false);
                changeDifficultyButton.setManaged(false);
                break;

            case OFFLINE_PVE:
                opponentNameLabel.setText("CPU (O)");
                opponentTypeLabel.setText("OPPONENT");
                difficultyBadge.setVisible(true);
                difficultyBadge.setManaged(true);
                changeDifficultyButton.setVisible(true);
                changeDifficultyButton.setManaged(true);
                break;

            case ONLINE_PVP:
                opponentNameLabel.setText("Opponent (O)");
                opponentTypeLabel.setText("ONLINE PLAYER");
                opponentIcon.setText("public");
                difficultyBadge.setVisible(false);
                difficultyBadge.setManaged(false);
                changeDifficultyButton.setVisible(false);
                changeDifficultyButton.setManaged(false);
                break;
        }
    }

    private void setupCellHandlers() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row;
                final int c = col;

                cells[row][col].setOnAction(event -> handleCellClick(r, c));
            }
        }
    }

    private void setupButtonHandlers() {
        playAgainButton.setOnAction(e -> resetGame());

        mainMenuButton.setOnAction(e -> {
            NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE_ALL);
        });

        backButton.setOnAction(e -> {
            NavigationManager.pop();
        });

        settingsButton.setOnAction(e -> {

            NavigationManager.navigate(Screens.SETTINGS, NavigationAction.REPLACE_ALL);
        });

        changeDifficultyButton.setOnAction(e -> {
            // TODO change difficulty
        });
    }

    private void handleCellClick(int row, int col) {
        if (!gameBoard.isValidMove(row, col)) {
            return;
        }

        if (gameMode == GameMode.ONLINE_PVP && !isPlayerTurn) {
            return;
        }

        char currentPlayer = gameBoard.getCurrentPlayer();
        if (gameBoard.makeMove(row, col, currentPlayer)) {
            updateCell(row, col, currentPlayer);

            if (gameBoard.getGameState() != GameState.IN_PROGRESS) {
                handleGameEnd();
            } else {
                gameBoard.switchPlayer();
                updateTurnIndicator();

                if (gameMode == GameMode.OFFLINE_PVE && gameBoard.getCurrentPlayer() == GameBoard.PLAYER_O) {
                    disableBoard();
                    PauseTransition pause = new PauseTransition(Duration.millis(500));
                    pause.setOnFinished(e -> makeCPUMove());
                    pause.play();
                }
            }
        }
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

    private void makeCPUMove() {
        var availableMoves = gameBoard.getAvailableMoves();

        if (!availableMoves.isEmpty()) {
            // TODO: change difficulty
            int randomIndex = (int) (Math.random() * availableMoves.size());
            int[] move = availableMoves.get(randomIndex);

            Platform.runLater(() -> {
                if (gameBoard.makeMove(move[0], move[1], GameBoard.PLAYER_O)) {
                    updateCell(move[0], move[1], GameBoard.PLAYER_O);

                    if (gameBoard.getGameState() != GameState.IN_PROGRESS) {
                        handleGameEnd();
                    } else {
                        gameBoard.switchPlayer();
                        updateTurnIndicator();
                        enableBoard();
                    }
                }
            });
        }
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
            playerScore++;
            playerScoreLabel.setText(String.valueOf(playerScore));
        } else if (state == GameState.O_WINS) {
            opponentScore++;
            opponentScoreLabel.setText(String.valueOf(opponentScore));
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
                modalMessage.setText(gameMode == GameMode.OFFLINE_PVE ?
                        "The CPU didn't stand a chance against your moves." :
                        "Player 1 wins the game!");
                break;

            case O_WINS:
                modalIcon.setText("sentiment_dissatisfied");
                modalTitle.setText("Defeat!");
                modalMessage.setText(gameMode == GameMode.OFFLINE_PVE ?
                        "The CPU outsmarted you this time." :
                        "Player 2 wins the game!");
                break;

            case DRAW:
                modalIcon.setText("handshake");
                modalTitle.setText("Draw!");
                modalMessage.setText("It's a tie! Both players played well.");
                break;
        }

        modalOverlay.setVisible(true);
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

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}