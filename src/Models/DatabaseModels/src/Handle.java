/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Compuomart
 */
public class Handle {
    private int staff_id;
    private int reservation_id;
    public Handle(int staff_id, int reservation_id){
        this.staff_id = staff_id;
        this.reservation_id = reservation_id;
    }
    public int GetHandleStaffID(){
        return this.staff_id;
    }
    public int GetHandleReservationID(){
        return this.reservation_id;
    }
}
