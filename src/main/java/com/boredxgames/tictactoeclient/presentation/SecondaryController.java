package com.boredxgames.tictactoeclient.presentation;

import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationAction;
import com.boredxgames.tictactoeclient.domain.managers.navigation.NavigationManager;
import com.boredxgames.tictactoeclient.domain.managers.navigation.Screens;
import com.boredxgames.tictactoeclient.domain.managers.state.StatefulController;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable, StatefulController {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public Object saveState() {
        return null;
    }

    @Override
    public void restoreState(Object state) {

    }

    public void back() {
        NavigationManager.pop();
    }

    public void replace() {
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE);
    }

    public void clear() {
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.REPLACE_ALL);
    }

    public void pushPrimary() {
        NavigationManager.navigate(Screens.PRIMARY, NavigationAction.PUSH);
    }


}
