package com.example.restaurantreservationsystem.model;

public class Reservation {

    private int    reservationId;
    private int    customerId;
    private int    tableId;
    private int    restaurantId;
    private int    staffId;
    private String reservationDate;
    private String timeSlot;
    private int    partySize;
    private String status;
    private String notes;
    private String createdAt;
    private String customerName;
    private String tableNumber;
    private String restaurantName;
    private String staffName;

    public Reservation(int reservationId, int customerId, int tableId, int restaurantId,
                       int staffId, String reservationDate, String timeSlot,
                       int partySize, String status, String notes, String createdAt) {}

    public Reservation(int customerId, int tableId, int restaurantId, int staffId,
                       String reservationDate, String timeSlot,
                       int partySize, String status, String notes) {}

    public int    getReservationId()                        { return 0; }
    public void   setReservationId(int reservationId)      {}

    public int    getCustomerId()                           { return 0; }
    public void   setCustomerId(int customerId)             {}

    public int    getTableId()                              { return 0; }
    public void   setTableId(int tableId)                   {}

    public int    getRestaurantId()                         { return 0; }
    public void   setRestaurantId(int restaurantId)         {}

    public int    getStaffId()                              { return 0; }
    public void   setStaffId(int staffId)                   {}

    public String getReservationDate()                      { return null; }
    public void   setReservationDate(String reservationDate){}

    public String getTimeSlot()                             { return null; }
    public void   setTimeSlot(String timeSlot)              {}

    public int    getPartySize()                            { return 0; }
    public void   setPartySize(int partySize)               {}

    public String getStatus()                               { return null; }
    public void   setStatus(String status)                  {}

    public String getNotes()                                { return null; }
    public void   setNotes(String notes)                    {}

    public String getCreatedAt()                            { return null; }
    public void   setCreatedAt(String createdAt)            {}

    public String getCustomerName()                         { return null; }
    public void   setCustomerName(String customerName)      {}

    public String getTableNumber()                          { return null; }
    public void   setTableNumber(String tableNumber)        {}

    public String getRestaurantName()                       { return null; }
    public void   setRestaurantName(String restaurantName)  {}

    public String getStaffName()                            { return null; }
    public void   setStaffName(String staffName)            {}

    @Override
    public String toString()                                { return ""; }
}