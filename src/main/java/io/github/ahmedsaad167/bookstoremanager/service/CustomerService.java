package io.github.ahmedsaad167.bookstoremanager.service;

import java.util.List;

import java.sql.SQLException;

import io.github.ahmedsaad167.bookstoremanager.dao.CustomerDao;
import io.github.ahmedsaad167.bookstoremanager.model.Customer;

public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public int addCustomer(Customer customer) throws SQLException {
        validateCustomer(customer);

        return customerDao.save(customer);
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        validateCustomer(customer);

        checkId(customer.getId());

        return customerDao.update(customer);
    }

    public boolean deleteCustomer(int id) throws SQLException {
        checkId(id);
        // if (orderDao.existsByBookId(id)) {
        //     throw new IllegalArgumentException("Cannot delete a customer that has existing orders.");
        // }

        return customerDao.deleteById(id);
    }

    public Customer getCustomerById(int id) throws SQLException {
        checkId(id);

        return customerDao.findById(id);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDao.findAll();
    }

    public List<Customer> searchByName(String name) throws SQLException {
        validateSearchText(name, "name");
        
        return customerDao.findByName(name);
    }

    public List<Customer> searchByUsername(String username) throws SQLException {
        validateSearchText(username, "username");
        
        return customerDao.findByUsername(username);
    }

    public List<Customer> searchByPhone(String phone) throws SQLException {
        validateSearchText(phone, "phone");
        
        return customerDao.findByPhone(phone);
    }

    public List<Customer> searchByEmail(String email) throws SQLException {
        validateSearchText(email, "email");
        
        return customerDao.findByEmail(email);
    }

    public List<Customer> searchByAddress(String address) throws SQLException {
        validateSearchText(address, "address");
        
        return customerDao.findByAddress(address);
    }

    private static void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank.");
        }
        
        if (customer.getUsername() == null || customer.getUsername().isBlank()) {
            throw new IllegalArgumentException("Customer username cannot be null or blank.");
        }
        
        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Customer phone cannot be null or blank.");
        }
    }

    private static void checkId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }
    }

    private static void validateSearchText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Search " + fieldName + " cannot be null or blank.");
        }
    }
}
