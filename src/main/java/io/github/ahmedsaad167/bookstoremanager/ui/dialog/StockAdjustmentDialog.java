package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class StockAdjustmentDialog {

    private final BookService bookService;
    private final Book book;
    private final boolean increase;

    private TextField quantityField;

    public StockAdjustmentDialog(
        BookService bookService,
        Book book,
        boolean increase
    ) {
        this.bookService = bookService;
        this.book = book;
        this.increase = increase;
    }

    public boolean showAndWait() {

        Dialog<ButtonType> dialog = new Dialog<>();

        String operation = increase
            ? "إضافة للمخزون"
            : "سحب من المخزون";

        dialog.setTitle(operation);

        dialog.setHeaderText(
            operation + " - " + book.getTitle()
        );

        dialog.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        ButtonType confirmButton = new ButtonType(
            increase ? "إضافة" : "سحب",
            ButtonBar.ButtonData.OK_DONE
        );

        dialog.getDialogPane().getButtonTypes().addAll(
            confirmButton,
            ButtonType.CANCEL
        );

        dialog.getDialogPane().setContent(
            createForm()
        );

        Button confirmDialogButton =
            (Button) dialog.getDialogPane()
                .lookupButton(confirmButton);

        confirmDialogButton.setOnAction(event -> {

            if (adjustStock()) {
                dialog.setResult(ButtonType.OK);
                dialog.close();
            }
        });

        return dialog.showAndWait()
            .map(result -> result == ButtonType.OK)
            .orElse(false);
    }

    private GridPane createForm() {

        GridPane form = new GridPane();

        form.setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        Label bookLabel = new Label("الكتاب:");

        Label bookValue = new Label(
            book.getTitle()
        );

        Label currentStockLabel =
            new Label("المخزون الحالي:");

        Label currentStockValue =
            new Label(
                String.valueOf(
                    book.getStockQuantity()
                )
            );

        Label quantityLabel =
            new Label(
                increase
                    ? "الكمية المراد إضافتها:"
                    : "الكمية المراد سحبها:"
            );

        quantityField = new TextField();

        quantityField.setPromptText(
            "أدخل الكمية"
        );

        quantityField.setPrefWidth(200);

        form.add(bookLabel, 1, 0);
        form.add(bookValue, 0, 0);

        form.add(currentStockLabel, 1, 1);
        form.add(currentStockValue, 0, 1);

        form.add(quantityLabel, 1, 2);
        form.add(quantityField, 0, 2);

        return form;
    }

    private boolean adjustStock() {

        String input =
            quantityField.getText().trim();

        if (input.isBlank()) {

            showError(
                "يرجى إدخال الكمية."
            );

            return false;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            showError(
                "الكمية يجب أن تكون رقمًا صحيحًا."
            );

            return false;
        }

        try {

            boolean success;

            if (increase) {

                success =
                    bookService.increaseStock(
                        book.getId(),
                        quantity
                    );

            } else {

                success =
                    bookService.decreaseStock(
                        book.getId(),
                        quantity
                    );
            }

            if (!success) {

                showError(
                    increase
                        ? "فشلت إضافة المخزون."
                        : "فشل سحب المخزون."
                );

                return false;
            }

            return true;

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

    private void showError(String message) {

        Alert alert =
            new Alert(Alert.AlertType.ERROR);

        alert.setTitle("خطأ");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        alert.showAndWait();
    }
}