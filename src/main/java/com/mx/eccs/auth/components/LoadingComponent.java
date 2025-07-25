package com.mx.eccs.auth.components;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LoadingComponent extends StackPane {
    private Label messageLabel;
    private ProgressBar progressBar;
    private Timeline timeline;
    private Runnable onComplete;

    public LoadingComponent() {
        initializeComponents();
        setupLayout();
        startAnimation();
    }

    private void initializeComponents() {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(25);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 50; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);");
        card.setPrefWidth(450);
        card.setPrefHeight(300);
        
        Label companyLabel = new Label("ECCS");
        companyLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #4169E1; -fx-effect: dropshadow(gaussian, rgba(65,105,225,0.5), 8, 0, 2, 2);");
        
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);
        progressBar.setPrefHeight(12);
        progressBar.setStyle("-fx-accent: #4169E1; -fx-background-color: #E5E5E5;");
        
        messageLabel = new Label("ECCS: Inicializando sistema...");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: 400;");
        
        card.getChildren().addAll(companyLabel, progressBar, messageLabel);
        getChildren().add(card);
    }

    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #2F2F2F;");
    }

    private void startAnimation() {
        String[] messages = {
            "ECCS: Inicializando sistema...",
            "ECCS: Cargando módulos...",
            "ECCS: Configurando seguridad...",
            "ECCS: Preparando interfaz...",
            "ECCS: Finalizando..."
        };
        
        timeline = new Timeline();
        
        for (int i = 0; i <= 20; i++) {
            final double progress = i / 20.0;
            final int messageIndex = Math.min(i / 4, messages.length - 1);
            
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i * 0.1), e -> {
                progressBar.setProgress(progress);
                messageLabel.setText(messages[messageIndex]);
            }));
        }
        
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2.0), e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                if (onComplete != null) onComplete.run();
            });
            fadeOut.play();
        }));
        
        timeline.play();
    }

    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
}