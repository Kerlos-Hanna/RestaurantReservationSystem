package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Staff;
import com.example.restaurantreservationsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD SQL operations for the STAFF table
public class StaffDAO implements DAOInterface<Staff> {

    // Inserts a new staff member and returns the auto-generated staff_id
    @Override
    public int insert(Staff staff) {
        String sql = "INSERT INTO staff (restaurant_id, first_name, last_name, "
                + "role, phone, email, hire_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt   (1, staff.getRestaurantId());
            stmt.setString(2, staff.getFirstName());
            stmt.setString(3, staff.getLastName());
            stmt.setString(4, staff.getRole());
            stmt.setString(5, staff.getPhone());
            stmt.setString(6, staff.getEmail());
            stmt.setString(7, staff.getHireDate());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Updates first_name, last_name, role, phone, and email for an existing staff member
    @Override
    public boolean update(Staff staff) {
        String sql = "UPDATE staff SET restaurant_id=?, first_name=?, last_name=?, "
                + "role=?, phone=?, email=?, hire_date=? "
                + "WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, staff.getRestaurantId());
            stmt.setString(2, staff.getFirstName());
            stmt.setString(3, staff.getLastName());
            stmt.setString(4, staff.getRole());
            stmt.setString(5, staff.getPhone());
            stmt.setString(6, staff.getEmail());
            stmt.setString(7, staff.getHireDate());
            stmt.setInt   (8, staff.getStaffId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a staff member record by staff_id
    @Override
    public boolean delete(int staffId) {
        String sql = "DELETE FROM staff WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetches one staff member by staff_id, or null if not found
    @Override
    public Staff getById(int staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns all staff members ordered by restaurant then last name
    @Override
    public List<Staff> getAll() {
        String sql = "SELECT * FROM staff ORDER BY restaurant_id, last_name, first_name";
        List<Staff> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns all staff members who belong to a specific restaurant
    public List<Staff> getByRestaurantId(int restaurantId) {
        String sql = "SELECT * FROM staff WHERE restaurant_id=? "
                + "ORDER BY last_name, first_name";
        List<Staff> list = new ArrayList<>();

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

    // Returns staff members filtered by role (e.g. 'Manager', 'Waiter', 'Host')
    public List<Staff> getByRole(String role) {
        String sql = "SELECT * FROM staff WHERE role=? "
                + "ORDER BY last_name, first_name";
        List<Staff> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Searches staff whose full name or role contains the keyword
    public List<Staff> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM staff "
                + "WHERE first_name LIKE ? OR last_name LIKE ? OR role LIKE ? "
                + "ORDER BY last_name, first_name";
        List<Staff> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Maps a ResultSet row to a Staff object
    private Staff mapRow(ResultSet rs) throws SQLException {
        return new Staff(
                rs.getInt   ("staff_id"),
                rs.getInt   ("restaurant_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("role"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("hire_date")
        );
    }
}
