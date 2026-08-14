package io.github.ahmedsaad167.bookstoremanager.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;
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

                    case "2" -> addBook();

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
            System.out.print(book.getId());
            System.out.print(" | ");
            System.out.print(book.getTitle());
            System.out.print(" | ");
            System.out.print(book.getCategory());
            System.out.print(" | ");
            System.out.print(book.getAuthor());
            System.out.print(" | ");
            System.out.print(book.getPurchasePrice());
            System.out.print(" | ");
            System.out.print(book.getSellingPrice());
            System.out.print(" | ");
            System.out.print(book.getStockQuantity());
            System.out.print(" | ");
            System.out.print(book.getMaterialType());
            System.out.print(" | ");
            System.out.print(book.getPublisher());
            System.out.print(" | ");
            System.out.print(book.getPublicationYear());
            System.out.print(" | ");
            System.out.print(book.getIsbn());
            System.out.print(" | ");
            System.out.print(book.getAgeGroup());
            System.out.print(" | ");
            System.out.print(book.getNotes());
            System.out.println();
        }
    }

    private void addBook() throws SQLException {

        System.out.println();
        System.out.println("========== Add Book ==========");

        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Author: ");
        String author = scanner.nextLine().trim();

        System.out.print("Purchase price: ");
        double purchasePrice = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Selling price: ");
        double sellingPrice = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Stock quantity: ");
        int stockQuantity = Integer.parseInt(scanner.nextLine().trim());

        MaterialType materialType = readMaterialType();

        System.out.print("Publisher: ");
        String publisher = scanner.nextLine().trim();

        System.out.print("Publication year: ");
        String publicationYearInput = scanner.nextLine().trim();

        int publicationYear = publicationYearInput.isBlank()
                ? 0
                : Integer.parseInt(publicationYearInput);

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();

        AgeGroup ageGroup = readAgeGroup();

        System.out.print("Notes: ");
        String notes = scanner.nextLine().trim();

        Book book = new Book(
                title,
                category,
                author,
                purchasePrice,
                sellingPrice,
                stockQuantity,
                materialType,
                ageGroup
        );

        book.setPublisher(publisher);
        book.setPublicationYear(publicationYear);
        book.setIsbn(isbn);
        book.setNotes(notes);

        int bookId = bookService.addBook(book);

        System.out.println();
        System.out.println("Book added successfully.");
        System.out.println("Book ID: " + bookId);
    }

    private MaterialType readMaterialType() {

        MaterialType[] types = MaterialType.values();

        System.out.println("Material type:");

        for (int i = 0; i < types.length; i++) {
            System.out.println(
                    (i + 1) + ". " + types[i].getDisplayName()
            );
        }

        while (true) {

            System.out.print("Choose: ");

            try {

                int choice = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                if (choice >= 1 && choice <= types.length) {
                    return types[choice - 1];
                }

            } catch (NumberFormatException ignored) {
            }

            System.out.println("Invalid choice.");
        }
    }

    private AgeGroup readAgeGroup() {

        AgeGroup[] groups = AgeGroup.values();

        System.out.println("Age group:");

        for (int i = 0; i < groups.length; i++) {
            System.out.println(
                    (i + 1) + ". " + groups[i].getDisplayName()
            );
        }

        while (true) {

            System.out.print("Choose: ");

            try {

                int choice = Integer.parseInt(
                        scanner.nextLine().trim()
                );

                if (choice >= 1 && choice <= groups.length) {
                    return groups[choice - 1];
                }

            } catch (NumberFormatException ignored) {
            }

            System.out.println("Invalid choice.");
        }
    }
}
