package com.mx.eccs.arieserp;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AriesERPDashboard extends BorderPane {
    private Runnable onBack;
    private Runnable onLogout;
    private Label clockLabel;

    public AriesERPDashboard() {
        initializeComponents();
        setupLayout();
        startClock();
    }
    
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_RIGHT);
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setStyle("-fx-background-color: #2c3e50;");
        
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: 500; -fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand;");
        logoutButton.setOnAction(e -> { if (onLogout != null) onLogout.run(); });
        
        navbar.getChildren().add(logoutButton);
        return navbar;
    }

    private void initializeComponents() {
        // Navbar
        HBox navbar = createNavbar();
        setTop(navbar);
        
        // Main content
        StackPane mainContent = new StackPane();
        
        clockLabel = new Label();
        clockLabel.setStyle("-fx-font-size: 120px; -fx-font-weight: bold; -fx-text-fill: rgba(44, 62, 80, 0.1); -fx-font-family: 'Arial';");
        
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        
        Label titleLabel = new Label("AriesERP");
        titleLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Dashboard Ejecutivo Empresarial");
        subtitleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #34495e;");
        
        Label statusLabel = new Label("Sistema Activo - Todos los módulos operativos");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
        
        content.getChildren().addAll(titleLabel, subtitleLabel, statusLabel);
        
        mainContent.getChildren().addAll(clockLabel, content);
        StackPane.setAlignment(clockLabel, Pos.CENTER);
        StackPane.setAlignment(content, Pos.CENTER);
        
        setCenter(mainContent);
    }

    private void setupLayout() {
        setStyle("-fx-background-color: #ecf0f1;");
        setPadding(new Insets(20));
    }

    private void startClock() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        updateClock();
    }

    private void updateClock() {
        LocalTime now = LocalTime.now();
        clockLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
    
    public void setOnLogout(Runnable onLogout) {
        this.onLogout = onLogout;
    }
}