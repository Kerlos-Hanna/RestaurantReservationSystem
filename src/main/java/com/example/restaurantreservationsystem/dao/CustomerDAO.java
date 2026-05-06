package com.example.restaurantreservationsystem.dao;

import com.example.restaurantreservationsystem.model.Customer;
import java.util.List;

// Handles all CRUD SQL operations for the CUSTOMERS table
public class CustomerDAO implements DAOInterface<Customer> {

    // Inserts a new customer and returns the auto-generated customer_id
    @Override
    public int insert(Customer customer) {
        return 0;
    }

    // Updates first_name, last_name, email, and phone for an existing customer
    @Override
    public boolean update(Customer customer) {
        return false;
    }

    // Deletes a customer record by customer_id
    @Override
    public boolean delete(int customerId) {
        return false;
    }

    // Fetches one customer by customer_id, or null if not found
    @Override
    public Customer getById(int customerId) {
        return null;
    }

    // Returns all customers ordered by last name then first name
    @Override
    public List<Customer> getAll() {
        return null;
    }

    // Searches customers whose full name, email, or phone contains the keyword
    public List<Customer> searchByKeyword(String keyword) {
        return null;
    }

    // Finds a customer by their unique email address, or null if not found
    public Customer getByEmail(String email) {
        return null;
    }

    // Returns the total number of customers registered in the system
    public int getTotalCount() {
        return 0;
    }
}
