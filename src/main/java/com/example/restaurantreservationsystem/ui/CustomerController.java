package com.example.restaurantreservationsystem.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import com.example.restaurantreservationsystem.dao.CustomerDAO;
import com.example.restaurantreservationsystem.model.Customer;

// Form 2 - Customers: full CRUD interface for registering and managing customers
public class CustomerController {

    // ── FXML fields ──────────────────────────────────────────────────────────
    private TableView<Customer>       tableCustomers;
    private TableColumn<Customer,Integer> colId;
    private TableColumn<Customer,String>  colFirstName;
    private TableColumn<Customer,String>  colLastName;
    private TableColumn<Customer,String>  colEmail;
    private TableColumn<Customer,String>  colPhone;
    private TableColumn<Customer,String>  colCreatedDate;

    private TextField fieldFirstName;
    private TextField fieldLastName;
    private TextField fieldEmail;
    private TextField fieldPhone;
    private TextField fieldSearch;

    private Button btnSave;
    private Button btnDelete;
    private Button btnClear;

    private CustomerDAO            customerDAO;
    private ObservableList<Customer> customerList;
    private Customer               selectedCustomer;

    // Initialises DAO, binds TableView columns, and loads all customers
    
    public void initialize() {

    }

    // Fetches all customers from the DB and populates the TableView
    private void loadAllCustomers() {

    }

    // Populates the form fields with the data of the clicked TableView row
    
    public void onTableRowSelected() {

    }

    // Inserts a new customer if no row is selected, or updates the selected one
    
    public void onSaveClicked() {

    }

    // Validates that first name, last name, email, and phone are non-empty and email is unique
    private boolean validateInputs() {
        return false;
    }

    // Deletes the currently selected customer after a confirmation dialog
    
    public void onDeleteClicked() {

    }

    // Clears all form fields and deselects the TableView row
    
    public void onClearClicked() {

    }

    // Filters the TableView in real-time based on the search field keyword
    
    public void onSearchTyped() {

    }

    // Navigates back to the Dashboard form
    
    public void onBackClicked() {

    }
}
