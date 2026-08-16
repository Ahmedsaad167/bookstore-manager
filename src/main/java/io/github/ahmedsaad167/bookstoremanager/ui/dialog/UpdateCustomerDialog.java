package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.service.CustomerService;

import java.sql.SQLException;
import java.util.Optional;

import io.github.ahmedsaad167.bookstoremanager.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class UpdateCustomerDialog {
    
    private final CustomerService customerService;
    private final Customer customer;
    
    private TextField nameField;
    private TextField usernameField;
    private TextField phoneField;
    private TextField emailField;
    private TextField addressField;
    private TextField notesField;
    
    public UpdateCustomerDialog(CustomerService customerService, Customer customer) {
        this.customerService = customerService;
        this.customer = customer;
    }
    
    public boolean showAndWait() {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("تعديل العميل");
        dialog.setHeaderText("تعديل بيانات العميل");

        dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        ButtonType saveButton = new ButtonType("حفظ", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);
        
        dialog.getDialogPane().setContent(createForm());

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() != saveButton) {
            return false;
        }

        return updateCustomer();

    }    

    private GridPane createForm() {

        GridPane form = new GridPane();

        form.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        initializeFields();

        int row = 0;

        addField(form, "الاسم:", nameField, row++);

        addField(form, "اسم المستخدم:", usernameField, row++);

        addField(form, "الهاتف:", phoneField, row++);

        addField(form, "البريد الإلكتروني :", emailField, row++);

        addField(form, "العنوان:", addressField, row++);

        addField(form, "الملاحظات:", notesField, row++);

        return form;
    }

    private void initializeFields() {
        nameField = new TextField(customer.getName());
        usernameField = new TextField(customer.getUsername());
        phoneField = new TextField(customer.getPhone());
        emailField = new TextField(customer.getEmail());
        addressField = new TextField(customer.getAddress());
        notesField = new TextField(customer.getNotes());
    }

    private void addField(GridPane form, String labelText, Control control, int row) {

        Label label = new Label(labelText);

        form.add(label, 1, row);
        form.add(control, 0 , row);

        control.setPrefWidth(250);
    }

    private boolean updateCustomer() {

        customer.setName(emptyToNull(nameField.getText()));
        customer.setUsername(emptyToNull(usernameField.getText()));
        customer.setPhone(emptyToNull(phoneField.getText()));
        customer.setEmail(emptyToNull(emailField.getText()));
        customer.setAddress(emptyToNull(addressField.getText()));
        customer.setNotes(emptyToNull(notesField.getText()));

        try {
            return customerService.updateCustomer(customer);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
            return false;
        } catch (SQLException e) {
            showError(":حدث خطأ أثناء تعديل العميل\n" + e.getMessage());

            return false;
        }
    }

    private String emptyToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isBlank() ? null : trimmed;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("خطأ");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        
        alert.showAndWait();
    }
}


