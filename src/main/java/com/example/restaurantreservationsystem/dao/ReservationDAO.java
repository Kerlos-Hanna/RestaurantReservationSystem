package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Reservation;
import com.example.restaurantreservationsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Handles all CRUD SQL operations for the RESERVATIONS table, including JOIN-based queries
public class ReservationDAO implements DAOInterface<Reservation> {

    // Inserts a new reservation and returns the auto-generated reservation_id
    @Override
    public int insert(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_id, table_id, restaurant_id, staff_id, "
                + "reservation_date, time_slot, party_size, status, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt   (1, reservation.getCustomerId());
            stmt.setInt   (2, reservation.getTableId());
            stmt.setInt   (3, reservation.getRestaurantId());
            stmt.setInt   (4, reservation.getStaffId());
            stmt.setString(5, reservation.getReservationDate());
            stmt.setString(6, reservation.getTimeSlot());
            stmt.setInt   (7, reservation.getPartySize());
            stmt.setString(8, reservation.getStatus());
            stmt.setString(9, reservation.getNotes());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Updates date, time_slot, party_size, status, staff_id, and notes for an existing reservation
    @Override
    public boolean update(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id=?, table_id=?, restaurant_id=?, "
                + "staff_id=?, reservation_date=?, time_slot=?, party_size=?, "
                + "status=?, notes=? WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, reservation.getCustomerId());
            stmt.setInt   (2, reservation.getTableId());
            stmt.setInt   (3, reservation.getRestaurantId());
            stmt.setInt   (4, reservation.getStaffId());
            stmt.setString(5, reservation.getReservationDate());
            stmt.setString(6, reservation.getTimeSlot());
            stmt.setInt   (7, reservation.getPartySize());
            stmt.setString(8, reservation.getStatus());
            stmt.setString(9, reservation.getNotes());
            stmt.setInt   (10, reservation.getReservationId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Deletes a reservation record by reservation_id
    @Override
    public boolean delete(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetches one reservation by reservation_id with JOIN-populated display fields
    @Override
    public Reservation getById(int reservationId) {
        String sql = buildJoinQuery() + " WHERE r.reservation_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns all reservations with customer, table, restaurant, and staff names via JOIN
    @Override
    public List<Reservation> getAll() {
        String sql = buildJoinQuery() + " ORDER BY r.reservation_date DESC, r.time_slot";
        List<Reservation> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns reservations filtered by their status (Pending, Confirmed, Cancelled, Completed)
    public List<Reservation> getByStatus(String status) {
        String sql = buildJoinQuery() + " WHERE r.status=? ORDER BY r.reservation_date DESC";
        List<Reservation> list = new ArrayList<>();

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

    // Returns all reservations for a specific customer
    public List<Reservation> getByCustomerId(int customerId) {
        String sql = buildJoinQuery() + " WHERE r.customer_id=? ORDER BY r.reservation_date DESC";
        List<Reservation> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns all reservations on a given date across all restaurants
    public List<Reservation> getByDate(String date) {
        String sql = buildJoinQuery() + " WHERE r.reservation_date=? ORDER BY r.time_slot";
        List<Reservation> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Returns all reservations for a specific restaurant on a given date
    public List<Reservation> getByRestaurantAndDate(int restaurantId, String date) {
        String sql = buildJoinQuery()
                + " WHERE r.restaurant_id=? AND r.reservation_date=?"
                + " ORDER BY r.time_slot";
        List<Reservation> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt   (1, restaurantId);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Updates only the status column for a specific reservation_id
    public boolean updateStatus(int reservationId, String newStatus) {
        String sql = "UPDATE reservations SET status=? WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus);
            stmt.setInt   (2, reservationId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Returns the count of today's reservations across all restaurants
    public int getTodayCount() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE reservation_date = DATE('now')";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Returns the count of reservations by status (used for dashboard statistics)
    public int getCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE status=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Reusable JOIN query that populates display fields from related tables
    private String buildJoinQuery() {
        return "SELECT r.*, "
                + "c.first_name || ' ' || c.last_name AS customer_name, "
                + "t.table_number, "
                + "rs.name AS restaurant_name, "
                + "s.first_name || ' ' || s.last_name AS staff_name "
                + "FROM reservations r "
                + "LEFT JOIN customers      c  ON r.customer_id   = c.customer_id "
                + "LEFT JOIN restaurant_tables t  ON r.table_id   = t.table_id "
                + "LEFT JOIN restaurants     rs ON r.restaurant_id = rs.restaurant_id "
                + "LEFT JOIN staff           s  ON r.staff_id     = s.staff_id";
    }

    // Maps a ResultSet row to a Reservation object including JOIN fields
    private Reservation mapRow(ResultSet rs) throws SQLException {
        Reservation res = new Reservation(
                rs.getInt   ("reservation_id"),
                rs.getInt   ("customer_id"),
                rs.getInt   ("table_id"),
                rs.getInt   ("restaurant_id"),
                rs.getInt   ("staff_id"),
                rs.getString("reservation_date"),
                rs.getString("time_slot"),
                rs.getInt   ("party_size"),
                rs.getString("status"),
                rs.getString("notes"),
                rs.getString("created_at")
        );
        res.setCustomerName  (rs.getString("customer_name"));
        res.setTableNumber   (rs.getString("table_number"));
        res.setRestaurantName(rs.getString("restaurant_name"));
        res.setStaffName     (rs.getString("staff_name"));
        return res;
    }
}
