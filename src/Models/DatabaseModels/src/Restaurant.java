/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Time;
/**
 *
 * @author Compuomart
 */
public class Restaurant {
    private int restaurant_id;
    private String name;
    private String address;
    private String phone_number;
    private Time opening_hours;
    private Time closing_hours;
    
    public Restaurant(int id, String name, String addr, String phone, Time op_hours, Time closing_hours){
        this.restaurant_id = id;
        SetRestaurantName(name);
        SetRestaurantAddr(addr);
        SetRestaurantPhone(phone);
        this.opening_hours = op_hours;
        this.closing_hours = closing_hours;
    }
    public void SetRestaurantName(String name){
        if(name.length()>100){
            System.out.println("Error: Restaurant name exceeds max character count (100)");
        }
        else{
            this.name = name;
        }
    }
    public void SetRestaurantAddr(String addr){
        if(addr.length()>255){
            System.out.println("Error: Restaurant address exceeds max character count (255)");
        }
        else{
            this.address = addr;
        }
    }
    public void SetRestaurantPhone(String phone){
        if(phone.length()>20){
            System.out.println("Error: Restaurant phone exceeds max character count (20)");
        }
        else{
            this.phone_number = phone;
        }
    }
    
    public int GetRestaurantID(){
        return this.restaurant_id;
    }
    public String GetRestaurantName(){
        return this.name;
    }
    public String GetRestaurantPhone(){
        return this.phone_number;
    }
    public String GetRestaurantAdd(){
        return this.address;
    }
    public Time GetRestaurantOpeningHours(){
        return this.opening_hours;
    }
    public Time GetRestaurantClosingHours(){
        return this.closing_hours;
    }
}
