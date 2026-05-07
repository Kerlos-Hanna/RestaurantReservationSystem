/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.sql.Date;
/**
 *
 * @author Compuomart
 */
public class Customer {
    private int customer_id;
    private String email;
    private String first_name;
    private String second_name;
    private String phone;
    private Date date_created;
    
    public Customer(int id, String email, String fname, String Sname, String phone, Date creation_date){
        this.customer_id = id;
        SetCustomerEmail(email);
        SetCustomerFirstName(fname);
        SetCustomerSecondName(Sname);
        SetCustomerPhone(phone);
        this.date_created = creation_date;
    }
    public void SetCustomerEmail(String email){
        if(email.length()>100){
            System.out.println("Error: Customer email exceeds max character count (100)");
        }
        else{
            this.email = email;
        }
    }
    public void SetCustomerFirstName(String fname){
        if(fname.length()>50){
            System.out.println("Error: Customer first_Name exceeds max character count (50)");
        }
        else{
            this.first_name = fname;
        }
    }
    public void SetCustomerSecondName(String Sname){
        if(Sname.length()>50){
            System.out.println("Error: Customer Second_Name exceeds max character count (50)");
        }
        else{
            this.second_name = Sname;
        }
    }
    public void SetCustomerPhone(String phone){
        if(phone.length()>20){
            System.out.println("Error: Customer phone exceeds max character count (20)");
        }
        else{
            this.phone = phone;
        }
    }
    
    public int GetCustomerID(){
        return this.customer_id;
    }
    public String GetCustomerEmail(){
        return this.email;
    }
    public String GetCustomerFirstName(){
        return this.first_name;
    }
    public String GetCustomerSecondName(){
        return this.second_name;
    }
    public String GetCustomerPhone(){
        return this.phone;
    }
    public Date GetCreationDate(){
        return this.date_created;
    }
    
}
