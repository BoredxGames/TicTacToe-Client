module com.boredxgames.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.boredxgames.tictactoeclient to javafx.fxml;
    exports com.boredxgames.tictactoeclient;
}
