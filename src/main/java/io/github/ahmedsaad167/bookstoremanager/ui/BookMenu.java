package io.github.ahmedsaad167.bookstoremanager.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;

public class BookMenu {
    
    private final Scanner scanner;
    private final BookService bookService;

    public BookMenu(Scanner scanner, BookService bookService) {
        this.scanner = scanner;
        this.bookService = bookService;
    }

    public void show() {
        boolean running = true;

        while (running) {
            
            printMenu();

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {

                    case "1" -> showAllBooks();

                    case "2" -> System.out.println("Add Book - Coming soon");

                    case "3" -> System.out.println("Update Book - Coming soon");

                    case "4" -> System.out.println("Delete Book - Coming soon");

                    case "5" -> System.out.println("Search Books - Coming soon");

                    case "0" -> running = false;
                    
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }

            System.out.println();
        }
    }

    private void printMenu() {

        System.out.println("==============================");
        System.out.println("       Book Management");
        System.out.println("==============================");
        System.out.println("1. Show All Books");
        System.out.println("2. Add Book");
        System.out.println("3. Update Book");
        System.out.println("4. Delete Book");
        System.out.println("5. Search Books");
        System.out.println("0. Back");
        System.out.print("Choose an option: ");
    }

    private void showAllBooks() throws SQLException {

        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book book : books) {
            System.out.println(book);
        }
    }
}
