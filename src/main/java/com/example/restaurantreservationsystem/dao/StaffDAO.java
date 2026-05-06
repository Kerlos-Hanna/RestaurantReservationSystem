package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Staff;
import java.util.List;

// Handles all CRUD SQL operations for the STAFF table
public class StaffDAO implements DAOInterface<Staff> {

    // Inserts a new staff member and returns the auto-generated staff_id
    @Override
    public int insert(Staff staff) {
        return 0;
    }

    // Updates first_name, last_name, role, phone, and email for an existing staff member
    @Override
    public boolean update(Staff staff) {
        return false;
    }

    // Deletes a staff member record by staff_id
    @Override
    public boolean delete(int staffId) {
        return false;
    }

    // Fetches one staff member by staff_id, or null if not found
    @Override
    public Staff getById(int staffId) {
        return null;
    }

    // Returns all staff members ordered by restaurant then last name
    @Override
    public List<Staff> getAll() {
        return null;
    }

    // Returns all staff members who belong to a specific restaurant
    public List<Staff> getByRestaurantId(int restaurantId) {
        return null;
    }

    // Returns staff members filtered by role (e.g. 'Manager', 'Waiter', 'Host')
    public List<Staff> getByRole(String role) {
        return null;
    }

    // Searches staff whose full name or role contains the keyword
    public List<Staff> searchByKeyword(String keyword) {
        return null;
    }
}
