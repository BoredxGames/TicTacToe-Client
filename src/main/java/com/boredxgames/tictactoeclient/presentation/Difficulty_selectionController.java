/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.boredxgames.tictactoeclient.presentation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.layout.Pane;
/**
 * FXML Controller class
 *
 * @author mahmoud
 */
public class Difficulty_selectionController implements Initializable {


    @FXML
    private Pane backgroundPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backgroundPane.getChildren().clear();


        Platform.runLater(() -> {
            BackgroundAnimation.startWarpAnimation(
                backgroundPane,
                backgroundPane.getWidth(),
                backgroundPane.getHeight()
            );
        });
    }    
    
}
