package com.mx.eccs.scorpioxl;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ScorpioXLDashboard extends BorderPane {
    private Runnable onBack;
    private Runnable onLogout;
    private Label clockLabel;

    public ScorpioXLDashboard() {
        initializeComponents();
        setupLayout();
        startClock();
    }

    private void initializeComponents() {
        // Navbar con cerrar sesión
        HBox navbar = createNavbar();
        setTop(navbar);
        
        // Sidebar eliminado
        
        // Main content replicando AriesERP
        StackPane mainContent = new StackPane();
        
        clockLabel = new Label();
        clockLabel.setStyle("-fx-font-size: 120px; -fx-font-weight: bold; -fx-text-fill: rgba(13, 79, 60, 0.1); -fx-font-family: 'Arial';");
        
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));
        
        // Header
        Label titleLabel = new Label("Scorpio XL");
        titleLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: 300; -fx-text-fill: #0d4f3c; -fx-letter-spacing: 2px;");
        
        Label subtitleLabel = new Label("Dashboard Ejecutivo");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6B7280; -fx-font-weight: 400;");
        
        Label statusLabel = new Label("Sistema Activo - Plataforma XML");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #059669; -fx-font-weight: bold;");
        
        // Card de generar PFX
        VBox pfxCard = createDashboardCard("📁", "Generar Certificado PFX", "Convierte CER + KEY a PFX", "#059669", () -> showPFXComponent());
        
        content.getChildren().addAll(titleLabel, subtitleLabel, statusLabel, pfxCard);
        
        mainContent.getChildren().addAll(clockLabel, content);
        StackPane.setAlignment(clockLabel, Pos.CENTER);
        StackPane.setAlignment(content, Pos.CENTER);
        

        
        setCenter(mainContent);
    }
    
    private void showPFXComponent() {
        // Configurar variables de entorno OpenSSL antes de mostrar PFX
        configureOpenSSLEnvironment();
        
        // Verificar si OpenSSL está disponible
        if (verifyOpenSSLInstallation()) {
            // Mostrar modal con estado de variables
            showEnvironmentStatusModal(() -> {
                PFXCertificateComponent pfxComponent = new PFXCertificateComponent();
                pfxComponent.setOnBack(() -> {
                    setTop(null);
                    setLeft(null);
                    setCenter(null);
                    initializeComponents();
                    setupLayout();
                    startClock();
                });
                setTop(null);
                setLeft(null);
                setCenter(pfxComponent);
            });
        } else {
            // Mostrar modal de error si OpenSSL no está disponible
            showOpenSSLErrorModal();
        }
    }
    
    private void configureOpenSSLEnvironment() {
        try {
            String opensslPath = "C:\\Program Files\\OpenSSL-Win64\\bin";
            String opensslConf = "C:\\Program Files\\OpenSSL-Win64\\bin\\openssl.cfg";
            
            // Obtener PATH actual
            String currentPath = System.getenv("PATH");
            if (currentPath == null || !currentPath.contains(opensslPath)) {
                // Agregar OpenSSL al PATH
                String newPath = opensslPath + ";" + (currentPath != null ? currentPath : "");
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "set", "PATH=" + newPath);
                pb.start();
                System.out.println("[DEBUG] OpenSSL PATH configurado: " + opensslPath);
            }
            
            // Configurar OPENSSL_CONF
            System.setProperty("OPENSSL_CONF", opensslConf);
            System.out.println("[DEBUG] OPENSSL_CONF configurado: " + opensslConf);
            
            // Verificar que OpenSSL funciona
            verifyOpenSSLInstallation();
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error configurando OpenSSL: " + e.getMessage());
        }
    }
    
    private boolean verifyOpenSSLInstallation() {
        try {
            ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\OpenSSL-Win64\\bin\\openssl.exe", "version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("[DEBUG] ✓ OpenSSL configurado correctamente");
                return true;
            } else {
                System.err.println("[ERROR] OpenSSL no se pudo ejecutar. Verifique la instalación.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error verificando OpenSSL: " + e.getMessage());
            return false;
        }
    }
    
    private void showEnvironmentStatusModal(Runnable onContinue) {
        javafx.stage.Stage modal = new javafx.stage.Stage();
        modal.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        modal.setTitle("Configuración OpenSSL");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        
        Label titleLabel = new Label("ECCS - LISTO PARA GENERAR PFX");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #059669;");
        
        VBox statusList = new VBox(15);
        statusList.setAlignment(Pos.CENTER_LEFT);
        statusList.setPadding(new Insets(20, 0, 0, 0));
        
        Label connectionLabel = new Label("1.- Conexión segura con el SAT");
        connectionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151; -fx-font-weight: 600;");
        
        Label descriptionLabel = new Label("Tu sistema está configurado con certificados de seguridad\nvalidados para comunicarse de forma segura con los\nservidores del SAT y procesar tus archivos fiscales.");
        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-line-spacing: 2;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(380);
        
        statusList.getChildren().addAll(connectionLabel, descriptionLabel);
        
        Button continueButton = new Button("Continuar");
        continueButton.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600; -fx-cursor: hand;");
        continueButton.setOnAction(e -> {
            modal.close();
            onContinue.run();
        });
        
        content.getChildren().addAll(titleLabel, statusList, continueButton);
        
        javafx.scene.Scene scene = new javafx.scene.Scene(content, 450, 250);
        modal.setScene(scene);
        modal.show();
    }
    
    private void showOpenSSLErrorModal() {
        javafx.stage.Stage modal = new javafx.stage.Stage();
        modal.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        modal.setTitle("Configuración Requerida");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        
        javafx.scene.text.Text errorIcon = new javafx.scene.text.Text("⚠️");
        errorIcon.setStyle("-fx-font-size: 48px;");
        
        Label titleLabel = new Label("Aún no tenemos conexión segura");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
        
        Label messageLabel = new Label("Para generar certificados PFX de forma segura,\nnecesitas instalar OpenSSL en tu sistema.");
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151; -fx-text-alignment: center;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Label instructionLabel = new Label("Instala OpenSSL y reinicia la aplicación");
        instructionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-font-weight: 500;");
        
        Button okButton = new Button("Entendido");
        okButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600; -fx-cursor: hand;");
        okButton.setOnAction(e -> modal.close());
        
        content.getChildren().addAll(errorIcon, titleLabel, messageLabel, instructionLabel, okButton);
        
        javafx.scene.Scene scene = new javafx.scene.Scene(content, 400, 300);
        modal.setScene(scene);
        modal.show();
    }
    
    private void setupLayout() {
        setStyle("-fx-background-color: #ecfdf5;");
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
    
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_RIGHT);
        navbar.setPadding(new Insets(15, 20, 15, 20));
        navbar.setStyle("-fx-background-color: #134e4a;");
        
        Button logoutButton = new Button("Cerrar Sesión");
        logoutButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: 500; -fx-padding: 8 16; -fx-background-radius: 4; -fx-cursor: hand;");
        logoutButton.setOnAction(e -> { if (onLogout != null) onLogout.run(); });
        
        navbar.getChildren().add(logoutButton);
        return navbar;
    }
    
    private VBox createDashboardCard(String icon, String title, String description, String color, Runnable onAction) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 5); -fx-cursor: hand;");
        card.setPrefWidth(350);
        card.setPrefHeight(220);
        
        javafx.scene.text.Text iconText = new javafx.scene.text.Text(icon);
        iconText.setStyle("-fx-font-size: 56px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: 600; -fx-text-fill: " + color + "; -fx-text-alignment: center;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(320);
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(320);
        
        card.getChildren().addAll(iconText, titleLabel, descLabel);
        
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 8); -fx-cursor: hand; -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 5); -fx-cursor: hand; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
        });
        
        card.setOnMouseClicked(e -> onAction.run());
        
        return card;
    }
}