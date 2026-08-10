package io.github.ahmedsaad167.bookstoremanager.dao;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import io.github.ahmedsaad167.bookstoremanager.database.DatabaseManager;
import io.github.ahmedsaad167.bookstoremanager.model.Customer;

public class CustomerDao {
    private static final List<String> SEARCHABLE_FIELDS = List.of(
        "name",
        "username",
        "phone",
        "email",
        "address"
    );

    public int save(Customer customer) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                INSERT INTO "customers" (
                    "name",
                    "username",
                    "phone",
                    "email",
                    "address",
                    "notes"
                )
                VALUES (?, ?, ?, ?, ?, ?);
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                mapCustomerToRow(preparedStatement, customer);

                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected == 0) {
                    throw new SQLException("Saving customer failed.");
                }

                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                    
                    throw new SQLException("Failed to retrieve generated customer ID.");
                }
            }
        }
    }

    public Customer findById(int id) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "customers"
                WHERE "id" = ? ;
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRowToCustomer(resultSet);
                    }
                }
            }
        }
        return null;
    }

    public Customer findById(Connection connection, int id) throws SQLException {
        String sql = """
            SELECT * FROM "customers"
            WHERE "id" = ? ;
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToCustomer(resultSet);
                }
            }
        }

        return null;
    }

    public List<Customer> findAll() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "customers";
            """;
            List<Customer> customers = new ArrayList<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        customers.add(mapRowToCustomer(resultSet));
                    }
                }
            }
            return customers;
        }
    }

    public List<Customer> findByName(String name) throws SQLException {
        return findByField("name", name);
    }

    public List<Customer> findByUsername(String username) throws SQLException {
        return findByField("username", username);
    }

    public List<Customer> findByPhone(String phone) throws SQLException {
        return findByField("phone", phone);
    }

    public List<Customer> findByEmail(String email) throws SQLException {
        return findByField("email", email);
    }

    public List<Customer> findByAddress(String address) throws SQLException {
        return findByField("address", address);
    }

    public boolean update(Customer customer) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                UPDATE "customers"
                SET "name" = ?,
                    "username" = ?,
                    "phone" = ?,
                    "email" = ?,
                    "address" = ?,
                    "notes" = ?
                WHERE "id" = ? ; 
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                mapCustomerToRow(preparedStatement, customer);
                preparedStatement.setInt(7, customer.getId());
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected > 0;
            }
        }
    }

    public boolean deleteById(int id) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                DELETE FROM "customers"
                WHERE "id" = ? ;
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected > 0;
            }
        }
    }

    private static void mapCustomerToRow(PreparedStatement preparedStatement, Customer customer) throws SQLException {
        preparedStatement.setString(1, customer.getName());
        preparedStatement.setString(2, customer.getUsername());
        preparedStatement.setString(3, customer.getPhone());
        preparedStatement.setString(4, customer.getEmail());
        preparedStatement.setString(5, customer.getAddress());
        preparedStatement.setString(6, customer.getNotes());
    }

    private static Customer mapRowToCustomer(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");

        String name = resultSet.getString("name");
        String username = resultSet.getString("username");
        String phone = resultSet.getString("phone");
        String email = resultSet.getString("email");
        String address = resultSet.getString("address");
        String notes = resultSet.getString("notes");

        Customer customer = new Customer(id, name, username, phone);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setNotes(notes);
        
        return customer;
    }


    private static List<Customer> findByField(String field, String value) throws SQLException {
        if (!SEARCHABLE_FIELDS.contains(field)) {
            throw new IllegalArgumentException("Invalid search field.");
        }
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "customers" 
                WHERE %s LIKE ? ;
            """.formatted(field);
            List<Customer> customers = new ArrayList<>(); 
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + value + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        customers.add(mapRowToCustomer(resultSet));
                    }
                }
            }
            return customers;

        }
    }

}
