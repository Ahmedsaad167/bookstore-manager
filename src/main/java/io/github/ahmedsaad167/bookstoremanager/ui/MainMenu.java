package io.github.ahmedsaad167.bookstoremanager.ui;

import java.util.Scanner;

public class MainMenu {
    
    private final Scanner scanner;

    public MainMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            printMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> System.out.println("Book Management");
                
                case "2" -> System.out.println("Customer Management");

                case "3" -> System.out.println("Order Management");

                case "4" -> System.out.println("Search");

                case "5" -> System.out.println("Backup / Restore");

                case "0" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }

                default -> System.out.println("Invalid choice.");
            }

            System.out.println();
        }
    }

    private void printMenu() {

        System.out.println("==============================");
        System.out.println("      Bookstore Manager");
        System.out.println("==============================");
        System.out.println("1. Book Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Order Management");
        System.out.println("4. Search");
        System.out.println("5. Backup / Restore");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
}
