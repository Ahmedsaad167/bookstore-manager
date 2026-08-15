package io.github.ahmedsaad167.bookstoremanager.ui.dialog;

import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;
import io.github.ahmedsaad167.bookstoremanager.search.BookSearchCriteria;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class BookSearchDialog {

    private TextField titleField;
    private TextField categoryField;
    private TextField authorField;
    private TextField publisherField;
    private TextField isbnField;
    private TextField notesField;
    private TextField publicationYearField;
    private TextField minPriceField;
    private TextField maxPriceField;

    private ComboBox<MaterialType> materialTypeBox;
    private ComboBox<AgeGroup> ageGroupBox;
    private ComboBox<String> availabilityBox;

    public BookSearchCriteria showAndWait() {

        Dialog<BookSearchCriteria> dialog = new Dialog<>();

        dialog.setTitle("بحث متقدم");
        dialog.setHeaderText("البحث في الكتب");

        dialog.getDialogPane().setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        ButtonType searchButton =
            new ButtonType(
                "بحث",
                ButtonBar.ButtonData.OK_DONE
            );

        dialog.getDialogPane().getButtonTypes().addAll(
            searchButton,
            ButtonType.CANCEL
        );

        dialog.getDialogPane().setContent(
            createForm()
        );

        dialog.setResultConverter(button -> {

            if (button == searchButton) {
                return buildCriteria();
            }

            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private GridPane createForm() {

        GridPane form = new GridPane();

        form.setNodeOrientation(
            NodeOrientation.RIGHT_TO_LEFT
        );

        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(20));

        initializeFields();

        int row = 0;

        addField(form, "العنوان:", titleField, row++);
        addField(form, "التصنيف:", categoryField, row++);
        addField(form, "المؤلف:", authorField, row++);
        addField(form, "الناشر:", publisherField, row++);
        addField(form, "ISBN:", isbnField, row++);
        addField(form, "الملاحظات:", notesField, row++);
        addField(form, "سنة النشر:", publicationYearField, row++);
        addField(form, "أقل سعر بيع:", minPriceField, row++);
        addField(form, "أعلى سعر بيع:", maxPriceField, row++);
        addField(form, "نوع المادة:", materialTypeBox, row++);
        addField(form, "الفئة العمرية:", ageGroupBox, row++);
        addField(form, "التوفر:", availabilityBox, row++);

        return form;
    }

    private void initializeFields() {

        titleField = new TextField();
        categoryField = new TextField();
        authorField = new TextField();
        publisherField = new TextField();
        isbnField = new TextField();
        notesField = new TextField();

        publicationYearField = new TextField();
        minPriceField = new TextField();
        maxPriceField = new TextField();

        materialTypeBox = new ComboBox<>();
        materialTypeBox.getItems().add(null);
        materialTypeBox.getItems().addAll(
            MaterialType.values()
        );

        ageGroupBox = new ComboBox<>();
        ageGroupBox.getItems().add(null);
        ageGroupBox.getItems().addAll(
            AgeGroup.values()
        );

        availabilityBox = new ComboBox<>();

        availabilityBox.getItems().addAll(
            "الكل",
            "متوفر",
            "غير متوفر"
        );

        availabilityBox.setValue("الكل");
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

    private BookSearchCriteria buildCriteria() {

        BookSearchCriteria criteria =
            new BookSearchCriteria();

        criteria.setTitle(
            emptyToNull(titleField.getText())
        );

        criteria.setCategory(
            emptyToNull(categoryField.getText())
        );

        criteria.setAuthor(
            emptyToNull(authorField.getText())
        );

        criteria.setPublisher(
            emptyToNull(publisherField.getText())
        );

        criteria.setIsbn(
            emptyToNull(isbnField.getText())
        );

        criteria.setNotes(
            emptyToNull(notesField.getText())
        );

        criteria.setPublicationYear(
            parseInteger(publicationYearField.getText())
        );

        criteria.setMinSellingPrice(
            parseDouble(minPriceField.getText())
        );

        criteria.setMaxSellingPrice(
            parseDouble(maxPriceField.getText())
        );

        criteria.setMaterialType(
            materialTypeBox.getValue()
        );

        criteria.setAgeGroup(
            ageGroupBox.getValue()
        );

        criteria.setAvailable(
            getAvailability()
        );

        return criteria;
    }

    private Boolean getAvailability() {

        return switch (availabilityBox.getValue()) {

            case "متوفر" -> true;

            case "غير متوفر" -> false;

            default -> null;
        };
    }

    private String emptyToNull(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isBlank()
            ? null
            : trimmed;
    }

    private Integer parseInteger(String value) {

        String text = emptyToNull(value);

        if (text == null) {
            return null;
        }

        return Integer.parseInt(text);
    }

    private Double parseDouble(String value) {

        String text = emptyToNull(value);

        if (text == null) {
            return null;
        }

        return Double.parseDouble(text);
    }
}