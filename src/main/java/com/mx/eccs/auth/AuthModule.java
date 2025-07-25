package com.mx.eccs.auth;

import com.mx.eccs.arieserp.AriesERPDashboard;
import com.mx.eccs.auth.components.AppSelectorComponent;
import com.mx.eccs.auth.components.LoadingComponent;
import com.mx.eccs.auth.components.LoginComponent;
import com.mx.eccs.scorpioxl.ScorpioXLDashboard;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AuthModule {
    private Stage stage;
    private Scene scene;
    private StackPane root;

    public AuthModule(Stage stage) {
        this.stage = stage;
        this.root = new StackPane();
        this.scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("ECCS");
        
        // Configurar icono de la ventana
        try {
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("/icon.ico"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("[DEBUG] Icono no encontrado: " + e.getMessage());
        }
        
        showLoadingScreen();
    }

    private void showLoadingScreen() {
        LoadingComponent loading = new LoadingComponent();
        loading.setOnComplete(this::showLoginScreen);
        root.getChildren().setAll(loading);
    }

    private void showLoginScreen() {
        LoginComponent login = new LoginComponent();
        login.setOnLoginSuccess(this::showAppSelector);
        
        StackPane loginContainer = new StackPane();
        loginContainer.setStyle("-fx-background-color: #2F2F2F;");
        loginContainer.getChildren().add(login);
        
        root.getChildren().setAll(loginContainer);
    }

    private void showAppSelector() {
        AppSelectorComponent selector = new AppSelectorComponent();
        selector.setOnAriesERPSelected(this::showAriesERPDashboard);
        selector.setOnScorpioXLSelected(this::showScorpioXLDashboard);
        root.getChildren().setAll(selector);
    }

    private void showAriesERPDashboard() {
        AriesERPDashboard dashboard = new AriesERPDashboard();
        dashboard.setOnBack(this::showAppSelector);
        dashboard.setOnLogout(this::showLoginScreen);
        root.getChildren().setAll(dashboard);
    }

    private void showScorpioXLDashboard() {
        ScorpioXLDashboard dashboard = new ScorpioXLDashboard();
        dashboard.setOnBack(this::showAppSelector);
        dashboard.setOnLogout(this::showLoginScreen);
        root.getChildren().setAll(dashboard);
    }

    public void show() {
        stage.show();
    }
}