package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.managers.state.StatefulController;
import com.boredxgames.tictactoeclient.domain.managers.state.records.PrimaryScreenRecord;
import com.boredxgames.tictactoeclient.domain.managers.theme.Theme;
import com.boredxgames.tictactoeclient.domain.managers.theme.ThemeManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController implements Initializable, StatefulController {
    private PrimaryScreenRecord primaryScreenState;
    @FXML
    private TextField serverIpField;
    @FXML
    private Button connectButton;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        primaryScreenState = new PrimaryScreenRecord(0);
    }
    @Override
    public Object saveState() {
        return primaryScreenState;
    }
    @Override
    public void restoreState(Object state) {
        primaryScreenState = (PrimaryScreenRecord) state;
    }


    public void usePurple() {
        ThemeManager.setTheme(Theme.CYBER_PINK);
    }
    public void useGreen() {
        ThemeManager.setTheme(Theme.CYBER_PINK);
    }
    public void useRed() { ThemeManager.setTheme(Theme.TOXIC_GREEN); }


    public void pop() {
        NavigationManager.pop();
    }
    public void pushSecondary() {
        NavigationManager.navigate(Screens.SERVER_CONNECTION, NavigationAction.REPLACE);
    }


    public void incrementCounter() {
        primaryScreenState = new PrimaryScreenRecord(primaryScreenState.number() + 1);
        System.out.println("Incremented: " + primaryScreenState.number());
    }
    public void decrementCounter() {
        primaryScreenState = new PrimaryScreenRecord(primaryScreenState.number() - 1);
        System.out.println("Decremented: " + primaryScreenState.number());
    }


}
