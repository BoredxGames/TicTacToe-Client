package com.boredxgames.tictactoeclient.presentation;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Hazem
 */


import java.util.Random;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public abstract class BackgroundHomeAnimation{

    private static final Random random = new Random();

    /**
     * Animates a container sliding up from the bottom and fading in.
     * @param node The container (e.g., VBox) to animate.
     */
    public static void animateCardEntry(Node node) {
//        node.setOpacity(0);
//        node.setTranslateY(500);
//
//        TranslateTransition tt = new TranslateTransition(Duration.millis(1500), node);
//        tt.setToY(0);
//
//        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
//        ft.setToValue(1);
//
//        ParallelTransition pt = new ParallelTransition(tt, ft);
//        pt.play();
    }

    
    public static void startBackgroundAnimation(Pane backgroundPane, double screenWidth, double screenHeight) {
//        int particleCount = 50;
//
//        for (int i = 0; i < particleCount; i++) {
//            Label particle = new Label(random.nextBoolean() ? "X" : "O");
//            particle.getStyleClass().add("background-particle");
//
//            double size = 15 + random.nextInt(50);
//            particle.setStyle("-fx-font-size: " + size + "px; -fx-text-fill: rgba(79, 94, 247, " + (0.05 + random.nextDouble() * 0.2) + ");");
//
//            particle.setTranslateX(random.nextInt((int) screenWidth));
//            particle.setTranslateY(random.nextInt((int) screenHeight));
//
//            backgroundPane.getChildren().add(particle);
//            animateParticle(particle, screenWidth, screenHeight);
//        }
    }

    private static void animateParticle(Label particle, double width, double height) {
//        TranslateTransition move = new TranslateTransition(Duration.seconds(15 + random.nextInt(15)), particle);
//        double endX = particle.getTranslateX() + (random.nextInt(200) - 100); // Drift sideways
//        double endY = -100; 
//
//        move.setToX(endX);
//        move.setToY(endY);
//        move.setCycleCount(1);
//
//        RotateTransition rotate = new RotateTransition(Duration.seconds(5 + random.nextInt(10)), particle);
//        rotate.setByAngle(360);
//        rotate.setCycleCount(RotateTransition.INDEFINITE);
//
//        move.setOnFinished(e -> {
//           
//            particle.setTranslateY(height + random.nextInt(100));
//            particle.setTranslateX(random.nextInt((int) width));
//            animateParticle(particle, width, height);
//        });
//
//        move.play();
//        rotate.play();
    }
}