package com.mx.eccs.auth.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginComponent extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label errorLabel;
    private Runnable onLoginSuccess;

    public LoginComponent() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        // Header con logo
        VBox headerSection = new VBox(15);
        headerSection.setAlignment(Pos.CENTER);
        
        // Logo
        try {
            javafx.scene.image.ImageView logoView = new javafx.scene.image.ImageView();
            javafx.scene.image.Image logoImage = new javafx.scene.image.Image(getClass().getResourceAsStream("/logo.png"));
            logoView.setImage(logoImage);
            logoView.setFitWidth(80);
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
            headerSection.getChildren().add(logoView);
        } catch (Exception e) {
            System.out.println("[DEBUG] Logo no encontrado en login: " + e.getMessage());
        }
        
        Label titleLabel = new Label("ECCS");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 400; -fx-text-fill: #1A202C; -fx-letter-spacing: 2px;");
        
        Label subtitleLabel = new Label("Sistema de Certificados");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096; -fx-font-weight: 400;");
        
        headerSection.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Sección de campos
        VBox fieldsSection = new VBox(20);
        fieldsSection.setAlignment(Pos.CENTER);
        
        // Campo usuario
        VBox userSection = new VBox(8);
        userSection.setAlignment(Pos.CENTER_LEFT);
        
        Label userLabel = new Label("Usuario");
        userLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4A5568; -fx-font-weight: 500;");
        
        HBox userContainer = new HBox(12);
        userContainer.setAlignment(Pos.CENTER_LEFT);
        userContainer.setPrefWidth(300);
        userContainer.setPrefHeight(45);
        userContainer.setStyle("-fx-background-color: #F7FAFC; -fx-border-color: #E2E8F0; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 15;");
        
        Text userIcon = new Text("👤");
        userIcon.setStyle("-fx-font-size: 16px;");
        
        usernameField = new TextField();
        usernameField.setPromptText("Ingrese su usuario");
        usernameField.setPrefWidth(250);
        usernameField.setStyle("-fx-font-size: 14px; -fx-background-color: transparent; -fx-text-fill: #2D3748; -fx-prompt-text-fill: #A0AEC0; -fx-border-width: 0;");
        
        userContainer.getChildren().addAll(userIcon, usernameField);
        userSection.getChildren().addAll(userLabel, userContainer);
        
        // Campo contraseña
        VBox passSection = new VBox(8);
        passSection.setAlignment(Pos.CENTER_LEFT);
        
        Label passLabel = new Label("Contraseña");
        passLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4A5568; -fx-font-weight: 500;");
        
        HBox passContainer = new HBox(12);
        passContainer.setAlignment(Pos.CENTER_LEFT);
        passContainer.setPrefWidth(300);
        passContainer.setPrefHeight(45);
        passContainer.setStyle("-fx-background-color: #F7FAFC; -fx-border-color: #E2E8F0; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 15;");
        
        Text passIcon = new Text("🔒");
        passIcon.setStyle("-fx-font-size: 16px;");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Ingrese su contraseña");
        passwordField.setPrefWidth(250);
        passwordField.setStyle("-fx-font-size: 14px; -fx-background-color: transparent; -fx-text-fill: #2D3748; -fx-prompt-text-fill: #A0AEC0; -fx-border-width: 0;");
        
        passContainer.getChildren().addAll(passIcon, passwordField);
        passSection.getChildren().addAll(passLabel, passContainer);
        
        fieldsSection.getChildren().addAll(userSection, passSection);
        
        // Botón de login
        loginButton = new Button("Iniciar Sesión");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(50);
        loginButton.setStyle("-fx-background-color: #2B6CB0; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: 600; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;");
        
        // Efectos hover
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle("-fx-background-color: #2C5282; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: 600; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;");
        });
        
        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle("-fx-background-color: #2B6CB0; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: 600; -fx-background-radius: 8; -fx-cursor: hand; -fx-border-width: 0;");
        });
        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#E53E3E"));
        errorLabel.setVisible(false);
        errorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-alignment: center;");
        
        getChildren().addAll(headerSection, fieldsSection, loginButton, errorLabel);
    }

    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setSpacing(30);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 25, 0, 0, 10);");
        setMaxWidth(400);
        setPrefHeight(500);
    }

    private void setupEventHandlers() {
        usernameField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 10) {
                usernameField.setText(oldText);
            } else if (!newText.matches("[a-zA-Z0-9]*")) {
                usernameField.setText(newText.replaceAll("[^a-zA-Z0-9]", ""));
            }
        });
        
        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() > 10) {
                passwordField.setText(oldText);
            } else if (!newText.matches("[a-zA-Z0-9]*")) {
                passwordField.setText(newText.replaceAll("[^a-zA-Z0-9]", ""));
            }
        });
        
        loginButton.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠️ Complete todos los campos");
            errorLabel.setVisible(true);
            return;
        }
        
        if ("eccs".equals(username) && "1234".equals(password)) {
            errorLabel.setVisible(false);
            if (onLoginSuccess != null) {
                onLoginSuccess.run();
            }
        } else {
            errorLabel.setText("❌ Credenciales incorrectas");
            errorLabel.setVisible(true);
        }
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
}