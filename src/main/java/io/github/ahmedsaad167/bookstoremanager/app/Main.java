package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.model.Book;

import java.sql.SQLException;
import java.util.List;


public class Main {
    
    public static void main(String[] args) {
        try {
            BookDao dao = new BookDao();
            List<Book> books = dao.findAll();

            for (Book book : books) {
                System.out.println(book.getId() + " - " + book.getTitle());
            }
} 
catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    