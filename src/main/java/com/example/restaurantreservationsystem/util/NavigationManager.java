package com.example.restaurantreservationsystem.util;

import javafx.scene.Scene;
import javafx.stage.Stage;

// Centralised router that swaps FXML scenes on the primary stage
public class NavigationManager {

    private static NavigationManager instance;
    private Stage primaryStage;

    public static NavigationManager getInstance()        { return null; }

    public void setPrimaryStage(Stage stage)             {}

    public void navigateToDashboard()                    {}

    public void navigateToCustomers()                    {}

    public void navigateToReservations()                 {}

    public void navigateToTables()                       {}

    public void navigateToStaff()                        {}

    private Scene loadScene(String fxmlPath)             { return null; }
}