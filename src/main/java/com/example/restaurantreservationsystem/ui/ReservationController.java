package com.example.restaurantreservationsystem.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.restaurantreservationsystem.dao.CustomerDAO;
import com.example.restaurantreservationsystem.dao.ReservationDAO;
import com.example.restaurantreservationsystem.dao.RestaurantDAO;
import com.example.restaurantreservationsystem.dao.RestaurantTableDAO;
import com.example.restaurantreservationsystem.dao.StaffDAO;
import com.example.restaurantreservationsystem.model.Customer;
import com.example.restaurantreservationsystem.model.Reservation;
import com.example.restaurantreservationsystem.model.Restaurant;
import com.example.restaurantreservationsystem.model.RestaurantTable;
import com.example.restaurantreservationsystem.model.Staff;
import com.example.restaurantreservationsystem.util.AlertHelper;
import com.example.restaurantreservationsystem.util.DateTimeUtil;
import com.example.restaurantreservationsystem.util.NavigationManager;

import java.time.LocalDate;
import java.util.List;

// Form 3 - Reservations: core booking form with full CRUD and status management
public class ReservationController {

    // ── FXML fields ──────────────────────────────────────────────────────────
    private TableView<Reservation>           tableReservations;
    private TableColumn<Reservation,Integer> colResId;
    private TableColumn<Reservation,String>  colCustomer;
    private TableColumn<Reservation,String>  colRestaurant;
    private TableColumn<Reservation,String>  colTable;
    private TableColumn<Reservation,String>  colDate;
    private TableColumn<Reservation,String>  colTime;
    private TableColumn<Reservation,Integer> colPartySize;
    private TableColumn<Reservation,String>  colStatus;

    private ComboBox<Customer>        comboCustomer;
    private ComboBox<Restaurant>      comboRestaurant;
    private ComboBox<RestaurantTable> comboTable;
    private ComboBox<Staff>           comboStaff;
    private DatePicker                dateReservation;
    private ComboBox<String>          comboTimeSlot;
    private Spinner<Integer>          spinnerPartySize;
    private ComboBox<String>          comboStatus;
    private TextArea                  fieldNotes;
    private TextField                 fieldSearchDate;
    private ComboBox<String>          comboFilterStatus;

    private ReservationDAO              reservationDAO;
    private CustomerDAO                 customerDAO;
    private RestaurantTableDAO          tableDAO;
    private StaffDAO                    staffDAO;
    private RestaurantDAO               restaurantDAO;
    private ObservableList<Reservation> reservationList;
    private Reservation                 selectedReservation;

    // Initialises all DAOs, populates ComboBoxes, binds TableView, and loads reservations
    public void initialize() {
        reservationDAO  = new ReservationDAO();
        customerDAO     = new CustomerDAO();
        tableDAO        = new RestaurantTableDAO();
        staffDAO        = new StaffDAO();
        restaurantDAO   = new RestaurantDAO();
        reservationList = FXCollections.observableArrayList();

        // ── Column bindings ──────────────────────────────────────────────────
        // Reservation JOIN fields: customerName, restaurantName, tableNumber are set by mapRow()
        colResId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRestaurant.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        // DB column is reservation_date → getter is getReservationDate()
        colDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        // DB column is time_slot → getter is getTimeSlot()
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        colPartySize.setCellValueFactory(new PropertyValueFactory<>("partySize"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Party-size spinner: 1–20, default 2
        spinnerPartySize.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 2)
        );
        spinnerPartySize.setEditable(true);

        loadComboBoxData();
        loadAllReservations();

        // Row-selection listener
        tableReservations.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> { selectedReservation = newVal; onTableRowSelected(); }
        );

        // Cascade: restaurant/date/time changes refresh available tables
        comboRestaurant.valueProperty().addListener((obs, o, n) -> onRestaurantSelected());
        dateReservation.valueProperty().addListener((obs, o, n) -> onDateOrTimeChanged());
        comboTimeSlot.valueProperty().addListener((obs, o, n) -> onDateOrTimeChanged());

