package io.github.ahmedsaad167.bookstoremanager.ui;

import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;
import io.github.ahmedsaad167.bookstoremanager.model.Customer;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.CustomerFormDialog;

import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class CustomerView {
    
    private final CustomerService customerService;
    private final TableView<Customer> table = new TableView<>();

    public CustomerView(CustomerService customerService) {
        this.customerService = customerService;
    }

    public BorderPane build() {

        BorderPane root = new BorderPane();

        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        root.setTop(createTopBar());
        root.setCenter(createTable());

        loadCustomers();

        return root;
    }


    private HBox createTopBar() {

        Label title = new Label("العملاء");

        title.setStyle("""
            -fx-font-size: 24px;
            -fx-font-weight: bold;
        """);

        Button addButton = new Button("إضافة عميل");
        Button editButton = new Button("تعديل");
        Button deleteButton = new Button("حذف");
        Button refreshButton = new Button("تحديث");
        
        addButton.setOnAction(event -> addCustomer());
        // editButton.setOnAction(event -> updateSelectedCustomer());
        // deleteButton.setOnAction(event -> deleteSelectedCustomer());
        refreshButton.setOnAction(event -> loadCustomers());

        HBox bar = new HBox(10, title, addButton, editButton, deleteButton, refreshButton);

        bar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        bar.setStyle("""
            -fx-padding: 15px;
        """);


        return bar;
    }

    private TableView<Customer> createTable() {

        TableColumn<Customer, Integer> idColumn = new TableColumn<>("الرقم");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("الاسم");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> usernameColumn = new TableColumn<>("اسم المستخدم");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Customer, String> phoneColumn = new TableColumn<>("الهاتف");
        phoneColumn.setCellValueFactory( new PropertyValueFactory<>("phone"));

        TableColumn<Customer, String> emailColumn = new TableColumn<>("البريد الإلكتروني");
        emailColumn.setCellValueFactory( new PropertyValueFactory<>("email"));

        TableColumn<Customer, String> addressColumn = new TableColumn<>("العنوان");
        addressColumn.setCellValueFactory( new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> notesColumn = new TableColumn<>("ملاحظات");
        notesColumn.setCellValueFactory( new PropertyValueFactory<>("notes"));

        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(phoneColumn);
        table.getColumns().add(emailColumn);
        table.getColumns().add(addressColumn);
        table.getColumns().add(notesColumn);

        table.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        return table;

    }

    private void loadCustomers() {

        try {
            List<Customer> customers = customerService.getAllCustomers();

            table.setItems(FXCollections.observableArrayList(customers));
        } catch (SQLException e) {
            showError("خطأ أثناء تحميل العملاء\n" + e.getMessage());
        }
    }


    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("خطأ");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        alert.showAndWait();
    }

    private void addCustomer() {
        CustomerFormDialog dialog = new CustomerFormDialog();

        Customer customer = dialog.showAndWait();

        if (customer == null) {
            return;
        }

        try {
            customerService.addCustomer(customer);

            loadCustomers();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError(":حدث خطأ أثناء إضافة العميل\n" + e.getMessage());
        }
    }

}
