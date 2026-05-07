/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Compuomart
 */
public class Staff {
    private int staff_id;
    private String first_name;
    private String last_name;
    private String role;
    private double salary;
    private String phone;
    
    private int restaurant_id;
    
    public Staff(int id, String fname, String lname, String role, double salary, String phone, int restaurant_id){
        this.staff_id = id;
        SetStaffFirstName(fname);
        SetStaffLastName(lname);
        SetStaffPhone(phone);
        this.salary = salary;
        SetStaffRole(role);
        this.restaurant_id = restaurant_id;
    }
    public void SetStaffPhone(String phone){
        if(phone.length()>20){
            System.out.println("Error: Staff phone exceeds max character count (20)");
        }
        else{
            this.phone = phone;
        }
    }
    public void SetStaffFirstName(String fname){
        if(fname.length()>50){
            System.out.println("Error: Staff first_Name exceeds max character count (50)");
        }
        else{
            this.first_name = fname;
        }
    }
    public void SetStaffLastName(String lname){
        if(lname.length()>50){
            System.out.println("Error: Staff last_name exceeds max character count (50)");
        }
        else{
            this.last_name = lname;
        }
    }
    public void SetStaffRole(String role){
        if(role.length()>50){
            System.out.println("Error: Staff role exceeds max character count (50)");
        }
        else{
            this.role = role;
        }
    }
    
    public int GetStaffID(){
        return this.staff_id;
    }
    public String GetStaffFirstName(){
        return this.first_name;
    }
    public String GetStaffLastName(){
        return this.last_name;
    }
    public String GetStaffRole(){
        return this.role;
    }
    public String GetStaffPhone(){
        return this.phone;
    }
    public double GetStaffSalary(){
        return this.salary;
    }
    public int GetStaffRestaurantID(){
        return this.restaurant_id;
    }
    
}
