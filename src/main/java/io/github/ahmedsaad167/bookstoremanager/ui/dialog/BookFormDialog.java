package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BookFormDialog {
    
    public Book show() {

        Dialog<Book> dialog = new Dialog<>();

        dialog.setTitle("إضافة كتاب");
        dialog.setHeaderText("إضافة كتاب جديد");

        dialog.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        ButtonType addButtonType = new ButtonType("إضافة", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        TextField categoryField = new TextField();
        TextField authorField = new TextField();

        TextField purchasePriceField = new TextField();
        TextField sellingPriceField = new TextField();
        TextField stockQuantityField = new TextField();

        ComboBox<MaterialType> materialTypeBox = new ComboBox<>();

        materialTypeBox.getItems().addAll(MaterialType.values());

        materialTypeBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem (MaterialType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        materialTypeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem (MaterialType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        TextField publisherField = new TextField();
        TextField publicationYearField = new TextField();
        TextField isbnField = new TextField();

        ComboBox<AgeGroup> ageGroupBox = new ComboBox<>();

        ageGroupBox.getItems().addAll(AgeGroup.values());

        ageGroupBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem (AgeGroup item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        ageGroupBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(AgeGroup item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDisplayName());
            }
        });

        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(3);

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));
        form.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        int row = 0;
        
        addField(form, row++, "العنوان", titleField);
        addField(form, row++, "التصنيف", categoryField);
        addField(form, row++, "المؤلف", authorField);

        addField(form, row++, "سعر الشراء", purchasePriceField);
        addField(form, row++, "سعر البيع", sellingPriceField);
        addField(form, row++, "المخزون", stockQuantityField);

        addField(form, row++, "نوع المادة", materialTypeBox);

        addField(form, row++, "الناشر", publisherField);
        addField(form, row++, "سنة النشر", publicationYearField);
        addField(form, row++, "ISBN", isbnField);

        addField(form, row++, "الفئة العمرية", ageGroupBox);

        addField(form, row++, "الملاحظات", notesArea);

        VBox container = new VBox(form);

        container.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        dialog.getDialogPane().setContent(container);

        dialog.setResultConverter(button -> {

            if (button != addButtonType) {
                return null;
            }

            try {
                String title = titleField.getText().trim();

                String category = categoryField.getText().trim();

                String author = authorField.getText().trim();

                double purchasePrice = Double.parseDouble(
                    purchasePriceField.getText().trim()
                );

                double sellingPrice = Double.parseDouble(
                    sellingPriceField.getText().trim()
                );

                int stockQuantity = Integer.parseInt(
                    stockQuantityField.getText().trim()
                );

                MaterialType materialType = materialTypeBox.getValue();

                AgeGroup ageGroup = ageGroupBox.getValue();

                if (materialType == null) {
                    showError("اختر نوع المادة.");
                    return null;
                }

                if (ageGroup == null) {
                    showError("اختر الفئة العمرية.");
                    return null;
                }

                Book book = new Book(
                    title,
                    category,
                    author,
                    purchasePrice,
                    sellingPrice,
                    stockQuantity,
                    materialType,
                    ageGroup
                );

                book.setPublisher(emptyToNull(publisherField.getText()));
                
                String year = publicationYearField.getText().trim();
                book.setPublicationYear(year.isBlank() ? 0 : Integer.parseInt(year));

                book.setIsbn(emptyToNull(isbnField.getText()));

                book.setNotes(emptyToNull(notesArea.getText()));

                return book;
            } catch (NumberFormatException e) {
                showError("تأكد من إدخال الأسعار والكميات والأرقام بشكل صحيح.");
                return null;
            }
        });
        return dialog.showAndWait().orElse(null);
    }

    private void addField(GridPane grid, int row, String labelText, Control control) {
        
        Label label = new Label(labelText + ":");

        grid.add(label, 1, row);
        grid.add(control, 0, row);

        control.setPrefWidth(250);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("خطأ");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        alert.showAndWait();
    }

    private String emptyToNull(String value) {

        String trimmed = value.trim();

        return trimmed.isBlank() ? null : trimmed;
    }
}
