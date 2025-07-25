package com.mx.eccs;

import com.mx.eccs.auth.AuthModule;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        AuthModule authModule = new AuthModule(stage);
        authModule.show();
    }

    public static void main(String[] args) {
        launch();
    }

}