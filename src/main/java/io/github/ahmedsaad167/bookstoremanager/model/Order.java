package io.github.ahmedsaad167.bookstoremanager.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Order {
    private int id;
    private int customerId;

    private LocalDateTime orderDate;
    
    private OrderStatus orderStatus;
    
    private double totalPrice;
    private double discount;
    private double priceAfterDiscount;
    
    private String notes;
    
    private List<OrderItem> items;
    
    public Order(
        int id,
        int customerId,
        LocalDateTime orderDate,
        OrderStatus orderStatus,
        double totalPrice,
        double discount,
        double priceAfterDiscount,
        String notes) {

        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.notes = notes;
        this.items = new ArrayList<>();
    }
    public Order(int customerId) {
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = OrderStatus.PENDING;
        this.discount = 0;
        this.items = new ArrayList<>();
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        recalculatePrices();
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public String getNotes() {
        return notes;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null.");
        }

        items.add(item);
        recalculatePrices();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        recalculatePrices();
    }

    public void recalculatePrices() {
        totalPrice = 0;

        for (OrderItem item : items) {
            totalPrice += item.getTotalPrice();
        }

        priceAfterDiscount = totalPrice - discount;
    }
}
