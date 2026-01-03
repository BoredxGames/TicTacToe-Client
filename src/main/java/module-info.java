module com.boredxgames.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.boredxgames.tictactoeclient to javafx.fxml;
    exports com.boredxgames.tictactoeclient;
    exports com.boredxgames.tictactoeclient.presentation;
    opens com.boredxgames.tictactoeclient.presentation to javafx.fxml;
    exports com.boredxgames.tictactoeclient.domain.managers.state;
    exports com.boredxgames.tictactoeclient.domain.managers.state.records;
}
