package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.dao.CustomerDao;
import io.github.ahmedsaad167.bookstoremanager.dao.OrderDao;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;
import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;
import io.github.ahmedsaad167.bookstoremanager.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class BookstoreApplication extends Application {
    
    @Override
    public void start(Stage stage) {

        BookDao bookDao = new BookDao();
        CustomerDao customerDao = new CustomerDao();
        OrderDao orderDao = new OrderDao();

        BookService bookService = new BookService(bookDao, orderDao);
        CustomerService customerService = new CustomerService(customerDao);

        MainView mainView = new MainView(bookService, customerService);

        Scene scene = new Scene(mainView.build(), 1000, 650);

        stage.setTitle("نظام إدارة المكتبة");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
