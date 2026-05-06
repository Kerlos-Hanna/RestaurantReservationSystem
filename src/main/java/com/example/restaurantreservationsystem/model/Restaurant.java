package com.example.restaurantreservationsystem.model;

// Represents a RESTAURANTS row: identity, contact info, hours, and total capacity
public class Restaurant {

    private int    restaurantId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private int    totalCapacity;
    private String openingTime;
    private String closingTime;

    public Restaurant(int restaurantId, String name, String address, String phone,
                      String email, int totalCapacity, String openingTime, String closingTime) {}

    public Restaurant(String name, String address, String phone,
                      String email, int totalCapacity, String openingTime, String closingTime) {}

    public int    getRestaurantId()                      { return 0; }
    public void   setRestaurantId(int restaurantId)      {}

    public String getName()                              { return null; }
    public void   setName(String name)                   {}

    public String getAddress()                           { return null; }
    public void   setAddress(String address)             {}

    public String getPhone()                             { return null; }
    public void   setPhone(String phone)                 {}

    public String getEmail()                             { return null; }
    public void   setEmail(String email)                 {}

    public int    getTotalCapacity()                     { return 0; }
    public void   setTotalCapacity(int totalCapacity)    {}

    public String getOpeningTime()                       { return null; }
    public void   setOpeningTime(String openingTime)     {}

    public String getClosingTime()                       { return null; }
    public void   setClosingTime(String closingTime)     {}

    @Override
    public String toString()                             { return ""; }
}