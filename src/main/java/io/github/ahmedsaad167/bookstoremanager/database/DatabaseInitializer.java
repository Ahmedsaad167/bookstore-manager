package io.github.ahmedsaad167.bookstoremanager.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initialize() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement()) {
                createBooksTable(statement);
                createCustomersTable(statement);
            }                
        }
        
        private static void createBooksTable(Statement statement) throws SQLException {
            String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    category TEXT NOT NULL,
                    author TEXT NOT NULL,
                    purchase_price REAL NOT NULL,
                    selling_price REAL NOT NULL,
                    stock_quantity INTEGER NOT NULL,
                    material_type TEXT NOT NULL,
                    publisher TEXT,
                    publication_year INTEGER,
                    isbn TEXT,
                    age_group TEXT NOT NULL,
                    notes TEXT
                );
            """;

            statement.executeUpdate(sql);
        }
        
        private static void createCustomersTable(Statement statement) throws SQLException {
            String sql = """
                CREATE TABLE IF NOT EXISTS "customers" (
                    "id" INTEGER PRIMARY KEY AUTOINCREMENT,
                    "name" TEXT NOT NULL,
                    "username" TEXT NOT NULL UNIQUE,
                    "phone" TEXT NOT NULL UNIQUE,
                    "email" TEXT,
                    "address" TEXT,
                    "notes" TEXT
                );
            """;
    
            statement.executeUpdate(sql);
        }
}

