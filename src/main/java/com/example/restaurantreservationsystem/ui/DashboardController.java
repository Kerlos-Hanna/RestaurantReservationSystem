package com.example.restaurantreservationsystem.ui;

import javafx.scene.control.Label;
import com.example.restaurantreservationsystem.dao.CustomerDAO;
import com.example.restaurantreservationsystem.dao.ReservationDAO;
import com.example.restaurantreservationsystem.dao.RestaurantDAO;
import com.example.restaurantreservationsystem.util.NavigationManager;

// Form 1 - Dashboard: displays KPI stats and provides navigation buttons to all other forms
public class DashboardController {

    // ── FXML fields ──────────────────────────────────────────────────────────
    private Label labelTotalReservations;
    private Label labelTodayReservations;
    private Label labelTotalCustomers;
    private Label labelPendingCount;
    private Label labelConfirmedCount;
    private Label labelCancelledCount;

    private ReservationDAO reservationDAO;
    private CustomerDAO    customerDAO;
    private RestaurantDAO  restaurantDAO;

    // Initialises DAOs and populates all stat labels by calling load methods
    public void initialize() {
        reservationDAO = new ReservationDAO();
        customerDAO    = new CustomerDAO();
        restaurantDAO  = new RestaurantDAO();

        loadReservationStats();
        loadCustomerStats();
    }

    // Queries the DB and sets the total/today/status reservation count labels
    private void loadReservationStats() {
        // getAll().size() used for total since no dedicated getTotalCount() exists in ReservationDAO
        int total = reservationDAO.getAll().size();
        labelTotalReservations.setText(String.valueOf(total));
        labelTodayReservations.setText(String.valueOf(reservationDAO.getTodayCount()));
        labelPendingCount.setText(String.valueOf(reservationDAO.getCountByStatus("Pending")));
        labelConfirmedCount.setText(String.valueOf(reservationDAO.getCountByStatus("Confirmed")));
        labelCancelledCount.setText(String.valueOf(reservationDAO.getCountByStatus("Cancelled")));
    }

    // Queries the DB and sets the total customers count label
    private void loadCustomerStats() {
        labelTotalCustomers.setText(String.valueOf(customerDAO.getTotalCount()));
    }

    // Navigates to the Customers form via NavigationManager
    public void onManageCustomersClicked() {
        NavigationManager.getInstance().navigateToCustomers();
    }

    // Navigates to the Reservations form via NavigationManager
    public void onManageReservationsClicked() {
        NavigationManager.getInstance().navigateToReservations();
    }

    // Navigates to the Tables form via NavigationManager
    public void onManageTablesClicked() {
        NavigationManager.getInstance().navigateToTables();
    }

    // Navigates to the Staff form via NavigationManager
    public void onManageStaffClicked() {
        NavigationManager.getInstance().navigateToStaff();
    }

    // Refreshes all stat labels with the latest DB counts
    public void onRefreshClicked() {
        loadReservationStats();
        loadCustomerStats();
    }
}