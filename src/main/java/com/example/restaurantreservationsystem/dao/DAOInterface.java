package com.example.restaurantreservationsystem.dao;

import java.util.List;

// Generic contract that every DAO must fulfil for full CRUD support
public interface DAOInterface<T> {

    // Inserts a new record into the DB; returns the generated primary key
    int insert(T entity);

    // Updates all editable columns of an existing record by its primary key
    boolean update(T entity);

    // Deletes a record by its primary key; returns true on success
    boolean delete(int id);

    // Retrieves one record by its primary key, or null if not found
    T getById(int id);

    // Retrieves every record in the table as an ordered list
    List<T> getAll();
}
