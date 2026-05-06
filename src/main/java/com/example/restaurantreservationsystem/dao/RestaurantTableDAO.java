package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.RestaurantTable;
import java.util.List;

// Handles all CRUD SQL operations for the RESTAURANT_TABLES table
public class RestaurantTableDAO implements DAOInterface<RestaurantTable> {

    // Inserts a new table and returns the auto-generated table_id
    @Override
    public int insert(RestaurantTable table) {
        return 0;
    }

    // Updates table_number, capacity, location, and status for an existing table
    @Override
    public boolean update(RestaurantTable table) {
        return false;
    }

    // Deletes a table record by table_id
    @Override
    public boolean delete(int tableId) {
        return false;
    }

    // Fetches one table by table_id, or null if not found
    @Override
    public RestaurantTable getById(int tableId) {
        return null;
    }

    // Returns all tables for every restaurant, ordered by restaurant then table number
    @Override
    public List<RestaurantTable> getAll() {
        return null;
    }

    // Returns only the tables belonging to a specific restaurant
    public List<RestaurantTable> getByRestaurantId(int restaurantId) {
        return null;
    }

    // Returns only tables whose status equals the given value (e.g. 'Available')
    public List<RestaurantTable> getByStatus(String status) {
        return null;
    }

    // Updates only the status column for a specific table_id
    public boolean updateStatus(int tableId, String newStatus) {
        return false;
    }

    // Returns tables that are available for a given restaurant, date, and time slot
    public List<RestaurantTable> getAvailableTables(int restaurantId, String date, String timeSlot) {
        return null;
    }
}
