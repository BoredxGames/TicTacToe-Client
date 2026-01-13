module com.boredxgames.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.prefs;
    requires org.json;
    requires com.google.gson;
requires java.desktop;
    requires javafx.media;
    opens com.boredxgames.tictactoeclient.domain.managers.audio to javafx.fxml;
    exports com.boredxgames.tictactoeclient;
    exports com.boredxgames.tictactoeclient.presentation;
    exports com.boredxgames.tictactoeclient.domain.managers.state;
    exports com.boredxgames.tictactoeclient.domain.managers.state.records;

    opens com.boredxgames.tictactoeclient.presentation to javafx.fxml;
    opens com.boredxgames.tictactoeclient to com.google.gson, javafx.fxml;
    opens com.boredxgames.tictactoeclient.domain.model to com.google.gson;
    opens com.boredxgames.tictactoeclient.domain.services.communication to com.google.gson;

}
