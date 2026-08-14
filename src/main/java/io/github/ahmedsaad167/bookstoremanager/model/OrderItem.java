package io.github.ahmedsaad167.bookstoremanager.model;

public class OrderItem {
    private int id;
    private int orderId;
    private int bookId;
    private int quantity;

    private double unitPrice;
    private double totalPrice;

    public OrderItem(int id, int orderId, int bookId, int quantity, double unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }

    public OrderItem(int orderId, int bookId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Order item quantity must be greater than zero.");
        }
        
        this.quantity = quantity;
        recalculateTotalPrice();
    }
    
    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
        
        this.unitPrice = unitPrice;
        recalculateTotalPrice();
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void recalculateTotalPrice() {
        this.totalPrice = quantity * unitPrice;
    }
}
