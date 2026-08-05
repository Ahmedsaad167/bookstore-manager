package io.github.ahmedsaad167.bookstoremanager.model;

public class Book {
    private int id;
    private String title;
    private String category;
    private String author;

    private double purchasePrice;
    private double sellingPrice;

    private int stockQuantity; 

    private MaterialType materialType;

    private String publisher;

    private int publicationYear;

    private String isbn;

    private AgeGroup ageGroup;

    private String notes;
    
    public Book(String title, String category, String author, 
                double purchasePrice, double sellingPrice, int stockQuantity,
                MaterialType materialType, AgeGroup ageGroup) {
        setTitle(title);
        setCategory(category);
        setAuthor(author);
        setPurchasePrice(purchasePrice);
        setSellingPrice(sellingPrice);
        setStockQuantity(stockQuantity);
        setMaterialType(materialType);
        setAgeGroup(ageGroup);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAuthor() {
        return author;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public String getNotes() {
        return notes;
    }

    public void setTitle(String title) {
        validateRequiredText(title, "title");
        this.title = title;
    }

    public void setCategory(String category) {
        validateRequiredText(category, "category");
        this.category = category;
    }

    public void setAuthor(String author) {
        validateRequiredText(author, "author");
        this.author = author;
    }

    public void setPurchasePrice(double purchasePrice) {
        validateNonNegative(purchasePrice, "purchase price");
        this.purchasePrice = purchasePrice;
    }

    public void setSellingPrice(double sellingPrice) {
        validateNonNegative(sellingPrice, "selling price");
        this.sellingPrice = sellingPrice;
    }

    private void setStockQuantity(int stockQuantity) {
        validateNonNegative(stockQuantity, "stock quantity");
        this.stockQuantity = stockQuantity;
    }

    public void setMaterialType(MaterialType materialType) {
        validateNotNull(materialType, "material type");
        this.materialType = materialType;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public void setAgeGroup(AgeGroup ageGroup) {
        validateNotNull(ageGroup, "age group");
        this.ageGroup = ageGroup;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void addStock(int quantity) {
        validateNonNegative(quantity, "stock quantity");
        stockQuantity += quantity;
    }
    
    public void removeStock(int quantity) {
        validateNonNegative(quantity, "stock quantity");
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Cannot remove more books than available in stock.");
        }
        stockQuantity -= quantity;
    }

    private static void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Book " + fieldName + " cannot be null or blank.");
        }
    }

    private static void validateNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException("Book " + fieldName + " cannot be negative.");
        }
    }

    private static void validateNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException("Book " + fieldName + " cannot be negative.");
        }
    }

    private static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Book " + fieldName + " cannot be null.");
        }
    }

}
