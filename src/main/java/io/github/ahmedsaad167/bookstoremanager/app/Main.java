package io.github.ahmedsaad167.bookstoremanager.app;

import java.sql.SQLException;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.dao.OrderDao;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseInitializer;
import io.github.ahmedsaad167.bookstoremanager.ui.BookMenu;
import io.github.ahmedsaad167.bookstoremanager.ui.MainMenu;

public class Main {

    public static void main(String[] args) {        
        try {
            
            DatabaseInitializer.initialize();
            
            Scanner scanner = new Scanner(System.in);

            BookDao bookDao = new BookDao();
            OrderDao orderDao = new OrderDao();
            BookService bookService = new BookService(bookDao, orderDao);
            BookMenu bookMenu = new BookMenu(scanner, bookService);
            
            MainMenu mainMenu = new MainMenu(scanner, bookMenu);

            mainMenu.show();

            scanner.close();

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}