package com.boredxgames.tictactoeclient;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static ResourceBundle currentBundle;

    @Override
    public void start(Stage stage) throws IOException {
        Locale currentLocale = Locale.of("ar", "EG");

      
        currentBundle = ResourceBundle.getBundle("localization.message", currentLocale);

        scene = new Scene(loadFXML("presentation/primary"), 640, 480);
        
        scene.getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        stage.setScene(scene);
        stage.show();
    }
    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        
        fxmlLoader.setResources(currentBundle);
        
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}