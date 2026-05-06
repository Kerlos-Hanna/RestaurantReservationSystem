package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Restaurant;
import java.util.List;

// Handles all CRUD SQL operations for the RESTAURANTS table
public class RestaurantDAO implements DAOInterface<Restaurant> {

    // Inserts a new restaurant and returns the auto-generated restaurant_id
    @Override
    public int insert(Restaurant restaurant) {
        return 0;
    }

    // Updates name, address, phone, email, capacity, and hours for an existing restaurant
    @Override
    public boolean update(Restaurant restaurant) {
        return false;
    }

    // Deletes a restaurant and all its cascaded tables and staff by restaurant_id
    @Override
    public boolean delete(int restaurantId) {
        return false;
    }

    // Fetches one restaurant by restaurant_id, or null if not found
    @Override
    public Restaurant getById(int restaurantId) {
        return null;
    }

    // Returns all restaurants ordered by name
    @Override
    public List<Restaurant> getAll() {
        return null;
    }

    // Searches restaurants whose name or address contains the keyword (case-insensitive)
    public List<Restaurant> searchByKeyword(String keyword) {
        return null;
    }
}
