package io.github.ahmedsaad167.bookstoremanager.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import io.github.ahmedsaad167.bookstoremanager.database.DatabaseManager;
import io.github.ahmedsaad167.bookstoremanager.model.Book;

public class BookDao {
    public int save(Book book) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                INSERT INTO "books" (
                    "title",
                    "category", 
                    "author",
                    "purchase_price",
                    "selling_price",
                    "stock_quantity",
                    "material_type",
                    "publisher",
                    "publication_year",
                    "isbn",
                    "age_group",
                    "notes"
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, book.getTitle());
                preparedStatement.setString(2, book.getCategory());
                preparedStatement.setString(3, book.getAuthor());
                preparedStatement.setDouble(4, book.getPurchasePrice());
                preparedStatement.setDouble(5, book.getSellingPrice());
                preparedStatement.setInt(6, book.getStockQuantity());
                preparedStatement.setString(7, book.getMaterialType().name());
                preparedStatement.setString(8, book.getPublisher());
                preparedStatement.setInt(9, book.getPublicationYear());
                preparedStatement.setString(10, book.getIsbn());
                preparedStatement.setString(11, book.getAgeGroup().name());
                preparedStatement.setString(12, book.getNotes());
    
                preparedStatement.executeUpdate();
                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } 
                    
                    throw new SQLException("Failed to retrieve generated book ID.");
                }
            }
        }
    }
}
