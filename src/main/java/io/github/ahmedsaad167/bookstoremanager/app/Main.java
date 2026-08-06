package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;

import java.sql.SQLException;


public class Main {
    
    public static void main(String[] args) {
        try {
            BookDao dao = new BookDao();


            boolean deleted = dao.deleteById(1);

            System.out.println(deleted);

            System.out.println(dao.findById(1));
        } 
catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    