package io.github.ahmedsaad167.bookstoremanager.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;
import io.github.ahmedsaad167.bookstoremanager.model.Customer;
import io.github.ahmedsaad167.bookstoremanager.search.CustomerSearchCriteria;

public class CustomerMenu {
    public final Scanner scanner;
    public final CustomerService customerService;

    public CustomerMenu(Scanner scanner, CustomerService customerService) {
        this.scanner = scanner;
        this.customerService = customerService;
    }

    public void show() {
        boolean running = true;
        while (running) {
            printMenu();

            String choice = scanner.nextLine().trim();

            try {

                switch (choice) {
                    case "1" -> showAllCustomers();
                    
                    case "2" -> addCustomer();

                    case "3" -> updateCustomer();
                    
                    case "4" -> deleteCustomer();
                    
                    case "5" -> searchCustomers();
                    
                    case "0" -> running = false;
                    
                    default -> System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.err.println("Batabase error:" + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {

        System.out.println("==============================");
        System.out.println("     Customer Management");
        System.out.println("==============================");
        System.out.println("1. Show All Customers");
        System.out.println("2. Add Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
        System.out.println("5. Search Customers");
        System.out.println("0. Back");
        System.out.print("Choose an option: ");
    }

    private void showAllCustomers() throws SQLException {

        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : customers) {
            showRow(customer);
        }
    }

    private void addCustomer() throws SQLException {

        System.out.println();
        
        System.out.println("========== Add Customer ==========");

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = readOptionalString();

        System.out.print("Address: ");
        String address = readOptionalString();

        System.out.print("Notes: ");
        String notes = readOptionalString();

        Customer customer = new Customer(name, username, phone);

        customer.setEmail(email);
        customer.setAddress(address);
        customer.setNotes(notes);

        int customerId = customerService.addCustomer(customer);

        System.out.println();
        System.out.println("Customer added successfully.");
        System.out.println("Customer ID: " + customerId);
    }

    private void updateCustomer() throws SQLException {

        System.out.println();
        System.out.println("========== Update Customer ==========");

        System.out.print("Customer ID: ");

        int id = Integer.parseInt(
            scanner.nextLine().trim()
        );

        Customer customer =
            customerService.getCustomerById(id);

        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println();
        showRow(customer);

        System.out.println();
        System.out.println("Enter new values:");

        System.out.print("Name: ");
        customer.setName(scanner.nextLine().trim());

        System.out.print("Username: ");
        customer.setUsername(scanner.nextLine().trim());

        System.out.print("Phone: ");
        customer.setPhone(scanner.nextLine().trim());

        System.out.print("Email: ");
        customer.setEmail(readOptionalString());

        System.out.print("Address: ");
        customer.setAddress(readOptionalString());

        System.out.print("Notes: ");
        customer.setNotes(readOptionalString());

        boolean updated =
            customerService.updateCustomer(customer);

        System.out.println(
            updated
                ? "Customer updated successfully."
                : "Customer was not updated."
        );
    }

    private void deleteCustomer() throws SQLException {

        System.out.println();
        System.out.println("========== Delete Customer ==========");

        System.out.print("Customer ID: ");

        int id = Integer.parseInt(
            scanner.nextLine().trim()
        );

        System.out.print(
            "Are you sure you want to delete this customer? (y/n): "
        );

        String confirmation =
            scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("y")) {

            System.out.println("Delete cancelled.");
            return;
        }

        boolean deleted =
            customerService.deleteCustomer(id);

        System.out.println(
            deleted
                ? "Customer deleted successfully."
                : "Customer not found."
        );
    }

    private void searchCustomers() throws SQLException {

        System.out.println();
        System.out.println("========== Search Customers ==========");
        System.out.println("1. Search by name");
        System.out.println("2. Search by username");
        System.out.println("3. Search by phone");
        System.out.println("4. Search by email");
        System.out.println("5. Search by address");
        System.out.println("6. Advanced search");
        System.out.println("0. Back");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {

            case "1" -> searchByName();

            case "2" -> searchByUsername();

            case "3" -> searchByPhone();

            case "4" -> searchByEmail();

            case "5" -> searchByAddress();

            case "6" -> advancedSearch();

            case "0" -> {
                return;
            }

            default ->
                System.out.println("Invalid choice.");
        }
    }

    private void searchByName() throws SQLException {

        System.out.print("Enter name: ");

        String name = scanner.nextLine().trim();

        showSearchResults(
            customerService.searchByName(name)
        );
    }

    private void searchByUsername() throws SQLException {

        System.out.print("Enter username: ");

        String username = scanner.nextLine().trim();

        showSearchResults(
            customerService.searchByUsername(username)
        );
    }

    private void searchByPhone() throws SQLException {

        System.out.print("Enter phone: ");

        String phone = scanner.nextLine().trim();

        showSearchResults(
            customerService.searchByPhone(phone)
        );
    }

    private void searchByEmail() throws SQLException {

        System.out.print("Enter email: ");

        String email = scanner.nextLine().trim();

        showSearchResults(
            customerService.searchByEmail(email)
        );
    }

    private void searchByAddress() throws SQLException {

        System.out.print("Enter address: ");

        String address = scanner.nextLine().trim();

        showSearchResults(
            customerService.searchByAddress(address)
        );
    }

    private void advancedSearch() throws SQLException {

        System.out.println();
        System.out.println("========== Advanced Customer Search ==========");

        CustomerSearchCriteria criteria =
            new CustomerSearchCriteria();

        criteria.setName(
            readOptionalField("Name")
        );

        criteria.setUsername(
            readOptionalField("Username")
        );

        criteria.setPhone(
            readOptionalField("Phone")
        );

        criteria.setEmail(
            readOptionalField("Email")
        );

        criteria.setAddress(
            readOptionalField("Address")
        );

        criteria.setNotes(
            readOptionalField("Notes")
        );

        List<Customer> customers =
            customerService.search(criteria);

        showSearchResults(customers);
    }

    private void showRow(Customer customer) {

        System.out.print(customer.getId());
        System.out.print(" | ");
        System.out.print(customer.getName());
        System.out.print(" | ");
        System.out.print(customer.getUsername());
        System.out.print(" | ");
        System.out.print(customer.getPhone());
        System.out.print(" | ");
        System.out.print(customer.getEmail());
        System.out.print(" | ");
        System.out.print(customer.getAddress());
        System.out.print(" | ");
        System.out.print(customer.getNotes());
        System.out.println();
    }

    private String readOptionalString() {
        String value = scanner.nextLine().trim();
        return value.isBlank() ? null : value;
    }

    private void showSearchResults(List<Customer> customers) {

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println();
        System.out.println("Search results:");
        System.out.println("------------------------------");

        for (Customer customer : customers) {
            showRow(customer);
        }
    }

    private String readOptionalField(String fieldName) {

        System.out.print(
            fieldName + " (Enter to skip): "
        );

        String value =
            scanner.nextLine().trim();

        return value.isBlank() ? null : value;
    }
}
