package com.example.restaurantreservationsystem.util;

import java.sql.Connection;

// Manages a single SQLite connection and runs the schema initialisation script
public class DatabaseConnection {

    private static DatabaseConnection instance;

    // Returns the singleton instance, creating it on first call
    public static DatabaseConnection getInstance() {
        return null;
    }

    // Opens (or reuses) the SQLite connection to restaurant_reservation.db
    public Connection getConnection() {
        return null;
    }

    // Executes restaurant_reservation.sql to create tables if they don't exist
    public void initializeDatabase() {

    }

    // Closes the active connection and releases resources
    public void closeConnection() {

    }
}
