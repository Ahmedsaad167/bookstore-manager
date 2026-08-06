package io.github.ahmedsaad167.bookstoremanager.dao;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import io.github.ahmedsaad167.bookstoremanager.database.DatabaseManager;
import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;

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
                mapBookToRow(preparedStatement, book);
                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected == 0) {
                    throw new SQLException("Saving book failed.");
                }
                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                    
                    throw new SQLException("Failed to retrieve generated book ID.");
                }
            }
        }
    }

    public Book findById(int id) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "books"
                WHERE id = ? ;
                """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRowToBook(resultSet);
                    }
                }
            }
        }
        return null;
    }

    public List<Book> findAll() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "books";
            """;
            List<Book> books = new ArrayList<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        books.add(mapRowToBook(resultSet));
                    }
                }
            }
            return books;
        }
    }

    public boolean update(Book book) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                UPDATE "books"
                SET "title" = ?,
                    "category" = ?, 
                    "author" = ?,
                    "purchase_price" = ?,
                    "selling_price" = ?,
                    "stock_quantity" = ?,
                    "material_type" = ?,
                    "publisher" = ?,
                    "publication_year" = ?,
                    "isbn" = ?,
                    "age_group" = ?,
                    "notes" = ?
                WHERE "id" =  ?;
            """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                mapBookToRow(preparedStatement, book);
                preparedStatement.setInt(13, book.getId());
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected > 0;
            }
        }
    }

    public boolean deleteById(int id) throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                DELETE FROM "books"
                WHERE "id" = ? ;
            """;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                int rowAffected = preparedStatement.executeUpdate();
                return rowAffected > 0;
            }
        }
    }

    private static Book mapRowToBook(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");

        String title = resultSet.getString("title");
        String category = resultSet.getString("category");
        String author = resultSet.getString("author");

        double purchasePrice = resultSet.getDouble("purchase_price");
        double sellingPrice = resultSet.getDouble("selling_price");

        int stockQuantity = resultSet.getInt("stock_quantity");
        
        MaterialType materialType = MaterialType.valueOf(resultSet.getString("material_type"));

        String publisher = resultSet.getString("publisher");

        int publicationYear = resultSet.getInt("publication_year");

        String isbn = resultSet.getString("isbn");

        AgeGroup ageGroup = AgeGroup.valueOf(resultSet.getString("age_group"));

        String notes = resultSet.getString("notes");

        Book book = new Book(id, title, category, author, purchasePrice, sellingPrice, stockQuantity, materialType, ageGroup);
        book.setPublisher(publisher);
        book.setPublicationYear(publicationYear);
        book.setIsbn(isbn);
        book.setNotes(notes);
        return book;
    }

    private static void mapBookToRow(PreparedStatement preparedStatement, Book book) throws SQLException {
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
    }
}
