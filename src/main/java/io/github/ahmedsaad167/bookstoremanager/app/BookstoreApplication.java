package io.github.ahmedsaad167.bookstoremanager.app;

import io.github.ahmedsaad167.bookstoremanager.ui.MainView;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class BookstoreApplication extends Application {
    
    @Override
    public void start(Stage stage) {

        MainView mainView = new MainView();

        Scene scene = new Scene(mainView.build(), 1000, 650);

        stage.setTitle("نظام إدارة المكتبة");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
