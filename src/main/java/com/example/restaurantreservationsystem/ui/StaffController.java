package com.example.restaurantreservationsystem.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.restaurantreservationsystem.dao.RestaurantDAO;
import com.example.restaurantreservationsystem.dao.StaffDAO;
import com.example.restaurantreservationsystem.model.Restaurant;
import com.example.restaurantreservationsystem.model.Staff;
import com.example.restaurantreservationsystem.util.AlertHelper;
import com.example.restaurantreservationsystem.util.DateTimeUtil;
import com.example.restaurantreservationsystem.util.NavigationManager;

import java.util.HashMap;
import java.util.Map;

// Form 5 - Staff: manages employee records including roles and restaurant assignments
public class StaffController {

    // ── FXML fields ──────────────────────────────────────────────────────────
    private TableView<Staff>           tableStaff;
    private TableColumn<Staff,Integer> colStaffId;
    private TableColumn<Staff,String>  colRestaurant;
    private TableColumn<Staff,String>  colFirstName;
    private TableColumn<Staff,String>  colLastName;
    private TableColumn<Staff,String>  colRole;
    private TableColumn<Staff,String>  colPhone;
    private TableColumn<Staff,String>  colEmail;
    private TableColumn<Staff,String>  colHireDate;

    private ComboBox<Restaurant> comboRestaurant;
    private TextField            fieldFirstName;
    private TextField            fieldLastName;
    private ComboBox<String>     comboRole;
    private TextField            fieldPhone;
    private TextField            fieldEmail;
    private DatePicker           dateHired;
    private TextField            fieldSearch;
    private ComboBox<String>     comboFilterRole;

    private StaffDAO              staffDAO;
    private RestaurantDAO         restaurantDAO;
    private ObservableList<Staff> staffList;
    private Staff                 selectedStaff;

    // restaurant_id → name lookup (Staff model has no restaurantName field)
    private Map<Integer, String> restaurantNameMap;

    // Initialises DAOs, populates restaurant and role ComboBoxes, loads all staff
    public void initialize() {
        staffDAO      = new StaffDAO();
        restaurantDAO = new RestaurantDAO();
        staffList     = FXCollections.observableArrayList();

        // Build lookup map for resolving restaurant names in the TableView
        restaurantNameMap = new HashMap<>();
        restaurantDAO.getAll().forEach(r -> restaurantNameMap.put(r.getRestaurantId(), r.getName()));

        // ── Column bindings ──────────────────────────────────────────────────
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        // Staff has no restaurantName field → resolve from map using restaurantId
        colRestaurant.setCellValueFactory(data ->
            new SimpleStringProperty(
                restaurantNameMap.getOrDefault(data.getValue().getRestaurantId(), "—")
            )
        );
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        // Populate restaurant ComboBox
        comboRestaurant.setItems(FXCollections.observableArrayList(restaurantDAO.getAll()));

        // Role lists
        ObservableList<String> roles = FXCollections.observableArrayList(
            "Manager", "Waiter", "Chef", "Host", "Bartender", "Busser", "Cashier"
        );
        comboRole.setItems(roles);

        ObservableList<String> filterRoles = FXCollections.observableArrayList("All");
        filterRoles.addAll(roles);
        comboFilterRole.setItems(filterRoles);
        comboFilterRole.setValue("All");

        loadAllStaff();

        // Row-selection listener
        tableStaff.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { selectedStaff = newVal; onTableRowSelected(); }
        );

        // Disable delete until a row is selected
        tableStaff.getSelectionModel().selectedItemProperty().addListener(
            (obs, o, n) -> { /* handled in button's disabled binding if needed */ }
        );

