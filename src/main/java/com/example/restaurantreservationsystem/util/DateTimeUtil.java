package com.example.restaurantreservationsystem.util;

import java.time.LocalDate;
import java.time.LocalTime;

// Conversion helpers between Java time types and the SQLite TEXT format used in the DB
public class DateTimeUtil {

    // Converts a LocalDate to the 'yyyy-MM-dd' string stored in the DB
    public static String formatDate(LocalDate date) {
        return null;
    }

    // Parses a 'yyyy-MM-dd' DB string back to a LocalDate
    public static LocalDate parseDate(String dateStr) {
        return null;
    }

    // Converts a LocalTime to the 'HH:mm' string stored in the DB
    public static String formatTime(LocalTime time) {
        return null;
    }

    // Parses a 'HH:mm' DB string back to a LocalTime
    public static LocalTime parseTime(String timeStr) {
        return null;
    }

    // Returns today's date as a formatted 'yyyy-MM-dd' string
    public static String today() {
        return null;
    }
}
