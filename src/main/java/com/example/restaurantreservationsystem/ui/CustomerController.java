package com.example.restaurantreservationsystem.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.restaurantreservationsystem.dao.CustomerDAO;
import com.example.restaurantreservationsystem.model.Customer;
import com.example.restaurantreservationsystem.util.AlertHelper;
import com.example.restaurantreservationsystem.util.DateTimeUtil;
import com.example.restaurantreservationsystem.util.NavigationManager;

// Form 2 - Customers: full CRUD interface for registering and managing customers
public class CustomerController {

    // ── FXML fields ──────────────────────────────────────────────────────────
    private TableView<Customer>           tableCustomers;
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

    private CustomerDAO              customerDAO;
    private ObservableList<Customer> customerList;
    private Customer                 selectedCustomer;

    // Initialises DAO, binds TableView columns, and loads all customers
    public void initialize() {
        customerDAO  = new CustomerDAO();
        customerList = FXCollections.observableArrayList();

        // Property names must match getter names: getCustomerId() → "customerId", etc.
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        loadAllCustomers();

        // Populate form fields whenever a row is selected
        tableCustomers.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { selectedCustomer = newVal; onTableRowSelected(); }
        );

        // Disable delete button until a row is selected
        btnDelete.setDisable(true);
        tableCustomers.getSelectionModel().selectedItemProperty().addListener(
            (obs, o, n) -> btnDelete.setDisable(n == null)
        );

        // Wire up real-time search
        fieldSearch.textProperty().addListener((obs, o, n) -> onSearchTyped());
    }

    // Fetches all customers from the DB and populates the TableView
    private void loadAllCustomers() {
        customerList.setAll(customerDAO.getAll());
        applySearch(fieldSearch.getText());
    }

    // Populates the form fields with the data of the clicked TableView row
    public void onTableRowSelected() {
        if (selectedCustomer == null) return;

        fieldFirstName.setText(selectedCustomer.getFirstName());
        fieldLastName.setText(selectedCustomer.getLastName());
        fieldEmail.setText(selectedCustomer.getEmail());
        fieldPhone.setText(selectedCustomer.getPhone());
        btnSave.setText("Update");
    }

    // Inserts a new customer if no row is selected, or updates the selected one
    public void onSaveClicked() {
        if (!validateInputs()) return;

        String firstName = fieldFirstName.getText().trim();
        String lastName  = fieldLastName.getText().trim();
        String email     = fieldEmail.getText().trim();
        String phone     = fieldPhone.getText().trim();

        if (selectedCustomer == null) {
            // INSERT — use the no-ID constructor; DateTimeUtil.today() provides 'yyyy-MM-dd'
            Customer newCustomer = new Customer(firstName, lastName, email, phone, DateTimeUtil.today());
            customerDAO.insert(newCustomer);
            AlertHelper.showSuccess("Customer added successfully.");
        } else {
            // UPDATE — mutate the selected object then persist
            selectedCustomer.setFirstName(firstName);
            selectedCustomer.setLastName(lastName);
            selectedCustomer.setEmail(email);
            selectedCustomer.setPhone(phone);
            customerDAO.update(selectedCustomer);
            AlertHelper.showSuccess("Customer updated successfully.");
        }

        onClearClicked();
        loadAllCustomers();
    }

    // Validates that first name, last name, email, and phone are non-empty and email is unique
    private boolean validateInputs() {
        if (fieldFirstName.getText().trim().isEmpty()) {
            AlertHelper.showWarning("First name is required.");
            return false;
        }
        if (fieldLastName.getText().trim().isEmpty()) {
            AlertHelper.showWarning("Last name is required.");
            return false;
        }

        String email = fieldEmail.getText().trim();
        if (email.isEmpty()) {
            AlertHelper.showWarning("Email is required.");
            return false;
        }
        if (!email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            AlertHelper.showWarning("Please enter a valid email address.");
            return false;
        }
        if (fieldPhone.getText().trim().isEmpty()) {
            AlertHelper.showWarning("Phone number is required.");
            return false;
        }

        // Uniqueness check: getByEmail returns null if no match, or another customer
        Customer existing = customerDAO.getByEmail(email);
        if (existing != null) {
            boolean isSameRecord = selectedCustomer != null
                                   && existing.getCustomerId() == selectedCustomer.getCustomerId();
            if (!isSameRecord) {
                AlertHelper.showWarning("This email is already registered to another customer.");
                return false;
            }
        }

        return true;
    }

    // Deletes the currently selected customer after a confirmation dialog
    public void onDeleteClicked() {
        if (selectedCustomer == null) {
            AlertHelper.showWarning("Please select a customer to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation(
            "Delete customer "" + selectedCustomer.getFullName() + ""?\nThis cannot be undone."
        );

        if (confirmed) {
            customerDAO.delete(selectedCustomer.getCustomerId());
            AlertHelper.showSuccess("Customer deleted successfully.");
            onClearClicked();
            loadAllCustomers();
        }
    }

    // Clears all form fields and deselects the TableView row
    public void onClearClicked() {
        fieldFirstName.clear();
        fieldLastName.clear();
        fieldEmail.clear();
        fieldPhone.clear();
        selectedCustomer = null;
        tableCustomers.getSelectionModel().clearSelection();
        btnSave.setText("Save");
    }

    // Filters the TableView in real-time based on the search field keyword
    public void onSearchTyped() {
        applySearch(fieldSearch.getText());
    }

    // Navigates back to the Dashboard form
    public void onBackClicked() {
        NavigationManager.getInstance().navigateToDashboard();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    // Wraps customerList in a FilteredList and binds it to the TableView
    private void applySearch(String keyword) {
        FilteredList<Customer> filtered = new FilteredList<>(customerList, c -> {
            if (keyword == null || keyword.trim().isEmpty()) return true;
            String lower = keyword.toLowerCase();
            return c.getFirstName().toLowerCase().contains(lower)
                || c.getLastName().toLowerCase().contains(lower)
                || c.getEmail().toLowerCase().contains(lower)
                || c.getPhone().toLowerCase().contains(lower);
        });

        SortedList<Customer> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableCustomers.comparatorProperty());
        tableCustomers.setItems(sorted);
    }
}