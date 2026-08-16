package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class CustomerFormDialog {
    
    private TextField nameField;
    private TextField usernameField;
    private TextField phoneField;
    private TextField emailField;
    private TextField addressField;
    private TextField notesField;

    public Customer showAndWait() {

        Dialog<Customer> dialog = new Dialog<>();

        dialog.setTitle("إضافة عميل");
        dialog.setHeaderText("إضافة عميل جديد");
        dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        ButtonType saveButton = new ButtonType("حفظ", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(createForm());

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return buildCustomer();
            }
            return null;
        });
        return dialog.showAndWait().orElse(null);
    }

    private GridPane createForm() {

        GridPane form = new GridPane();
        
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        initializeFields();

        int row = 0;

        addField(form, "الاسم:", nameField, row++);
        addField(form, "اسم المستخدم:", usernameField, row++);
        addField(form, "الهاتف:", phoneField, row++);
        addField(form, "البريد الإلكتروني:", emailField, row++);
        addField(form, "العنوان:", addressField, row++);
        addField(form, "الملاحظات:", notesField, row++);

        return form;
    }

    private void initializeFields() {
        nameField = new TextField();
        usernameField = new TextField();
        phoneField = new TextField();
        emailField = new TextField();
        addressField = new TextField();
        notesField = new TextField();
    }

    private void addField(GridPane form, String labelText, Control control, int row) {
        
        Label label = new Label(labelText);

        form.add(label, 1, row);
        form.add(control, 0, row);

        control.setPrefWidth(250);
    }

    private Customer buildCustomer() {
        Customer customer = new Customer(emptyToNull(nameField.getText()), emptyToNull(usernameField.getText()), emptyToNull(phoneField.getText()));

        customer.setEmail(emptyToNull(emailField.getText()));
        customer.setAddress(emptyToNull(addressField.getText()));
        customer.setNotes(emptyToNull(notesField.getText()));

        return customer;

    }

    private String emptyToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}

