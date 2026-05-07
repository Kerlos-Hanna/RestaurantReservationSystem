/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Date;
import java.sql.Time;
/**
 *
 * @author Compuomart
 */
public class Table {
    private int table_id;
    private int table_number;
    private String location;
    private int capacity;
    private int restaurant_id;
    
    public Table(int id, int number, int cap, int restaurant_id, String loc){
        this.table_id = id;
        this.table_number = number;
        this.capacity = cap;
        this.restaurant_id = restaurant_id;
        SetTableLocation(loc);
    }
    public void SetTableLocation(String loc){
        if(loc.length()>50){
            System.out.println("Error: table location exceeds max character count (50)");
        }
        else{
            this.location= loc;
        }
    }
    public int GetTableID(){
        return this.table_id;
    }
    public int GetTableNumber(){
        return this.table_number;
    }
    public int GetTableCapacity(){
        return this.capacity;
    }
    public int GetTableRestaurantID(){
        return this.restaurant_id;
    }
    public String GetTableLocation(){
        return this.location;
    }
}
