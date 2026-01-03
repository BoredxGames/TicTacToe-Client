package com.boredxgames.tictactoeclient.domain.managers.state;

public interface StatefulController {
    Object saveState();
    void restoreState(Object state);
}
