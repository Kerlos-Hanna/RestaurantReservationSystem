package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Restaurant;
import com.example.restaurantreservationsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD SQL operations for the RESTAURANTS table
public class RestaurantDAO implements DAOInterface<Restaurant> {

    // Inserts a new restaurant and returns the auto-generated restaurant_id
    @Override
    public int insert(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (name, address, phone, email, "
                + "total_capacity, opening_time, closing_time) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getAddress());
            stmt.setString(3, restaurant.getPhone());
            stmt.setString(4, restaurant.getEmail());
            stmt.setInt   (5, restaurant.getTotalCapacity());
            stmt.setString(6, restaurant.getOpeningTime());
            stmt.setString(7, restaurant.getClosingTime());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Updates name, address, phone, email, capacity, and hours for an existing restaurant
    @Override
    public boolean update(Restaurant restaurant) {
        String sql = "UPDATE restaurants SET name=?, address=?, phone=?, email=?, "
                + "total_capacity=?, opening_time=?, closing_time=? "
                + "WHERE restaurant_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, restaurant.getName());
            stmt.setString(2, restaurant.getAddress());
            stmt.setString(3, restaurant.getPhone());
            stmt.setString(4, restaurant.getEmail());
            stmt.setInt   (5, restaurant.getTotalCapacity());
            stmt.setString(6, restaurant.getOpeningTime());
            stmt.setString(7, restaurant.getClosingTime());
            stmt.setInt   (8, restaurant.getRestaurantId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a restaurant and all its cascaded tables and staff by restaurant_id
    @Override
    public boolean delete(int restaurantId) {
        String sql = "DELETE FROM restaurants WHERE restaurant_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetches one restaurant by restaurant_id, or null if not found
    @Override
    public Restaurant getById(int restaurantId) {
        String sql = "SELECT * FROM restaurants WHERE restaurant_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns all restaurants ordered by name
    @Override
    public List<Restaurant> getAll() {
        String sql = "SELECT * FROM restaurants ORDER BY name";
        List<Restaurant> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Searches restaurants whose name or address contains the keyword (case-insensitive)
    public List<Restaurant> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM restaurants "
                + "WHERE name LIKE ? OR address LIKE ? "
                + "ORDER BY name";
        List<Restaurant> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Maps a ResultSet row to a Restaurant object
    private Restaurant mapRow(ResultSet rs) throws SQLException {
        return new Restaurant(
                rs.getInt   ("restaurant_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getInt   ("total_capacity"),
                rs.getString("opening_time"),
                rs.getString("closing_time")
        );
    }
}
