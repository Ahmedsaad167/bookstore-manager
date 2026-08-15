package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class UpdateBookDialog {

    private final BookService bookService;
    private final Book book;

    private TextField titleField;
    private TextField categoryField;
    private TextField authorField;
    private TextField purchasePriceField;
    private TextField sellingPriceField;
    private TextField stockField;
    private ComboBox<MaterialType> materialTypeBox;
    private TextField publisherField;
    private TextField publicationYearField;
    private TextField isbnField;
    private ComboBox<AgeGroup> ageGroupBox;
    private TextField notesField;

    public UpdateBookDialog(BookService bookService, Book book) {
        this.bookService = bookService;
        this.book = book;
    }

    public boolean showAndWait() {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("تعديل كتاب");
        dialog.setHeaderText(
            "تعديل بيانات الكتاب رقم " + book.getId()
        );

        dialog.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        ButtonType updateButton = new ButtonType(
            "حفظ التعديلات",
            ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            updateButton,
            ButtonType.CANCEL
        );

        dialog.getDialogPane().setContent(
            createForm()
        );

        Button updateDialogButton =
            (Button) dialog.getDialogPane()
                .lookupButton(updateButton);

        updateDialogButton.setOnAction(event -> {

            if (updateBook()) {
                dialog.setResult(ButtonType.OK);
                dialog.close();
            }
        });

        return dialog.showAndWait()
            .map(result -> result == ButtonType.OK)
            .orElse(false);
    }

    private GridPane createForm() {

        initializeFields();

        GridPane form = new GridPane();

        form.setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        int row = 0;

        addField(form, "العنوان:", titleField, row++);
        addField(form, "التصنيف:", categoryField, row++);
        addField(form, "المؤلف:", authorField, row++);

        addField(
            form,
            "سعر الشراء:",
            purchasePriceField,
            row++
        );

        addField(
            form,
            "سعر البيع:",
            sellingPriceField,
            row++
        );

        addField(
            form,
            "المخزون:",
            stockField,
            row++
        );

        addField(
            form,
            "نوع المادة:",
            materialTypeBox,
            row++
        );

        addField(
            form,
            "الناشر:",
            publisherField,
            row++
        );

        addField(
            form,
            "سنة النشر:",
            publicationYearField,
            row++
        );

        addField(
            form,
            "ISBN:",
            isbnField,
            row++
        );

        addField(
            form,
            "الفئة العمرية:",
            ageGroupBox,
            row++
        );

        addField(
            form,
            "ملاحظات:",
            notesField,
            row++
        );

        return form;
    }

    private void initializeFields() {

        titleField = new TextField(
            valueOrEmpty(book.getTitle())
        );

        categoryField = new TextField(
            valueOrEmpty(book.getCategory())
        );

        authorField = new TextField(
            valueOrEmpty(book.getAuthor())
        );

        purchasePriceField = new TextField(
            String.valueOf(book.getPurchasePrice())
        );

        sellingPriceField = new TextField(
            String.valueOf(book.getSellingPrice())
        );

        stockField = new TextField(
            String.valueOf(book.getStockQuantity())
        );

        publisherField = new TextField(
            valueOrEmpty(book.getPublisher())
        );

        publicationYearField = new TextField(
            String.valueOf(book.getPublicationYear())
        );

        isbnField = new TextField(
            valueOrEmpty(book.getIsbn())
        );

        notesField = new TextField(
            valueOrEmpty(book.getNotes())
        );

        materialTypeBox = new ComboBox<>();

        materialTypeBox.getItems().addAll(
            MaterialType.values()
        );

        materialTypeBox.setValue(
            book.getMaterialType()
        );

        ageGroupBox = new ComboBox<>();

        ageGroupBox.getItems().addAll(
            AgeGroup.values()
        );

        ageGroupBox.setValue(
            book.getAgeGroup()
        );
    }

    private void addField(
        GridPane form,
        String labelText,
        Control control,
        int row
    ) {

        Label label = new Label(labelText);

        form.add(label, 1, row);
        form.add(control, 0, row);

        control.setPrefWidth(250);
    }

    private boolean updateBook() {

        try {

            updateBookFromFields();

            return bookService.updateBook(book);

        } catch (NumberFormatException e) {

            showError(
                "تأكد من إدخال الأسعار والكميات والسنوات بشكل صحيح."
            );

            return false;

        } catch (IllegalArgumentException e) {

            showError(
                e.getMessage()
            );

            return false;

        } catch (SQLException e) {

            showError(
                "حدث خطأ في قاعدة البيانات:\n"
                + e.getMessage()
            );

            return false;
        }
    }

    private void updateBookFromFields() {

        book.setTitle(
            titleField.getText().trim()
        );

        book.setCategory(
            categoryField.getText().trim()
        );

        book.setAuthor(
            authorField.getText().trim()
        );

        book.setPurchasePrice(
            Double.parseDouble(
                purchasePriceField.getText().trim()
            )
        );

        book.setSellingPrice(
            Double.parseDouble(
                sellingPriceField.getText().trim()
            )
        );

        book.setMaterialType(
            materialTypeBox.getValue()
        );

        book.setPublisher(
            emptyToNull(
                publisherField.getText()
            )
        );

        String year =
            publicationYearField.getText().trim();

        book.setPublicationYear(
            year.isBlank()
                ? 0
                : Integer.parseInt(year)
        );

        book.setIsbn(
            emptyToNull(
                isbnField.getText()
            )
        );

        book.setAgeGroup(
            ageGroupBox.getValue()
        );

        book.setNotes(
            emptyToNull(
                notesField.getText()
            )
        );
    }

    private String valueOrEmpty(String value) {

        return value == null
            ? ""
            : value;
    }

    private String emptyToNull(String value) {

        String trimmed = value.trim();

        return trimmed.isBlank()
            ? null
            : trimmed;
    }

    private void showError(String message) {

        Alert alert = new Alert(
            Alert.AlertType.ERROR
        );

        alert.setTitle("خطأ");
        alert.setHeaderText("فشل تعديل الكتاب");
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        alert.showAndWait();
    }
}