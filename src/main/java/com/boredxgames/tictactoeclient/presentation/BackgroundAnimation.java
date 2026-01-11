package com.boredxgames.tictactoeclient.presentation;

import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BackgroundAnimation {

    private static final Random random = new Random();
    private static final int STAR_COUNT = 50;
    private static final double MAX_SCALE = 6;
    private static final double MIN_SCALE = 0.1;
    private static final double GLOW_LEVEL = 0.5;

    public static void animateCardEntry(Node view) {
        view.setOpacity(0);
        view.setScaleX(0.5);
        view.setScaleY(0.5);

        FadeTransition fade = new FadeTransition(Duration.millis(1000), view);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(1000), view);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition entrance = new ParallelTransition(fade, scale);
        entrance.play();
    }

    public static void startWarpAnimation(Pane container, double width, double height) {
        container.getChildren().clear();

        double centerX = width / 2;
        double centerY = height / 2;

        for (int i = 0; i < STAR_COUNT; i++) {
            Label star = createNeonStar();

            star.setTranslateX(centerX);
            star.setTranslateY(centerY);

            container.getChildren().add(star);

            Duration initialDelay = Duration.millis(random.nextInt(2000));
            launchStar(star, centerX, centerY, width, height, initialDelay);
        }
    }

    private static Label createNeonStar() {
        boolean isX = random.nextBoolean();
        String symbol = isX ? "X" : "O";
        Label star = new Label(symbol);

        String neonColor = isX ? "#ff00ff" : "#00ffff";

        star.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: " + neonColor + ";"
                + "-fx-font-size: 18px;"
        );

        star.setMouseTransparent(true);
        star.setEffect(new Glow(GLOW_LEVEL));

        return star;
    }

    private static void launchStar(Node star, double startX, double startY, double boundsW, double boundsH, Duration delay) {
        star.setTranslateX(startX);
        star.setTranslateY(startY);
        star.setScaleX(MIN_SCALE);
        star.setScaleY(MIN_SCALE);
        star.setOpacity(0);

        double angle = random.nextDouble() * 360;
        double distance = Math.max(boundsW, boundsH);

        double targetX = startX + Math.cos(Math.toRadians(angle)) * distance;
        double targetY = startY + Math.sin(Math.toRadians(angle)) * distance;

        double speed = random.nextDouble(10) + 0.5;

        TranslateTransition move = new TranslateTransition(Duration.seconds(speed), star);
        move.setFromX(startX);
        move.setFromY(startY);
        move.setToX(targetX);
        move.setToY(targetY);
        move.setInterpolator(Interpolator.EASE_IN);

        ScaleTransition grow = new ScaleTransition(Duration.seconds(speed), star);
        grow.setToX(MAX_SCALE);
        grow.setToY(MAX_SCALE);
        grow.setInterpolator(Interpolator.EASE_IN);

        RotateTransition spin = new RotateTransition(Duration.seconds(speed), star);
        spin.setByAngle(random.nextBoolean() ? 360 : -360);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(speed * 0.2), star);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition flight = new ParallelTransition(move, grow, spin, fadeIn);
        flight.setDelay(delay);

        flight.setOnFinished(event -> launchStar(star, startX, startY, boundsW, boundsH, Duration.ZERO));
        flight.play();
    }
}
