package io.github.ahmedsaad167.bookstoremanager.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainView {
    
    public TabPane build() {

        TabPane tabPane = new TabPane();

        Tab booksTab = new Tab("الكتب");
        Tab customersTab = new Tab("العملاء");
        Tab ordersTab = new Tab("الطلبات");
        Tab reportsTab = new Tab("التقارير");

        booksTab.setClosable(false);
        customersTab.setClosable(false);
        ordersTab.setClosable(false);
        reportsTab.setClosable(false);

        booksTab.setContent(createPlaceholder("قسم الكتب"));
        customersTab.setContent(createPlaceholder("قسم العملاء"));
        ordersTab.setContent(createPlaceholder("قسم الطلبات"));
        reportsTab.setContent(createPlaceholder("قسم التقارير"));

        tabPane.getTabs().addAll(
            booksTab,
            customersTab,
            ordersTab,
            reportsTab
        );

        return tabPane;
    }

    private StackPane createPlaceholder(String text) {

        Label label = new Label(text);

        label.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;"
        );

        StackPane container = new StackPane(label);

        StackPane.setAlignment(label, Pos.CENTER);

        return container;
    }


}
