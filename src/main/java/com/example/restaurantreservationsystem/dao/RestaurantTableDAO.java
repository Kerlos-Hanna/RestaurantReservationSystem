package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.RestaurantTable;
import com.example.restaurantreservationsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD SQL operations for the RESTAURANT_TABLES table
public class RestaurantTableDAO implements DAOInterface<RestaurantTable> {

    // Inserts a new table and returns the auto-generated table_id
    @Override
    public int insert(RestaurantTable table) {
        String sql = "INSERT INTO restaurant_tables (restaurant_id, table_number, "
                + "capacity, location, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt   (1, table.getRestaurantId());
            stmt.setString(2, table.getTableNumber());
            stmt.setInt   (3, table.getCapacity());
            stmt.setString(4, table.getLocation());
            stmt.setString(5, table.getStatus());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Updates table_number, capacity, location, and status for an existing table
    @Override
    public boolean update(RestaurantTable table) {
        String sql = "UPDATE restaurant_tables SET restaurant_id=?, table_number=?, "
                + "capacity=?, location=?, status=? "
                + "WHERE table_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, table.getRestaurantId());
            stmt.setString(2, table.getTableNumber());
            stmt.setInt   (3, table.getCapacity());
            stmt.setString(4, table.getLocation());
            stmt.setString(5, table.getStatus());
            stmt.setInt   (6, table.getTableId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a table record by table_id
    @Override
    public boolean delete(int tableId) {
        String sql = "DELETE FROM restaurant_tables WHERE table_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetches one table by table_id, or null if not found
    @Override
    public RestaurantTable getById(int tableId) {
        String sql = "SELECT * FROM restaurant_tables WHERE table_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns all tables for every restaurant, ordered by restaurant then table number
    @Override
    public List<RestaurantTable> getAll() {
        String sql = "SELECT * FROM restaurant_tables "
                + "ORDER BY restaurant_id, table_number";
        List<RestaurantTable> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns only the tables belonging to a specific restaurant
    public List<RestaurantTable> getByRestaurantId(int restaurantId) {
        String sql = "SELECT * FROM restaurant_tables "
                + "WHERE restaurant_id=? ORDER BY table_number";
        List<RestaurantTable> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, restaurantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns only tables whose status equals the given value (e.g. 'Available')
    public List<RestaurantTable> getByStatus(String status) {
        String sql = "SELECT * FROM restaurant_tables "
                + "WHERE status=? ORDER BY restaurant_id, table_number";
        List<RestaurantTable> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Updates only the status column for a specific table_id
    public boolean updateStatus(int tableId, String newStatus) {
        String sql = "UPDATE restaurant_tables SET status=? WHERE table_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt   (2, tableId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Returns tables that are available for a given restaurant, date, and time slot
    public List<RestaurantTable> getAvailableTables(int restaurantId, String date, String timeSlot) {
        String sql = "SELECT * FROM restaurant_tables t "
                + "WHERE t.restaurant_id = ? "
                + "AND t.status = 'Available' "
                + "AND t.table_id NOT IN ( "
                + "    SELECT r.table_id FROM reservations r "
                + "    WHERE r.restaurant_id = ? "
                + "    AND r.reservation_date = ? "
                + "    AND r.time_slot = ? "
                + "    AND r.status NOT IN ('Cancelled') "
                + ") "
                + "ORDER BY t.table_number";
        List<RestaurantTable> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, restaurantId);
            stmt.setInt   (2, restaurantId);
            stmt.setString(3, date);
            stmt.setString(4, timeSlot);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Maps a ResultSet row to a RestaurantTable object
    private RestaurantTable mapRow(ResultSet rs) throws SQLException {
        return new RestaurantTable(
                rs.getInt   ("table_id"),
                rs.getInt   ("restaurant_id"),
                rs.getString("table_number"),
                rs.getInt   ("capacity"),
                rs.getString("location"),
                rs.getString("status")
        );
    }
}
