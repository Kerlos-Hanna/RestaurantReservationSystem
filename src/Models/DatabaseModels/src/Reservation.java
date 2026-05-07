
import java.sql.Date;
import java.sql.Time;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Compuomart
 */
public class Reservation {
    private int reservation_id;
    private Date reservation_date;
    private Time reservation_time;
    private int number_of_guests;
    private String status;
    
    private int customer_id;
    
    public Reservation(int id, Date reserve_Date, Time reserve_Time, int numOfGuests, String status, int customer_id){
        this.reservation_id = id;
        this.reservation_date = reserve_Date;
        this.reservation_time = reserve_Time;
        this.number_of_guests = numOfGuests;
        this.customer_id = customer_id;
        SetReservationStatus(status);
    }
    public void SetReservationStatus(String status){
        if(status.length()>20){
            System.out.println("Error: Reservation status exceeds max character count (20)");
        }
        else{
            this.status = status;
        }
    }
    public int GetReservationID(){
        return this.reservation_id;
    }
    public Date GetReservationDate(){
        return this.reservation_date;
    }
    public Time GetReservationTime(){
        return this.reservation_time;
    }
    public int GetReservationCustomerID(){
        return this.customer_id;
    }
    public String GetReservationStatus(){
        return this.status;
    }
    public int GetNumOFGuests(){
        return this.number_of_guests;
    }
}
