package com.mx.eccs.auth.components;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AppSelectorComponent extends VBox {
    private Runnable onAriesERPSelected;
    private Runnable onScorpioXLSelected;

    public AppSelectorComponent() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Header moderno
        VBox headerSection = new VBox(12);
        headerSection.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("ECCS");
        titleLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: 200; -fx-text-fill: #1A202C; -fx-letter-spacing: 3px;");
        
        Label subtitleLabel = new Label("Selecciona tu aplicación");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #4A5568; -fx-font-weight: 400;");
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Cards rediseñados
        VBox ariesCard = createAppCard("AriesERP", "Sistema ERP Empresarial", "🏢", "#2B6CB0", () -> {
            if (onAriesERPSelected != null) onAriesERPSelected.run();
        });
        
        VBox scorpioCard = createAppCard("Scorpio XL", "Descarga Masiva XML", "📈", "#D69E2E", () -> {
            if (onScorpioXLSelected != null) onScorpioXLSelected.run();
        });
        
        HBox cardsContainer = new HBox(30);
        cardsContainer.setAlignment(Pos.CENTER);
        cardsContainer.getChildren().addAll(ariesCard, scorpioCard);
        
        getChildren().addAll(headerSection, cardsContainer);
    }

    private VBox createAppCard(String title, String description, String icon, String color, Runnable onAction) {
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 25, 0, 0, 10); -fx-cursor: hand;");
        card.setPrefWidth(280);
        card.setPrefHeight(200);
        
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 52px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: 500; -fx-text-fill: " + color + ";");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(240);
        
        card.getChildren().addAll(iconText, titleLabel, descLabel);
        
        // Animaciones hover
        card.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
            scaleUp.setToX(1.05);
            scaleUp.setToY(1.05);
            scaleUp.setInterpolator(Interpolator.EASE_OUT);
            scaleUp.play();
            
            card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 35, 0, 0, 15); -fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);
            scaleDown.setInterpolator(Interpolator.EASE_OUT);
            scaleDown.play();
            
            card.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 25, 0, 0, 10); -fx-cursor: hand;");
        });
        
        card.setOnMouseClicked(e -> {
            ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), card);
            clickScale.setToX(0.95);
            clickScale.setToY(0.95);
            clickScale.setAutoReverse(true);
            clickScale.setCycleCount(2);
            clickScale.setOnFinished(event -> onAction.run());
            clickScale.play();
        });
        
        return card;
    }

    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setSpacing(40);
        setPadding(new Insets(50));
        setStyle("-fx-background-color: #F7FAFC;");
    }

    public void setOnAriesERPSelected(Runnable onAriesERPSelected) {
        this.onAriesERPSelected = onAriesERPSelected;
    }

    public void setOnScorpioXLSelected(Runnable onScorpioXLSelected) {
        this.onScorpioXLSelected = onScorpioXLSelected;
    }
}