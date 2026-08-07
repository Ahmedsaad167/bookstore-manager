package io.github.ahmedsaad167.bookstoremanager.service;

import java.util.List;
import java.sql.SQLException;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.model.Book;


public class BookService {
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public int addBook(Book book) throws SQLException {
        validateBookForSale(book);

        return bookDao.save(book);
    }

    public boolean updateBook(Book book) throws SQLException {
        validateBookForSale(book);

        if (book.getId() <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }

        return bookDao.update(book);
    }

    public boolean deleteBook(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }
        // if (orderDao.existsByBookId(id)) {
        //     throw new IllegalArgumentException("Cannot delete a book that has existing orders.");
        // }

        return bookDao.deleteById(id);
    }

    public Book getBookById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }

        return bookDao.findById(id);
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDao.findAll();
    }

    public List<Book> searchByTitle(String title) throws SQLException {
        validateSearchText(title, "title");
        
        return bookDao.findByTitle(title);
    }

    public List<Book> searchByCategory(String category) throws SQLException {
        validateSearchText(category, "category");
        
        return bookDao.findByCategory(category);
    }

    public List<Book> searchByAuthor(String author) throws SQLException {
        validateSearchText(author, "author");
        
        return bookDao.findByAuthor(author);
    }

    public List<Book> searchByPublisher(String publisher) throws SQLException {
        validateSearchText(publisher, "publisher");
        
        return bookDao.findByPublisher(publisher);
    }

    private void validateBookForSale(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        if (book.getSellingPrice() < book.getPurchasePrice()) {
            throw new IllegalArgumentException("Selling price cannot be lower than purchase price.");
        }
    }

    private void validateSearchText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Search " + fieldName + " cannot be null or blank.");
        }
    }
}
