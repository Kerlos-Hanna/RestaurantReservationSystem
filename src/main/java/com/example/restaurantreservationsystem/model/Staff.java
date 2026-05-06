package com.example.restaurantreservationsystem.model;

// Represents a STAFF row: which restaurant they work at, role, and contact info
public class Staff {

    private int    staffId;
    private int    restaurantId;
    private String firstName;
    private String lastName;
    private String role;
    private String phone;
    private String email;
    private String hireDate;

    public Staff(int staffId, int restaurantId, String firstName, String lastName,
                 String role, String phone, String email, String hireDate) {}

    public Staff(int restaurantId, String firstName, String lastName,
                 String role, String phone, String email, String hireDate) {}

    public int    getStaffId()                     { return 0; }
    public void   setStaffId(int staffId)          {}

    public int    getRestaurantId()                { return 0; }
    public void   setRestaurantId(int restaurantId){}

    public String getFirstName()                   { return null; }
    public void   setFirstName(String firstName)   {}

    public String getLastName()                    { return null; }
    public void   setLastName(String lastName)     {}

    public String getFullName()                    { return null; }

    public String getRole()                        { return null; }
    public void   setRole(String role)             {}

    public String getPhone()                       { return null; }
    public void   setPhone(String phone)           {}

    public String getEmail()                       { return null; }
    public void   setEmail(String email)           {}

    public String getHireDate()                    { return null; }
    public void   setHireDate(String hireDate)     {}

    @Override
    public String toString()                       { return ""; }
}