        // Real-time search and role filter
        fieldSearch.textProperty().addListener((obs, o, n) -> onSearchTyped());
        comboFilterRole.valueProperty().addListener((obs, o, n) -> onFilterByRoleChanged());
    }

    // Fetches all staff with restaurant names via JOIN and populates the TableView
    private void loadAllStaff() {
        staffList.setAll(staffDAO.getAll());
        applyFilters();
    }

    // Populates form fields when the user clicks a row in the TableView
    public void onTableRowSelected() {
        if (selectedStaff == null) return;

        // Match the Restaurant object in the ComboBox by ID
        comboRestaurant.getItems().stream()
            .filter(r -> r.getRestaurantId() == selectedStaff.getRestaurantId())
            .findFirst().ifPresent(comboRestaurant::setValue);

        fieldFirstName.setText(selectedStaff.getFirstName());
        fieldLastName.setText(selectedStaff.getLastName());
        comboRole.setValue(selectedStaff.getRole());
        fieldPhone.setText(selectedStaff.getPhone());
        fieldEmail.setText(selectedStaff.getEmail());

        // hireDate stored as 'yyyy-MM-dd'
        if (selectedStaff.getHireDate() != null && !selectedStaff.getHireDate().isEmpty()) {
            dateHired.setValue(DateTimeUtil.parseDate(selectedStaff.getHireDate()));
        }
    }

    // Inserts a new staff member if no row is selected, or updates the selected one
    public void onSaveClicked() {
        if (!validateInputs()) return;

        int    restaurantId = comboRestaurant.getValue().getRestaurantId();
        String firstName    = fieldFirstName.getText().trim();
        String lastName     = fieldLastName.getText().trim();
        String role         = comboRole.getValue();
        String phone        = fieldPhone.getText().trim();
        String email        = fieldEmail.getText().trim();
        String hireDate     = DateTimeUtil.formatDate(dateHired.getValue());

        if (selectedStaff == null) {
            // INSERT — use the no-ID Staff constructor
            Staff newStaff = new Staff(restaurantId, firstName, lastName, role, phone, email, hireDate);
            staffDAO.insert(newStaff);
            AlertHelper.showSuccess("Staff member added successfully.");
        } else {
            // UPDATE — mutate and persist
            selectedStaff.setRestaurantId(restaurantId);
            selectedStaff.setFirstName(firstName);
            selectedStaff.setLastName(lastName);
            selectedStaff.setRole(role);
            selectedStaff.setPhone(phone);
            selectedStaff.setEmail(email);
            selectedStaff.setHireDate(hireDate);
            staffDAO.update(selectedStaff);
            AlertHelper.showSuccess("Staff member updated successfully.");
        }

        // Refresh restaurant name map in case a new restaurant was added
        restaurantNameMap.clear();
        restaurantDAO.getAll().forEach(r -> restaurantNameMap.put(r.getRestaurantId(), r.getName()));

        onClearClicked();
        loadAllStaff();
    }

    // Validates that all required fields are filled and email format is correct
    private boolean validateInputs() {
        if (comboRestaurant.getValue() == null) {
            AlertHelper.showWarning("Please select a restaurant.");
            return false;
        }
        if (fieldFirstName.getText().trim().isEmpty()) {
            AlertHelper.showWarning("First name is required.");
            return false;
        }
        if (fieldLastName.getText().trim().isEmpty()) {
            AlertHelper.showWarning("Last name is required.");
            return false;
        }
        if (comboRole.getValue() == null) {
            AlertHelper.showWarning("Please select a role.");
            return false;
        }
        if (fieldPhone.getText().trim().isEmpty()) {
            AlertHelper.showWarning("Phone number is required.");
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
        if (dateHired.getValue() == null) {
            AlertHelper.showWarning("Hire date is required.");
            return false;
        }

        return true;
    }

    // Deletes the selected staff member after confirmation
    public void onDeleteClicked() {
        if (selectedStaff == null) {
            AlertHelper.showWarning("Please select a staff member to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation(
            "Delete staff member "" + selectedStaff.getFullName() + ""?\nThis cannot be undone."
        );
        if (confirmed) {
            staffDAO.delete(selectedStaff.getStaffId());
            AlertHelper.showSuccess("Staff member deleted successfully.");
            onClearClicked();
            loadAllStaff();
        }
    }

    // Clears all form fields and deselects the TableView row
    public void onClearClicked() {
        comboRestaurant.setValue(null);
        fieldFirstName.clear();
        fieldLastName.clear();
        comboRole.setValue(null);
        fieldPhone.clear();
        fieldEmail.clear();
        dateHired.setValue(null);
        selectedStaff = null;
        tableStaff.getSelectionModel().clearSelection();
    }

    // Filters the TableView in real-time based on the search field keyword
    public void onSearchTyped() {
        applyFilters();
    }

    // Reloads the TableView filtered by the role chosen in the filter ComboBox
    public void onFilterByRoleChanged() {
        applyFilters();
    }

    // Navigates back to the Dashboard form
    public void onBackClicked() {
        NavigationManager.getInstance().navigateToDashboard();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    // Applies both the role filter and the keyword search simultaneously
    private void applyFilters() {
        String keyword    = fieldSearch.getText();
        String roleFilter = comboFilterRole.getValue();

        FilteredList<Staff> filtered = new FilteredList<>(staffList, s -> {
            // Role filter
            boolean matchesRole = (roleFilter == null || "All".equals(roleFilter))
                                  || roleFilter.equals(s.getRole());
            // Keyword search across name and email
            boolean matchesKeyword = true;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String lower = keyword.toLowerCase();
                matchesKeyword = s.getFirstName().toLowerCase().contains(lower)
                              || s.getLastName().toLowerCase().contains(lower)
                              || s.getEmail().toLowerCase().contains(lower)
                              || s.getRole().toLowerCase().contains(lower);
            }
            return matchesRole && matchesKeyword;
        });

        SortedList<Staff> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableStaff.comparatorProperty());
        tableStaff.setItems(sorted);
    }
}