        // Filter combo listener
        comboFilterStatus.valueProperty().addListener((obs, o, n) -> onFilterByStatusChanged());
    }

    // Loads all customers, restaurants, time slots, and statuses into their ComboBoxes
    private void loadComboBoxData() {
        comboCustomer.setItems(FXCollections.observableArrayList(customerDAO.getAll()));
        comboRestaurant.setItems(FXCollections.observableArrayList(restaurantDAO.getAll()));
        comboStaff.setItems(FXCollections.observableArrayList(staffDAO.getAll()));

        // Half-hourly time slots 09:00 – 22:00
        ObservableList<String> slots = FXCollections.observableArrayList();
        for (int h = 9; h <= 22; h++) {
            slots.add(String.format("%02d:00", h));
            if (h < 22) slots.add(String.format("%02d:30", h));
        }
        comboTimeSlot.setItems(slots);

        // Form status combo
        comboStatus.setItems(FXCollections.observableArrayList(
            "Pending", "Confirmed", "Cancelled", "Completed"
        ));
        comboStatus.setValue("Pending");

        // Filter combo (includes "All")
        comboFilterStatus.setItems(FXCollections.observableArrayList(
            "All", "Pending", "Confirmed", "Cancelled", "Completed"
        ));
        comboFilterStatus.setValue("All");
    }

    // Fetches all reservations with JOIN fields and populates the TableView
    private void loadAllReservations() {
        reservationList.setAll(reservationDAO.getAll());
        tableReservations.setItems(reservationList);
    }

    // When restaurant is selected, refreshes the table ComboBox with available tables for that date/time
    public void onRestaurantSelected() {
        refreshAvailableTables();
    }

    // Re-runs available table query when the date or time slot changes
    public void onDateOrTimeChanged() {
        refreshAvailableTables();
    }

    // Populates form fields when the user selects a reservation row
    public void onTableRowSelected() {
        if (selectedReservation == null) return;

        // Match customer by ID
        comboCustomer.getItems().stream()
            .filter(c -> c.getCustomerId() == selectedReservation.getCustomerId())
            .findFirst().ifPresent(comboCustomer::setValue);

        // Match restaurant by ID → triggers refreshAvailableTables() via listener
        comboRestaurant.getItems().stream()
            .filter(r -> r.getRestaurantId() == selectedReservation.getRestaurantId())
            .findFirst().ifPresent(comboRestaurant::setValue);

        // Match staff by ID
        comboStaff.getItems().stream()
            .filter(s -> s.getStaffId() == selectedReservation.getStaffId())
            .findFirst().ifPresent(comboStaff::setValue);

        // Date — stored as 'yyyy-MM-dd'; DateTimeUtil.parseDate converts to LocalDate
        if (selectedReservation.getReservationDate() != null) {
            dateReservation.setValue(
                DateTimeUtil.parseDate(selectedReservation.getReservationDate())
            );
        }

        // Time slot — stored as 'HH:mm'
        comboTimeSlot.setValue(selectedReservation.getTimeSlot());

        // Select the reserved table (it may not be in "Available" list, so ensure it is included)
        comboTable.getItems().stream()
            .filter(t -> t.getTableId() == selectedReservation.getTableId())
            .findFirst().ifPresent(comboTable::setValue);

        spinnerPartySize.getValueFactory().setValue(selectedReservation.getPartySize());
        comboStatus.setValue(selectedReservation.getStatus());
        fieldNotes.setText(selectedReservation.getNotes() != null ? selectedReservation.getNotes() : "");
    }

    // Inserts a new reservation or updates the selected one after validation
    public void onSaveClicked() {
        if (!validateInputs()) return;

        // Build from form
        int    customerId  = comboCustomer.getValue().getCustomerId();
        int    tableId     = comboTable.getValue().getTableId();
        int    restaurantId = comboRestaurant.getValue().getRestaurantId();
        int    staffId     = comboStaff.getValue() != null ? comboStaff.getValue().getStaffId() : 0;
        String date        = DateTimeUtil.formatDate(dateReservation.getValue());
        String timeSlot    = comboTimeSlot.getValue();
        int    partySize   = spinnerPartySize.getValue();
        String status      = comboStatus.getValue();
        String notes       = fieldNotes.getText().trim();

        if (selectedReservation == null) {
            // INSERT — use the 9-arg constructor (no ID, no createdAt)
            Reservation r = new Reservation(
                customerId, tableId, restaurantId, staffId,
                date, timeSlot, partySize, status, notes
            );
            reservationDAO.insert(r);
            AlertHelper.showSuccess("Reservation created successfully.");
        } else {
            // UPDATE — mutate the selected object then persist
            selectedReservation.setCustomerId(customerId);
            selectedReservation.setTableId(tableId);
            selectedReservation.setRestaurantId(restaurantId);
            selectedReservation.setStaffId(staffId);
            selectedReservation.setReservationDate(date);
            selectedReservation.setTimeSlot(timeSlot);
            selectedReservation.setPartySize(partySize);
            selectedReservation.setStatus(status);
            selectedReservation.setNotes(notes);
            reservationDAO.update(selectedReservation);
            AlertHelper.showSuccess("Reservation updated successfully.");
        }

        onClearClicked();
        loadAllReservations();
    }

    // Validates customer, table, date, time, and party-size-vs-capacity constraints
    private boolean validateInputs() {
        if (comboCustomer.getValue() == null) {
            AlertHelper.showWarning("Please select a customer.");
            return false;
        }
        if (comboRestaurant.getValue() == null) {
            AlertHelper.showWarning("Please select a restaurant.");
            return false;
        }
        if (dateReservation.getValue() == null) {
            AlertHelper.showWarning("Please select a reservation date.");
            return false;
        }
        if (dateReservation.getValue().isBefore(LocalDate.now())) {
            AlertHelper.showWarning("Reservation date cannot be in the past.");
            return false;
        }
        if (comboTimeSlot.getValue() == null) {
            AlertHelper.showWarning("Please select a time slot.");
            return false;
        }
        if (comboTable.getValue() == null) {
            AlertHelper.showWarning("No available table selected. "
                + "Choose a restaurant, date, and time to see available tables.");
            return false;
        }
        if (comboStatus.getValue() == null) {
            AlertHelper.showWarning("Please select a status.");
            return false;
        }

        int partySize = spinnerPartySize.getValue();
        int capacity  = comboTable.getValue().getCapacity();
        if (partySize > capacity) {
            AlertHelper.showWarning("Party size (" + partySize
                + ") exceeds table capacity (" + capacity + "). Please choose a larger table.");
            return false;
        }

        return true;
    }

    // Cancels the selected reservation by setting its status to 'Cancelled'
    public void onCancelReservationClicked() {
        if (selectedReservation == null) {
            AlertHelper.showWarning("Please select a reservation to cancel.");
            return;
        }
        if ("Cancelled".equals(selectedReservation.getStatus())) {
            AlertHelper.showWarning("This reservation is already cancelled.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation(
            "Cancel reservation #" + selectedReservation.getReservationId() + "?"
        );
        if (confirmed) {
            reservationDAO.updateStatus(selectedReservation.getReservationId(), "Cancelled");
            AlertHelper.showSuccess("Reservation cancelled.");
            onClearClicked();
            loadAllReservations();
        }
    }

    // Marks the selected reservation as 'Completed' and releases the table
    public void onCompleteReservationClicked() {
        if (selectedReservation == null) {
            AlertHelper.showWarning("Please select a reservation to complete.");
            return;
        }
        if ("Completed".equals(selectedReservation.getStatus())) {
            AlertHelper.showWarning("This reservation is already completed.");
            return;
        }

        reservationDAO.updateStatus(selectedReservation.getReservationId(), "Completed");
        // Release the table so it becomes available for new bookings
        tableDAO.updateStatus(selectedReservation.getTableId(), "Available");
        AlertHelper.showSuccess("Reservation completed. Table released.");
        onClearClicked();
        loadAllReservations();
    }

    // Deletes the selected reservation record from the DB after confirmation
    public void onDeleteClicked() {
        if (selectedReservation == null) {
            AlertHelper.showWarning("Please select a reservation to delete.");
            return;
        }

        boolean confirmed = AlertHelper.showConfirmation(
            "Permanently delete reservation #" + selectedReservation.getReservationId() + "?"
        );
        if (confirmed) {
            reservationDAO.delete(selectedReservation.getReservationId());
            AlertHelper.showSuccess("Reservation deleted.");
            onClearClicked();
            loadAllReservations();
        }
    }

    // Clears all form fields and deselects the TableView row
    public void onClearClicked() {
        comboCustomer.setValue(null);
        comboRestaurant.setValue(null);
        comboTable.getItems().clear();
        comboTable.setValue(null);
        comboStaff.setValue(null);
        dateReservation.setValue(null);
        comboTimeSlot.setValue(null);
        spinnerPartySize.getValueFactory().setValue(2);
        comboStatus.setValue("Pending");
        fieldNotes.clear();
        selectedReservation = null;
        tableReservations.getSelectionModel().clearSelection();
    }

    // Filters the TableView by the date entered in the search field
    public void onSearchByDateClicked() {
        String dateText = fieldSearchDate.getText().trim();
        if (dateText.isEmpty()) {
            tableReservations.setItems(reservationList);
            return;
        }
        // ReservationDAO.getByDate expects 'yyyy-MM-dd' format
        List<Reservation> results = reservationDAO.getByDate(dateText);
        tableReservations.setItems(FXCollections.observableArrayList(results));
    }

    // Filters the TableView by the status chosen in the filter ComboBox
    public void onFilterByStatusChanged() {
        String status = comboFilterStatus.getValue();
        if (status == null || "All".equals(status)) {
            tableReservations.setItems(reservationList);
            return;
        }
        FilteredList<Reservation> filtered = new FilteredList<>(
            reservationList, r -> status.equals(r.getStatus())
        );
        tableReservations.setItems(filtered);
    }

    // Navigates back to the Dashboard form
    public void onBackClicked() {
        NavigationManager.getInstance().navigateToDashboard();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    // Queries RestaurantTableDAO for tables available at the chosen restaurant/date/time.
    // When editing an existing reservation, the currently assigned table is injected back
    // into the list so the form is pre-selectable even though that table is "taken".
    private void refreshAvailableTables() {
        comboTable.getItems().clear();

        Restaurant restaurant = comboRestaurant.getValue();
        LocalDate  date       = dateReservation.getValue();
        String     timeSlot   = comboTimeSlot.getValue();

        if (restaurant == null || date == null || timeSlot == null) return;

        String dateStr = DateTimeUtil.formatDate(date);
        List<RestaurantTable> available =
            tableDAO.getAvailableTables(restaurant.getRestaurantId(), dateStr, timeSlot);

        // Ensure the currently reserved table is visible when editing
        if (selectedReservation != null) {
            boolean alreadyPresent = available.stream()
                .anyMatch(t -> t.getTableId() == selectedReservation.getTableId());
            if (!alreadyPresent) {
                RestaurantTable currentTable = tableDAO.getById(selectedReservation.getTableId());
                if (currentTable != null) available.add(0, currentTable);
            }
        }

        comboTable.setItems(FXCollections.observableArrayList(available));
    }
}