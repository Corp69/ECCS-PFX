package com.mx.eccs.scorpioxl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

public class PFXCertificateComponent extends VBox {
    private Runnable onBack;
    private Label cerFileLabel;
    private Label keyFileLabel;
    private PasswordField passwordField;
    private File cerFile;
    private File keyFile;

    public PFXCertificateComponent() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #134e4a;");
        
        Button backButton = new Button("← Volver");
        backButton.setStyle("-fx-background-color: #0891b2; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8 16;");
        backButton.setOnAction(e -> { if (onBack != null) onBack.run(); });
        
        Label titleLabel = new Label("Generador de Certificado PFX");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 0 20;");
        
        header.getChildren().addAll(backButton, titleLabel);
        
        // Form
        VBox form = new VBox(20);
        form.setPadding(new Insets(40));
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(600);
        
        Text icon = new Text("📜");
        icon.setStyle("-fx-font-size: 60px;");
        
        Label formTitle = new Label("Convertir CER + KEY a PFX");
        formTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0d4f3c;");
        
        // CER File
        HBox cerBox = new HBox(10);
        cerBox.setAlignment(Pos.CENTER_LEFT);
        Button cerButton = new Button("📜 Seleccionar .CER");
        cerButton.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600;");
        cerButton.setOnAction(e -> selectCerFile());
        cerFileLabel = new Label("Ningún archivo seleccionado");
        cerFileLabel.setStyle("-fx-text-fill: #7f8c8d;");
        cerBox.getChildren().addAll(cerButton, cerFileLabel);
        
        // KEY File
        HBox keyBox = new HBox(10);
        keyBox.setAlignment(Pos.CENTER_LEFT);
        Button keyButton = new Button("🔑 Seleccionar .KEY");
        keyButton.setStyle("-fx-background-color: #0f766e; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600;");
        keyButton.setOnAction(e -> selectKeyFile());
        keyFileLabel = new Label("Ningún archivo seleccionado");
        keyFileLabel.setStyle("-fx-text-fill: #7f8c8d;");
        keyBox.getChildren().addAll(keyButton, keyFileLabel);
        
        // Password
        passwordField = new PasswordField();
        passwordField.setPromptText("Contraseña del certificado");
        passwordField.setStyle("-fx-font-size: 16px; -fx-padding: 16; -fx-background-radius: 8; -fx-border-color: #0f766e; -fx-border-width: 2; -fx-background-color: #f0fdfa;");
        

        
        VBox formContainer = createFormContainer();
        
