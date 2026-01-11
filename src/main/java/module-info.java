module com.boredxgames.tictactoeclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.base;
    requires java.prefs;

    
    opens com.boredxgames.tictactoeclient.domain.managers.audio to javafx.fxml;
    requires org.json;
    requires com.google.gson;
    requires java.base;
    opens com.boredxgames.tictactoeclient to javafx.fxml;
    opens com.boredxgames.tictactoeclient.domain.services.communication to com.google.gson;
    opens com.boredxgames.tictactoeclient.domain.model to com.google.gson;
    exports com.boredxgames.tictactoeclient;
    exports com.boredxgames.tictactoeclient.presentation;
    opens com.boredxgames.tictactoeclient.presentation to javafx.fxml;
    exports com.boredxgames.tictactoeclient.domain.managers.state;
    exports com.boredxgames.tictactoeclient.domain.managers.state.records;
    
}
