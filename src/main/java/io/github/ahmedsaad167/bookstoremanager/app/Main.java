package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;

import java.sql.SQLException;


public class Main {
    
    public static void main(String[] args) {
        try {
            Book cleanCode = new Book(
                "Clean Code",
                "Programming",
                "Robert C. Martin",
                250,
                350,
                10,
                MaterialType.PAPER,
                AgeGroup.ALL_AGES
            );
            BookDao dao = new BookDao();
            int id = dao.save(cleanCode);
            System.out.println("Book saved with id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    