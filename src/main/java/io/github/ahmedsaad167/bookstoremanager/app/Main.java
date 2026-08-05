package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.database.DatabaseInitializer;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseManager;

import java.sql.SQLException;


public class Main {
    
    public static void main(String[] args) {
        try {           
            DatabaseInitializer.initialize();
            System.out.println("Database initializaed successfully.");
        } catch (SQLException e) {
            System.out.println("Database initializaed failed.");
            e.printStackTrace();
        }
    }
}