package io.github.ahmedsaad167.bookstoremanager.app;

import java.sql.SQLException;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseInitializer;
import io.github.ahmedsaad167.bookstoremanager.ui.MainMenu;

public class Main {

    public static void main(String[] args) {
        BookDao dao = new BookDao();
        
        try {
            DatabaseInitializer.initialize();

            Scanner scanner = new Scanner(System.in);

            MainMenu mainMenu = new MainMenu(scanner);

            mainMenu.show();

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}