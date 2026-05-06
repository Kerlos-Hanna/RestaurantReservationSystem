package com.example.restaurantreservationsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import com.example.restaurantreservationsystem.util.DatabaseConnection;
import com.example.restaurantreservationsystem.util.NavigationManager;

// Entry point: initialises DB connection and launches the Dashboard scene
public class MainApp extends Application {

    // Bootstraps JavaFX, wires DB, and opens the primary stage
    @Override
    public void start(Stage primaryStage) {

    }

    // Closes the DB connection pool cleanly when the app exits
    @Override
    public void stop() {

    }

    // Standard Java main – delegates to Application.launch()
    public static void main(String[] args) {

    }
}
