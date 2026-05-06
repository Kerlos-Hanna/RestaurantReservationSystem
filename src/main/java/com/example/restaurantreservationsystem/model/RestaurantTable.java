package com.example.restaurantreservationsystem.model;

// Represents a RESTAURANT_TABLES row: which restaurant it belongs to, seating, and status
public class RestaurantTable {

    private int    tableId;
    private int    restaurantId;
    private String tableNumber;
    private int    capacity;
    private String location;
    private String status;

    public RestaurantTable(int tableId, int restaurantId, String tableNumber,
                           int capacity, String location, String status) {}

    public RestaurantTable(int restaurantId, String tableNumber,
                           int capacity, String location, String status) {}

    public int    getTableId()                       { return 0; }
    public void   setTableId(int tableId)            {}

    public int    getRestaurantId()                  { return 0; }
    public void   setRestaurantId(int restaurantId)  {}

    public String getTableNumber()                   { return null; }
    public void   setTableNumber(String tableNumber) {}

    public int    getCapacity()                      { return 0; }
    public void   setCapacity(int capacity)          {}

    public String getLocation()                      { return null; }
    public void   setLocation(String location)       {}

    public String getStatus()                        { return null; }
    public void   setStatus(String status)           {}

    @Override
    public String toString()                         { return ""; }
}