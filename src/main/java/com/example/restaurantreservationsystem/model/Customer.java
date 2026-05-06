package com.example.restaurantreservationsystem.model;

// Represents a CUSTOMERS row: personal contact details and account creation date
public class Customer {

    private int    customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String createdDate;  // 'yyyy-MM-dd'

    // Constructs a Customer with all fields (used when reading from DB)
    public Customer(int customerId, String firstName, String lastName,
                    String email, String phone, String createdDate) {

    }

    // Constructs a new Customer without an ID (used before INSERT)
    public Customer(String firstName, String lastName,
                    String email, String phone, String createdDate) {

    }

    public int getCustomerId() {
        return 0;
    }

    public void setCustomerId(int customerId) {

    }

    public String getFirstName() {
        return null;
    }

    public void setFirstName(String firstName) {

    }

    public String getLastName() {
        return null;
    }

    public void setLastName(String lastName) {

    }

    // Convenience: returns firstName + " " + lastName
    public String getFullName() {
        return null;
    }

    public String getEmail() {
        return null;
    }

    public void setEmail(String email) {

    }

    public String getPhone() {
        return null;
    }

    public void setPhone(String phone) {

    }

    public String getCreatedDate() {
        return null;
    }

    public void setCreatedDate(String createdDate) {

    }

    // Returns full name and email for ComboBox dropdowns
    @Override
    public String toString() {
        return null;
    }
}