        getChildren().addAll(header, formContainer);
    }

    private void setupLayout() {
        setStyle("-fx-background-color: #ecfdf5;");
        setAlignment(Pos.TOP_CENTER);
    }

    private void selectCerFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo .CER");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CER", "*.cer"));
        cerFile = fileChooser.showOpenDialog(getScene().getWindow());
        if (cerFile != null) {
            cerFileLabel.setText(cerFile.getName());
            cerFileLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
    
    private void selectKeyFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo .KEY");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos KEY", "*.key"));
        keyFile = fileChooser.showOpenDialog(getScene().getWindow());
        if (keyFile != null) {
            keyFileLabel.setText(keyFile.getName());
            keyFileLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
    
    private void generatePFX() {
        if (cerFile == null || keyFile == null || passwordField.getText().isEmpty()) {
            showAlert("Error", "Seleccione ambos archivos y proporcione la contraseña");
            return;
        }
        
        try {
            // Crear directorio temp en el proyecto
            File projectTempDir = new File("temp");
            if (!projectTempDir.exists()) {
                projectTempDir.mkdirs();
                System.out.println("[DEBUG] Directorio temp creado: " + projectTempDir.getAbsolutePath());
            }
            
            String tempDir = projectTempDir.getAbsolutePath();
            String baseName = cerFile.getName().replaceAll("\\.cer$", "");
            String password = passwordField.getText();
            
            // Ejecutar comandos OpenSSL
            executeOpenSSLCommands(tempDir, baseName, password);
            
            // Leer archivo PFX generado
            File pfxFile = new File(tempDir, baseName + ".pfx");
            if (pfxFile.exists()) {
                byte[] pfxBytes = Files.readAllBytes(pfxFile.toPath());
                String base64 = Base64.getEncoder().encodeToString(pfxBytes);
                
                // Mostrar modal con base64 y descargar
                showPFXModal(base64, baseName + ".pfx", pfxBytes);
                
                // No limpiar archivos temporales para que persistan
                System.out.println("[DEBUG] Archivo PFX disponible en: " + pfxFile.getAbsolutePath());
            } else {
                showAlert("Error", "No se pudo generar el archivo PFX");
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error al generar PFX: " + e.getMessage());
            // No mostrar alert adicional, solo el modal desde executeOpenSSLCommands
        }
    }
    
    private void executeOpenSSLCommands(String tempDir, String baseName, String password) throws Exception {
        try {
            System.out.println("[DEBUG] Iniciando generación PFX con OpenSSL...");
            System.out.println("[DEBUG] Directorio temporal: " + tempDir);
            System.out.println("[DEBUG] Nombre base: " + baseName);
            
            String opensslPath = "C:\\Program Files\\OpenSSL-Win64\\bin\\openssl.exe";
            String keyPem = tempDir + File.separator + "key.pem";
            String cerPem = tempDir + File.separator + "cer.pem";
            String pfxFile = tempDir + File.separator + baseName + ".pfx";
            
            // Comando 1: openssl pkcs8 -inform der -in key -passin pass:password -out key.pem
            System.out.println("[DEBUG] Paso 1: Convirtiendo KEY DER a PEM...");
            ProcessBuilder pb1 = new ProcessBuilder(opensslPath, "pkcs8", "-inform", "der", 
                    "-in", keyFile.getAbsolutePath(), 
                    "-passin", "pass:" + password, 
                    "-out", keyPem);
            executeCommand(pb1, "Convertir KEY a PEM");
            
            // Comando 2: openssl x509 -inform der -in cer -out cer.pem
            System.out.println("[DEBUG] Paso 2: Convirtiendo CER DER a PEM...");
            ProcessBuilder pb2 = new ProcessBuilder(opensslPath, "x509", "-inform", "der", 
                    "-in", cerFile.getAbsolutePath(), 
                    "-out", cerPem);
            executeCommand(pb2, "Convertir CER a PEM");
            
            // Comando 3: openssl pkcs12 -export -in cer.pem -inkey key.pem -out file.pfx
            System.out.println("[DEBUG] Paso 3: Generando archivo PFX...");
            ProcessBuilder pb3 = new ProcessBuilder(opensslPath, "pkcs12", "-export", 
                    "-in", cerPem, 
                    "-inkey", keyPem, 
                    "-out", pfxFile, 
                    "-passout", "pass:" + password);
            executeCommand(pb3, "Generar PFX");
            
            // Limpiar archivos temporales intermedios
            Files.deleteIfExists(new File(keyPem).toPath());
            Files.deleteIfExists(new File(cerPem).toPath());
            
            System.out.println("[DEBUG] Archivo PFX generado en: " + pfxFile);
            System.out.println("[DEBUG] Generación PFX completada exitosamente!");
        } catch (Exception e) {
            System.err.println("[ERROR] Error en executeOpenSSLCommands: " + e.getMessage());
            
            // Determinar tipo de error y mostrar mensaje apropiado
            String errorMessage = e.getMessage();
            if (errorMessage.contains("maybe wrong password")) {
                showErrorModal("Contraseña Incorrecta", "La contraseña ingresada no es correcta.\nVerifique la contraseña de su certificado.");
            } else if (errorMessage.contains("bad decrypt")) {
                showErrorModal("Error de Descifrado", "No se pudo descifrar el archivo KEY.\nVerifique que el archivo y la contraseña sean correctos.");
            } else {
                showErrorModal("Error de Procesamiento", "Error al procesar los certificados:\n" + errorMessage);
            }
            
            throw e;
        }
    }
    
    private void executeCommand(ProcessBuilder pb, String step) throws Exception {
        System.out.println("[DEBUG] Ejecutando: " + String.join(" ", pb.command()));
        Process process = pb.start();
        
        // Leer salida estándar
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[OPENSSL] " + line);
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder error = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                error.append(line).append("\n");
            }
            System.err.println("[ERROR] " + step + " falló con código: " + exitCode);
            System.err.println("[ERROR] Salida de error: " + error.toString());
            throw new RuntimeException(step + " falló: " + error.toString());
        }
        System.out.println("[DEBUG] " + step + " completado exitosamente");
    }
    
    private void showPFXModal(String base64, String filename, byte[] pfxBytes) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Certificado PFX Generado");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        
        Label title = new Label("✅ PFX Generado Exitosamente");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        
        TextArea base64Area = new TextArea(base64);
        base64Area.setEditable(false);
        base64Area.setPrefRowCount(10);
        base64Area.setStyle("-fx-font-family: monospace; -fx-font-size: 10px;");
        
        Button downloadButton = new Button("💾 Descargar PFX");
        downloadButton.setStyle("-fx-background-color: #059669; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600;");
        downloadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(filename);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PFX", "*.pfx"));
            File saveFile = fileChooser.showSaveDialog(modal);
            if (saveFile != null) {
                try {
                    Files.write(saveFile.toPath(), pfxBytes);
                    showAlert("Éxito", "Archivo descargado: " + saveFile.getAbsolutePath());
                    modal.close();
                } catch (IOException ex) {
                    showAlert("Error", "Error al guardar archivo: " + ex.getMessage());
                }
            }
        });
        
        content.getChildren().addAll(title, new Label("Base64:"), base64Area, downloadButton);
        
        Scene scene = new Scene(content, 600, 500);
        modal.setScene(scene);
        modal.show();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorModal(String title, String message) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Error");
        modal.setOpacity(0);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        
        Text errorIcon = new Text("❌");
        errorIcon.setStyle("-fx-font-size: 48px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");
        
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151; -fx-text-alignment: center;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        
        Button okButton = new Button("Entendido");
        okButton.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-padding: 12 24; -fx-background-radius: 8; -fx-font-weight: 600; -fx-cursor: hand;");
        okButton.setOnAction(e -> {
            javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), modal.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> modal.close());
            fadeOut.play();
        });
        
        content.getChildren().addAll(errorIcon, titleLabel, messageLabel, okButton);
        
        Scene scene = new Scene(content, 400, 280);
        scene.setFill(null);
        modal.setScene(scene);
        modal.show();
        
        // Animación de entrada
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), modal.getScene().getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        javafx.animation.ScaleTransition scaleIn = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(300), content);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        
        modal.setOpacity(1);
        fadeIn.play();
        scaleIn.play();
    }

    private VBox createFormContainer() {
        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(0, 30, 30, 30));
        
        VBox form = new VBox(20);
        form.setPadding(new Insets(30));
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(500);
        form.setPrefHeight(380);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0, 0, 5);");
        
        VBox headerSection = createFormHeader();
        VBox filesSection = createFilesSection();
        passwordField = createPasswordField();
        Button generateButton = createGenerateButton();
        
        form.getChildren().addAll(headerSection, filesSection, passwordField, generateButton);
        formContainer.getChildren().add(form);
        return formContainer;
    }
    
    private VBox createFormHeader() {
        VBox headerSection = new VBox(12);
        headerSection.setAlignment(Pos.CENTER);
        
        Text icon = new Text("📁");
        icon.setStyle("-fx-font-size: 36px;");
        
        Label formTitle = new Label("Generador PFX");
        formTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: 500; -fx-text-fill: #0d4f3c;");
        
        Label formSubtitle = new Label("CER + KEY → PFX");
        formSubtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280;");
        formSubtitle.setMaxWidth(300);
        
        headerSection.getChildren().addAll(icon, formTitle, formSubtitle);
        return headerSection;
    }
    
    private VBox createFilesSection() {
        VBox filesSection = new VBox(20);
        filesSection.setAlignment(Pos.CENTER);
        filesSection.setPadding(new Insets(10, 0, 10, 0));
        
        VBox cerSection = createFileSection("📁 Seleccionar .CER", "#059669", this::selectCerFile, true);
        VBox keySection = createFileSection("🔑 Seleccionar .KEY", "#0f766e", this::selectKeyFile, false);
        
        filesSection.getChildren().addAll(cerSection, keySection);
        return filesSection;
    }
    
    private VBox createFileSection(String buttonText, String color, Runnable action, boolean isCer) {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER);
        section.setPadding(new Insets(12));
        section.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 8;");
        section.setPrefWidth(280);
        
        Button button = new Button(buttonText);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 6; -fx-font-weight: 600; -fx-font-size: 12px; -fx-cursor: hand;");
        button.setPrefWidth(240);
        button.setOnAction(e -> action.run());
        
        Label fileLabel = new Label("Ningún archivo seleccionado");
        fileLabel.setStyle("-fx-text-fill: #9CA3AF; -fx-font-size: 13px; -fx-font-style: italic;");
        
        if (isCer) cerFileLabel = fileLabel;
        else keyFileLabel = fileLabel;
        
        section.getChildren().addAll(button, fileLabel);
        return section;
    }
    
    private PasswordField createPasswordField() {
        PasswordField field = new PasswordField();
        field.setPromptText("Ingrese la contraseña del certificado");
        field.setPrefWidth(280);
        field.setPrefHeight(35);
        field.setStyle("-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-border-color: #D1D5DB; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        return field;
    }
    
    private Button createGenerateButton() {
        Button button = new Button("Generar PFX");
        button.setStyle("-fx-background-color: #065f46; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 6; -fx-font-weight: 600; -fx-cursor: hand;");
        button.setPrefWidth(280);
        button.setPrefHeight(38);
        button.setOnAction(e -> generatePFX());
        return button;
    }
    
    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }
}