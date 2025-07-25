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
        // Título minimalista
        Label titleLabel = new Label("ECCS");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: 300; -fx-text-fill: #1A202C; -fx-letter-spacing: 2px;");
        
        Label subtitleLabel = new Label("Acceso al sistema");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #718096; -fx-font-weight: 400;");
        
        // Campo usuario con icono
        HBox userContainer = new HBox();
        userContainer.setAlignment(Pos.CENTER_LEFT);
        userContainer.setSpacing(12);
        userContainer.setPrefWidth(280);
        userContainer.setStyle("-fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0; -fx-padding: 8 0 8 0;");
        
        Text userIcon = new Text("👤");
        userIcon.setStyle("-fx-font-size: 16px;");
        
        usernameField = new TextField();
        usernameField.setPromptText("Usuario");
        usernameField.setPrefWidth(240);
        usernameField.setPrefHeight(32);
        usernameField.setStyle("-fx-font-size: 15px; -fx-background-color: transparent; -fx-text-fill: #2D3748; -fx-prompt-text-fill: #A0AEC0; -fx-border-width: 0;");
        
        userContainer.getChildren().addAll(userIcon, usernameField);
        
        Tooltip userTooltip = new Tooltip("Ingrese su nombre de usuario corporativo\n(Máximo 10 caracteres alfanuméricos)");
        userTooltip.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 6;");
        Tooltip.install(userContainer, userTooltip);
        
        // Campo contraseña con icono
        HBox passContainer = new HBox();
        passContainer.setAlignment(Pos.CENTER_LEFT);
        passContainer.setSpacing(12);
        passContainer.setPrefWidth(280);
        passContainer.setStyle("-fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0; -fx-padding: 8 0 8 0;");
        
        Text passIcon = new Text("🔒");
        passIcon.setStyle("-fx-font-size: 16px;");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña");
        passwordField.setPrefWidth(240);
        passwordField.setPrefHeight(32);
        passwordField.setStyle("-fx-font-size: 15px; -fx-background-color: transparent; -fx-text-fill: #2D3748; -fx-prompt-text-fill: #A0AEC0; -fx-border-width: 0;");
        
        passContainer.getChildren().addAll(passIcon, passwordField);
        
        Tooltip passTooltip = new Tooltip("Ingrese su contraseña de acceso\n(Máximo 10 caracteres alfanuméricos)");
        passTooltip.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 6;");
        Tooltip.install(passContainer, passTooltip);
        
        // Botón con icono
        loginButton = new Button("→ Ingresar");
        loginButton.setPrefWidth(280);
        loginButton.setPrefHeight(48);
        loginButton.setStyle("-fx-background-color: #2D3748; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 500; -fx-background-radius: 24; -fx-cursor: hand; -fx-border-width: 0;");
        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#E53E3E"));
        errorLabel.setVisible(false);
        errorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 400;");
        
        getChildren().addAll(titleLabel, subtitleLabel, userContainer, passContainer, loginButton, errorLabel);
    }

    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setSpacing(32);
        setPadding(new Insets(48));
        setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 24, 0, 0, 8);");
        setMaxWidth(380);
        setPrefHeight(420);
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