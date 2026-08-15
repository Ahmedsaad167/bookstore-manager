package io.github.ahmedsaad167.bookstoremanager.ui;

import java.util.List;
import java.sql.SQLException;

import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.search.BookSearchCriteria;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.BookFormDialog;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.BookSearchDialog;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.UpdateBookDialog;
import io.github.ahmedsaad167.bookstoremanager.ui.dialog.StockAdjustmentDialog;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BookView {
    private final BookService bookService;
    private TextField searchField;

    private final TableView<Book> table = new TableView<>();

    public BookView(BookService bookService) {
        this.bookService = bookService;
    }

    public BorderPane build() {

        BorderPane root = new BorderPane();

        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        root.setTop(createHeader());
        root.setCenter(createTable());

        loadBooks();

        return root;
    }

    private HBox createTopBar() {

        Label title = new Label("الكتب");

        title.setStyle("""
            -fx-font-size: 24px;
            -fx-font-weight: bold;
        """);

        Button addButton = new Button("إضافة كتاب");
        Button editButton = new Button("تعديل");
        Button deleteButton = new Button("حذف");
        Button increaseStockButton = new Button("إضافة للمخزون");
        Button decreaseStockButton = new Button("سحب من المخزون");
        Button refreshButton = new Button("تحديث");
        
        addButton.setOnAction(event -> addBook());
        editButton.setOnAction(event -> updateSelectedBook());
        deleteButton.setOnAction(event -> deleteSelectedBook());
        increaseStockButton.setOnAction(event -> increaseSelectedBookStock());
        decreaseStockButton.setOnAction(event -> decreaseSelectedBookStock());
        refreshButton.setOnAction(event -> loadBooks());

        HBox bar = new HBox(10, title, addButton, editButton, deleteButton, increaseStockButton, decreaseStockButton,refreshButton);

        bar.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        bar.setStyle("""
            -fx-padding: 15px;
        """);


        return bar;
    }

    private TableView<Book> createTable() {

        TableColumn<Book, Integer> idColumn = new TableColumn<>("الرقم");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("العنوان");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> categoryColumn = new TableColumn<>("التصنيف");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("المؤلف");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        TableColumn<Book, Double> purchasePriceColumn = new TableColumn<>("سعر الشراء");
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

        TableColumn<Book, Double> sellingPriceColumn = new TableColumn<>("سعر البيع");
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("المخزون");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        TableColumn<Book, String> materialTypeColumn = new TableColumn<>("نوع المادة");
        materialTypeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMaterialType().getDisplayName()));
        
        TableColumn<Book, String> publisherColumn = new TableColumn<>("الناشر");
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        
        TableColumn<Book, Integer> publicationYearColumn = new TableColumn<>("سنة النشر");
        publicationYearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        
        TableColumn<Book, String> ageGroupColumn = new TableColumn<>("الفئة العمرية");
        ageGroupColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAgeGroup().getDisplayName()));

        TableColumn<Book, String> notesColumn = new TableColumn<>("ملاحظات");
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        table.getColumns().add(idColumn);
        table.getColumns().add(titleColumn);
        table.getColumns().add(categoryColumn);
        table.getColumns().add(authorColumn);
        table.getColumns().add(purchasePriceColumn);
        table.getColumns().add(sellingPriceColumn);
        table.getColumns().add(stockColumn);
        table.getColumns().add(materialTypeColumn);
        table.getColumns().add(publisherColumn);
        table.getColumns().add(publicationYearColumn);
        table.getColumns().add(isbnColumn);
        table.getColumns().add(ageGroupColumn);
        table.getColumns().add(notesColumn);

        table.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        return table;
    }

    private void loadBooks() {

        try {
            List<Book> books = bookService.getAllBooks();

            table.setItems(FXCollections.observableArrayList(books));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBook() {

        BookFormDialog dialog = new BookFormDialog();

        Book book = dialog.show();

        if (book == null) {
            return;
        }

        try {

            bookService.addBook(book);
            loadBooks();
        } catch (SQLException e) {
            showError("حدث خطأ أثناء إضافة الكتاب:\n" + e.getMessage());
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

    private void updateSelectedBook() {

        Book selectedBook =
            table.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showError(
                "يرجى اختيار كتاب أولًا."
            );

            return;
        }

        UpdateBookDialog dialog =
            new UpdateBookDialog(
                bookService,
                selectedBook
            );

        boolean updated =
            dialog.showAndWait();

        if (updated) {
            loadBooks();
        }
    }

    private void deleteSelectedBook() {

        Book selectedBook = table.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("يرجى اختيار كتاب أولًا");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("تأكيد الحذف");
        confirmation.setHeaderText("حذف الكتاب");
        confirmation.setContentText(selectedBook.getTitle() + ":هل أنت متأكد من حذف الكتاب " + "\nلا يمكن التراجع عن هذه العملية\n");
        confirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        ButtonType deleteButton = new ButtonType("حذف", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("إلغاء", ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmation.getButtonTypes().setAll(deleteButton, cancelButton);

        confirmation.showAndWait().ifPresent(result -> {
            if (result == deleteButton) {
                performDelete(selectedBook);
            }
        });
    }

    private void increaseSelectedBookStock() {

        Book selectedBook =
            table.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showError(
                "يرجى اختيار كتاب أولًا."
            );

            return;
        }

        StockAdjustmentDialog dialog =
            new StockAdjustmentDialog(
                bookService,
                selectedBook,
                true
            );

        boolean updated =
            dialog.showAndWait();

        if (updated) {
            loadBooks();
        }
    }

    private void decreaseSelectedBookStock() {

        Book selectedBook =
            table.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showError(
                "يرجى اختيار كتاب أولًا."
            );

            return;
        }

        StockAdjustmentDialog dialog =
            new StockAdjustmentDialog(
                bookService,
                selectedBook,
                false
            );

        boolean updated =
            dialog.showAndWait();

        if (updated) {
            loadBooks();
        }
    }

    private void performDelete(Book book) {
        try {
            boolean deleted = bookService.deleteBook(book.getId());
            if (deleted) {
                loadBooks();
                showInformation("تم حذف الكتاب بنجاح.");
            }
            else {
                showError("لم يتم العثور على الكتاب.");
            }
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError("حدث خطأ في قاعدة البيانات:\n" + e.getMessage());
        }
    }

    private void showInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("تمت العملية.");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

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
        searchField.setPromptText("ابحث بعنوان الكتاب...");
        searchField.setPrefWidth(300);

        Button searchButton = new Button("بحث");
        Button clearButton = new Button("مسح");
        Button advancedSearchButton = new Button("بحث متقدم");

        searchButton.setOnAction(
            event -> searchBooks()
        );

        clearButton.setOnAction(
            event -> {
                searchField.clear();
                loadBooks();
            }
        );

        searchField.setOnAction(
            event -> searchBooks()
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

    private void searchBooks() {

        String text =
            searchField.getText().trim();

        if (text.isBlank()) {
            loadBooks();
            return;
        }

        BookSearchCriteria criteria =
            new BookSearchCriteria();

        criteria.setTitle(text);

        try {

            List<Book> books =
                bookService.search(criteria);

            table.setItems(
                FXCollections.observableArrayList(books)
            );

        } catch (SQLException e) {

            showError(
                "حدث خطأ أثناء البحث:\n"
                + e.getMessage()
            );
        }
    }

    private void advancedSearch() {

        BookSearchDialog dialog =
            new BookSearchDialog();

        BookSearchCriteria criteria =
            dialog.showAndWait();

        if (criteria == null) {
            return;
        }

        try {

            List<Book> books =
                bookService.search(criteria);

            table.setItems(
                FXCollections.observableArrayList(books)
            );

        } catch (NumberFormatException e) {

            showError(
                "تأكد من إدخال السنة والأسعار بشكل صحيح."
            );

        } catch (SQLException e) {

            showError(
                "حدث خطأ أثناء البحث:\n"
                + e.getMessage()
            );
        }
    }
}
