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
import javafx.fxml.Initializable;

public class PrimaryController implements Initializable, StatefulController {
    private PrimaryScreenRecord primaryScreenState;

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
        ThemeManager.setTheme(Theme.PURPLE);
    }
    public void useGreen() {
        ThemeManager.setTheme(Theme.GREEN);
    }
    public void useRed() { ThemeManager.setTheme(Theme.RED); }


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
