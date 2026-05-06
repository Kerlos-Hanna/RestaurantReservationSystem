package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Customer;
import com.example.restaurantreservationsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD SQL operations for the CUSTOMERS table
public class CustomerDAO implements DAOInterface<Customer> {

    // Inserts a new customer and returns the auto-generated customer_id
    @Override
    public int insert(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, email, phone, created_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getCreatedDate());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Updates first_name, last_name, email, and phone for an existing customer
    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=? "
                + "WHERE customer_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhone());
            stmt.setInt   (5, customer.getCustomerId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a customer record by customer_id
    @Override
    public boolean delete(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetches one customer by customer_id, or null if not found
    @Override
    public Customer getById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns all customers ordered by last name then first name
    @Override
    public List<Customer> getAll() {
        String sql = "SELECT * FROM customers ORDER BY last_name, first_name";
        List<Customer> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Searches customers whose full name, email, or phone contains the keyword
    public List<Customer> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM customers "
                + "WHERE first_name LIKE ? OR last_name LIKE ? "
                + "OR email LIKE ? OR phone LIKE ? "
                + "ORDER BY last_name, first_name";
        List<Customer> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Finds a customer by their unique email address, or null if not found
    public Customer getByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns the total number of customers registered in the system
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM customers";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Maps a ResultSet row to a Customer object
    private Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt   ("customer_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("created_date")
        );
    }
}
