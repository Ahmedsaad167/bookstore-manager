package io.github.ahmedsaad167.bookstoremanager.search;

import io.github.ahmedsaad167.bookstoremanager.model.AgeGroup;
import io.github.ahmedsaad167.bookstoremanager.model.MaterialType;

public class BookSearchCriteria {
    private String title;
    private String category;
    private String author;
    private String publisher;
    private String isbn;
    private String notes;

    private MaterialType materialType;
    private AgeGroup ageGroup;
    
    private Integer publicationYear;

    private Double minSellingPrice;
    private Double maxSellingPrice;

    private Boolean available;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Double getMinSellingPrice() {
        return minSellingPrice;
    }

    public void setMinSellingPrice(Double minSellingPrice) {
        this.minSellingPrice = minSellingPrice;
    }

    public Double getMaxSellingPrice() {
        return maxSellingPrice;
    }

    public void setMaxSellingPrice(Double maxSellingPrice) {
        this.maxSellingPrice = maxSellingPrice;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    
}
