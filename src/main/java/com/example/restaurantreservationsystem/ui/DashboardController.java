package com.example.restaurantreservationsystem.ui;

import javafx.scene.control.Label;
import com.example.restaurantreservationsystem.dao.ReservationDAO;
import com.example.restaurantreservationsystem.dao.CustomerDAO;
import com.example.restaurantreservationsystem.dao.RestaurantDAO;

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

    }

    // Queries the DB and sets the total/today/status reservation count labels
    private void loadReservationStats() {

    }

    // Queries the DB and sets the total customers count label
    private void loadCustomerStats() {

    }

    // Navigates to the Customers form via NavigationManager
    
    public void onManageCustomersClicked() {

    }

    // Navigates to the Reservations form via NavigationManager
    
    public void onManageReservationsClicked() {

    }

    // Navigates to the Tables form via NavigationManager
    
    public void onManageTablesClicked() {

    }

    // Navigates to the Staff form via NavigationManager
    
    public void onManageStaffClicked() {

    }

    // Refreshes all stat labels with the latest DB counts
    
    public void onRefreshClicked() {

    }
}
