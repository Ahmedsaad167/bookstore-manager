package io.github.ahmedsaad167.bookstoremanager.ui;

import java.util.List;
import java.sql.SQLException;

import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.service.BookService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class BookView {
    private final BookService bookService;

    private final TableView<Book> table = new TableView<>();

    public BookView(BookService bookService) {
        this.bookService = bookService;
    }

    public BorderPane build() {

        BorderPane root = new BorderPane();

        root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        root.setTop(createTopBar());
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
        Button refreshButton = new Button("تحديث");

        refreshButton.setOnAction(event -> loadBooks());

        HBox bar = new HBox(10, title, addButton, editButton, deleteButton, refreshButton);

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
}
