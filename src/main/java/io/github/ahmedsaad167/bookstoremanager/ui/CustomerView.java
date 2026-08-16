package io.github.ahmedsaad167.bookstoremanager.ui;

import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;
import io.github.ahmedsaad167.bookstoremanager.model.Customer;
import io.github.ahmedsaad167.bookstoremanager.search.CustomerSearchCriteria;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.CustomerFormDialog;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.CustomerSearchDialog;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.UpdateCustomerDialog;


import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomerView {
    
    private final CustomerService customerService;
    private final TableView<Customer> table = new TableView<>();

    private TextField searchField;

    public CustomerView(CustomerService customerService) {
        this.customerService = customerService;
    }

    public BorderPane build() {

        BorderPane root = new BorderPane();

        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        root.setTop(createHeader());
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
        editButton.setOnAction(event -> updateSelectedCustomer());
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

    private void updateSelectedCustomer() {

        Customer selectedCustomer =
            table.getSelectionModel()
                .getSelectedItem();

        if (selectedCustomer == null) {

            showError(
                "يرجى اختيار عميل أولًا."
            );

            return;
        }

        UpdateCustomerDialog dialog =
            new UpdateCustomerDialog(
                customerService,
                selectedCustomer
            );

        boolean updated =
            dialog.showAndWait();

        if (updated) {

            loadCustomers();

            showInformation(
                "تم تعديل بيانات العميل بنجاح."
            );
        }
    }

    private void showInformation(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("تمت العملية");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane()
            .setNodeOrientation(
                NodeOrientation.RIGHT_TO_LEFT
            );

        alert.showAndWait();
    }


    private VBox createHeader() {

        VBox header = new VBox(10);

        header.setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        header.getChildren().addAll(
            createTopBar(),
            createSearchBar()
        );

        header.setStyle("""
            -fx-padding: 15px;
        """);

        return header;
    }

    private HBox createSearchBar() {

        Label label = new Label(":بحث");

        searchField = new TextField();
        searchField.setPromptText("ابحث برقم الهاتف...");
        searchField.setPrefWidth(300);

        Button searchButton = new Button("بحث");
        Button clearButton = new Button("مسح");
        Button advancedSearchButton = new Button("بحث متقدم");

        searchButton.setOnAction(
            event -> searchCustomers()
        );

        clearButton.setOnAction(
            event -> {
                searchField.clear();
                loadCustomers();
            }
        );

        searchField.setOnAction(
            event -> searchCustomers()
        );

        advancedSearchButton.setOnAction(event -> advancedSearch());

        HBox bar = new HBox(
            10,
            label,
            searchField,
            searchButton,
            clearButton,
            advancedSearchButton
        );

        bar.setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        return bar;
    }

    private void searchCustomers() {

        String text = searchField.getText();

        if (text == null || text.isBlank()) {
            loadCustomers();
            return;
        }

        CustomerSearchCriteria criteria = new CustomerSearchCriteria();

        criteria.setPhone(text);

        try {
            List<Customer> customers = customerService.search(criteria);

            table.setItems(FXCollections.observableArrayList(customers));
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError(":خطأ أثناء البحث\n" + e.getMessage());
        }
    }    

    private void advancedSearch() {

        CustomerSearchDialog dialog =
            new CustomerSearchDialog();

        CustomerSearchCriteria criteria =
            dialog.showAndWait();

        if (criteria == null) {
            return;
        }

        try {

            List<Customer> customers =
                customerService.search(criteria);

            table.setItems(
                FXCollections.observableArrayList(customers)
            );

        } catch (IllegalArgumentException e) {

            showError(e.getMessage());

        } catch (SQLException e) {

            showError(
                "خطأ أثناء البحث:\n"
                + e.getMessage()
            );
        }
    }
}
