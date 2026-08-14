package io.github.ahmedsaad167.bookstoremanager.app;

import java.sql.SQLException;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.dao.CustomerDao;
import io.github.ahmedsaad167.bookstoremanager.dao.OrderDao;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;
import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseInitializer;
import io.github.ahmedsaad167.bookstoremanager.ui.BookMenu;
import io.github.ahmedsaad167.bookstoremanager.ui.CustomerMenu;
import io.github.ahmedsaad167.bookstoremanager.ui.MainMenu;

public class Main {

    public static void main(String[] args) {        
        try {
            
            DatabaseInitializer.initialize();
            
            Scanner scanner = new Scanner(System.in);

            BookDao bookDao = new BookDao();
            CustomerDao customerDao = new CustomerDao();
            OrderDao orderDao = new OrderDao();
            BookService bookService = new BookService(bookDao, orderDao);
            CustomerService customerService = new CustomerService(customerDao);
            BookMenu bookMenu = new BookMenu(scanner, bookService);
            CustomerMenu customerMenu = new CustomerMenu(scanner, customerService);
            
            MainMenu mainMenu = new MainMenu(scanner, bookMenu, customerMenu);

            mainMenu.show();

            scanner.close();

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
}