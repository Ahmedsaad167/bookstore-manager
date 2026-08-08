package io.github.ahmedsaad167.bookstoremanager.app;

import java.sql.SQLException;
import java.util.List;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseInitializer;
import io.github.ahmedsaad167.bookstoremanager.model.Book;

public class Main {

    public static void main(String[] args) {
        BookDao dao = new BookDao();
        
        try {
            DatabaseInitializer.initialize();

            System.out.println("========== Find By Title ==========");
            List<Book> books = dao.findByTitle("Clean Code");
            printBooks(books);

            System.out.println("\n========== Find By Category ==========");
            books = dao.findByCategory("Programming");
            printBooks(books);

            System.out.println("\n========== Find By Author ==========");
            books = dao.findByAuthor("Robert C. Martin");
            printBooks(books);

            System.out.println("\n========== Find By Publisher ==========");
            books = dao.findByPublisher("Prentice Hall");
            printBooks(books);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book book : books) {
            System.out.println("---------------------------");
            System.out.println("ID: " + book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Category: " + book.getCategory());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publisher: " + book.getPublisher());
        }
    }
}