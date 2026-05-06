module com.example.restaurantreservationsystem {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    opens com.example.restaurantreservationsystem to javafx.graphics;
}