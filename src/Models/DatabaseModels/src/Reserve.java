/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Compuomart
 */
public class Reserve {
    private int reservation_id;
    private int table_id;
    public Reserve(int reservation_id, int table_id){
        this.reservation_id = reservation_id;
        this.table_id = table_id;
    }
    public int GetReservationID(){
        return this.reservation_id;
    }
    public int GetReservedTableID(){
        return this.table_id;
    }
}
