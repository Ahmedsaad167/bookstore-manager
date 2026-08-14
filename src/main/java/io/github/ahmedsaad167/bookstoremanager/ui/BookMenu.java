package io.github.ahmedsaad167.bookstoremanager.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;
import io.github.ahmedsaad167.bookstoremanager.search.BookSearchCriteria;
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

                    case "3" -> updateBook();

                    case "4" -> deleteBook();

                    case "5" -> searchBooks();

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
            showRow(book);
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
                System.out.println("Invalid choice.");
            }
            
        }
    }
        
        private void updateBook() throws SQLException {
            
            System.out.println();
            System.out.println("========== Update Book ==========");
            
            System.out.println("Book ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            
            Book book = bookService.getBookById(id);
            
            if (book == null) {
                System.out.println("Book not found.");
                return;
            }
            
            System.out.println();
            showRow(book);

            System.out.println("Enter new values:");

            System.out.print("Title: ");
            book.setTitle(scanner.nextLine().trim());

            System.out.print("Category: ");
            book.setCategory(scanner.nextLine().trim());

            System.out.print("Author: ");
            book.setAuthor(scanner.nextLine().trim());

            System.out.print("Purchase price: ");
            book.setPurchasePrice(
                    Double.parseDouble(scanner.nextLine().trim())
            );

            System.out.print("Selling price: ");
            book.setSellingPrice(
                    Double.parseDouble(scanner.nextLine().trim())
            );

            MaterialType materialType = readMaterialType();
            book.setMaterialType(materialType);

            System.out.print("Publisher: ");
            book.setPublisher(scanner.nextLine().trim());

            System.out.print("Publication year: ");
            book.setPublicationYear(
                    Integer.parseInt(scanner.nextLine().trim())
            );

            System.out.print("ISBN: ");
            book.setIsbn(scanner.nextLine().trim());

            AgeGroup ageGroup = readAgeGroup();
            book.setAgeGroup(ageGroup);

            System.out.print("Notes: ");
            book.setNotes(scanner.nextLine().trim());

            boolean updated = bookService.updateBook(book);
            
            System.out.println();
            System.out.println(updated ? "Book updated successfully." : "Book was not updated");

        }

        private void deleteBook() throws SQLException {
            System.out.println();
            System.out.println("========== Delete Book ==========");

            System.out.print("Book ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Are you sure you want to delete this book? (y/n): ");
            String confirmation = scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Delete cancelled.");
                return;
            }

            boolean deleted = bookService.deleteBook(id);

            System.out.println(deleted ? "Book deleted successfully." : "Book not found.");
        }

        private void searchBooks() throws SQLException {

            System.out.println();
            System.out.println("========== Search Books ==========");
            System.out.println("1. Search by title");
            System.out.println("2. Search by category");
            System.out.println("3. Search by author");
            System.out.println("4. Search by publisher");
            System.out.println("5. Advanced search");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> searchByTitle();
                case "2" -> searchByCategory();
                case "3" -> searchByAuthor();
                case "4" -> searchByPublisher();
                case "5" -> advancedSearch();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }

        private void searchByTitle() throws SQLException {

            System.out.print("Enter title: ");
            String title = scanner.nextLine().trim();

            BookSearchCriteria criteria = new BookSearchCriteria();
            criteria.setTitle(title);

            List<Book> books = bookService.search(criteria);

            showSearchResults(books);
        }

        private void searchByCategory() throws SQLException {

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();

            BookSearchCriteria criteria = new BookSearchCriteria();
            criteria.setCategory(category);

            List<Book> books = bookService.search(criteria);

            showSearchResults(books);
        }

        private void searchByAuthor() throws SQLException {

            System.out.print("Enter author: ");
            String author = scanner.nextLine().trim();

            BookSearchCriteria criteria = new BookSearchCriteria();
            criteria.setAuthor(author);

            List<Book> books = bookService.search(criteria);

            showSearchResults(books);
        }

        private void searchByPublisher() throws SQLException {

            System.out.print("Enter publisher: ");
            String publisher = scanner.nextLine().trim();

            BookSearchCriteria criteria = new BookSearchCriteria();
            criteria.setPublisher(publisher);

            List<Book> books = bookService.search(criteria);

            showSearchResults(books);
        }

        private void advancedSearch() throws SQLException {

            System.out.println();
            System.out.println("========== Advanced Book Search ==========");

            BookSearchCriteria criteria = new BookSearchCriteria();

            criteria.setTitle(
                readOptionalString("Title")
            );

            criteria.setCategory(
                readOptionalString("Category")
            );

            criteria.setAuthor(
                readOptionalString("Author")
            );

            criteria.setPublisher(
                readOptionalString("Publisher")
            );

            criteria.setIsbn(
                readOptionalString("ISBN")
            );

            criteria.setNotes(
                readOptionalString("Notes")
            );

            criteria.setMaterialType(
                readOptionalMaterialType()
            );

            criteria.setAgeGroup(
                readOptionalAgeGroup()
            );

            criteria.setPublicationYear(
                readOptionalInteger("Publication year")
            );

            criteria.setMinSellingPrice(
                readOptionalDouble("Minimum selling price")
            );

            criteria.setMaxSellingPrice(
                readOptionalDouble("Maximum selling price")
            );

            criteria.setAvailable(
                readOptionalAvailable()
            );

            List<Book> books = bookService.search(criteria);

            showSearchResults(books);
        }

        private String readOptionalString(String fieldName) {

            System.out.print(fieldName + " (Enter to skip): ");

            String value = scanner.nextLine().trim();

            return value.isBlank() ? null : value;
        }

        private MaterialType readOptionalMaterialType() {

            MaterialType[] types = MaterialType.values();

            System.out.println();
            System.out.println("Material type:");
            System.out.println("0. Any");

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

                    if (choice == 0) {
                        return null;
                    }

                    if (choice >= 1 && choice <= types.length) {
                        return types[choice - 1];
                    }

                } catch (NumberFormatException ignored) {
                }

                System.out.println("Invalid choice.");
            }
        }

        private AgeGroup readOptionalAgeGroup() {

            AgeGroup[] groups = AgeGroup.values();

            System.out.println();
            System.out.println("Age group:");
            System.out.println("0. Any");

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

                    if (choice == 0) {
                        return null;
                    }

                    if (choice >= 1 && choice <= groups.length) {
                        return groups[choice - 1];
                    }

                } catch (NumberFormatException ignored) {
                }

                System.out.println("Invalid choice.");
            }
        }

        private Double readOptionalDouble(String fieldName) {

            while (true) {

                System.out.print(fieldName + " (Enter to skip): ");

                String input = scanner.nextLine().trim();

                if (input.isBlank()) {
                    return null;
                }

                try {
                    return Double.parseDouble(input);

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
        }

        private Integer readOptionalInteger(String fieldName) {

            while (true) {

                System.out.print(fieldName + " (Enter to skip): ");

                String input = scanner.nextLine().trim();

                if (input.isBlank()) {
                    return null;
                }

                try {
                    return Integer.parseInt(input);

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid integer.");
                }
            }
        }

        private Boolean readOptionalAvailable() {

            System.out.println();
            System.out.println("Availability:");
            System.out.println("0. Any");
            System.out.println("1. Available");
            System.out.println("2. Out of stock");

            while (true) {

                System.out.print("Choose: ");

                String input = scanner.nextLine().trim();

                try {

                    int choice = Integer.parseInt(input);

                    switch (choice) {
                        case 0:
                            return null;

                        case 1:
                            return true;

                        case 2:
                            return false;

                        default:
                            System.out.println("Invalid choice.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid choice.");
                }
            }
        }

        private void showSearchResults(List<Book> books) {

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            System.out.println();
            System.out.println("Search results:");
            System.out.println("------------------------------");

            for (Book book : books) {
                showRow(book);
            }
        }
        
        private void showRow(Book book) {
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
            System.out.print(book.getMaterialType().getDisplayName());
            System.out.print(" | ");
            System.out.print(book.getPublisher());
            System.out.print(" | ");
            System.out.print(book.getPublicationYear());
            System.out.print(" | ");
            System.out.print(book.getIsbn());
            System.out.print(" | ");
            System.out.print(book.getAgeGroup().getDisplayName());
            System.out.print(" | ");
            System.out.print(book.getNotes());
            System.out.println();
            
        }
    }
    