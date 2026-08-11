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
import io.github.ahmedsaad167.bookstoremanager.search.BookSearchCriteria;

public class BookDao {
    private static final List<String> SEARCHABLE_FIELDS = List.of(
    "title",
    "category",
    "author",
    "publisher"
    );

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

    public Book findById(Connection connection, int id) throws SQLException {
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

    public List<Book> findByTitle(String title) throws SQLException {
        return findByField("title", title);
    }

    public List<Book> findByCategory(String category) throws SQLException {
        return findByField("category", category);
    }

    public List<Book> findByAuthor(String author) throws SQLException {
        return findByField("author", author);
    }

    public List<Book> findByPublisher(String publisher) throws SQLException {
        return findByField("publisher", publisher);
    }

    public List<Book> search(Connection connection, BookSearchCriteria criteria) throws SQLException {
        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null.");
        }

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM "books"
            WHERE 1 = 1
        """);

        List<Object> parameters = new ArrayList<>();

        if (criteria.getTitle() != null && !criteria.getTitle().isBlank()) {
            sql.append("""
                AND "title" LIKE ?
            """);
            parameters.add("%" + criteria.getTitle().trim() + "%");
        }

        if (criteria.getCategory() != null && !criteria.getCategory().isBlank()) {
            sql.append("""
                AND "category" LIKE ?
            """);
            parameters.add("%" + criteria.getCategory().trim() + "%");
        }

        if (criteria.getAuthor() != null && !criteria.getAuthor().isBlank()) {
            sql.append("""
                AND "author" LIKE ?
            """);
            parameters.add("%" + criteria.getAuthor().trim() + "%");
        }

        if (criteria.getPublisher() != null && !criteria.getPublisher().isBlank()) {
            sql.append("""
                AND "publisher" LIKE ?
            """);
            parameters.add("%" + criteria.getPublisher().trim() + "%");
        }

        if (criteria.getIsbn() != null && !criteria.getIsbn().isBlank()) {
            sql.append("""
                AND "isbn" LIKE ?
            """);
            parameters.add("%" + criteria.getIsbn().trim() + "%");
        }

        if (criteria.getNotes() != null && !criteria.getNotes().isBlank()) {
            sql.append("""
                AND "notes" LIKE ?
            """);
            parameters.add("%" + criteria.getNotes().trim() + "%");
        }

    if (criteria.getMaterialType() != null) {
        sql.append("""
            AND "material_type" = ?
        """);
        parameters.add(criteria.getMaterialType().name());
    }

    if (criteria.getAgeGroup() != null) {
        sql.append("""
            AND "age_group" = ?
        """);
        parameters.add(criteria.getAgeGroup().name());
    }

    if (criteria.getPublicationYear() != null) {
        sql.append("""
            AND "publication_year" = ?
        """);
        parameters.add(criteria.getPublicationYear());
    }

    if (criteria.getMinSellingPrice() != null) {
        sql.append("""
            AND "selling_price" >= ?
        """);
        parameters.add(criteria.getMinSellingPrice());
    }

    if (criteria.getMaxSellingPrice() != null) {
        sql.append("""
            AND "selling_price" <= ?
        """);
        parameters.add(criteria.getMaxSellingPrice());
    }

    if (criteria.isAvailable() != null) {
        if (criteria.isAvailable()) {
            sql.append("""
                AND "stock_quantity" > 0
            """);
        } else {
            sql.append("""
                AND "stock_quantity" = 0
            """);
        }
    }

    sql.append("""
        ORDER BY "title";
    """);

    List<Book> books = new ArrayList<>();

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while(resultSet.next()) {
                books.add(mapRowToBook(resultSet));
            }
        }
    }
    return books;

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

    public boolean decreaseStock(Connection connection, int bookId, int quantity) throws SQLException {
        String sql = """
            UPDATE "books"
            SET "stock_quantity" = "stock_quantity" - ?
            WHERE "id" = ?
            AND "stock_quantity" >= ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, bookId);
            preparedStatement.setInt(3, quantity);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean increaseStock(Connection connection, int bookId, int quantity) throws SQLException {
        String sql = """
            UPDATE "books"
            SET "stock_quantity" = "stock_quantity" + ?
            WHERE "id" = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, bookId);

            return preparedStatement.executeUpdate() > 0;
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

    private static List<Book> findByField(String field, String value) throws SQLException {
        if (!SEARCHABLE_FIELDS.contains(field)) {
            throw new IllegalArgumentException("Invalid search field.");
        }
        try (Connection connection = DatabaseManager.getConnection()) {
            String sql = """
                SELECT * FROM "books" 
                WHERE %s LIKE ? ;
            """.formatted(field);
            List<Book> books = new ArrayList<>(); 
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + value + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        books.add(mapRowToBook(resultSet));
                    }
                }
            }
            return books;
        }
    }
}
