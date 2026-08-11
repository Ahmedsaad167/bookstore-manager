package io.github.ahmedsaad167.bookstoremanager.search;

import java.time.LocalDateTime;

import io.github.ahmedsaad167.bookstoremanager.model.OrderStatus;

public class OrderSearchCriteria {
    private Integer orderId;
    
    private String customerName;
    private String customerPhone;

    private Integer bookId;
    private String bookTitle;

    private OrderStatus orderStatus;

    private LocalDateTime minDate;
    private LocalDateTime maxDate;

    private Double minPrice;
    private Double maxPrice;
    
    public Integer getOrderId() {
        return orderId;
    }
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public Integer getBookId() {
        return bookId;
    }
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public LocalDateTime getMinDate() {
        return minDate;
    }
    public void setMinDate(LocalDateTime minDate) {
        this.minDate = minDate;
    }
    public LocalDateTime getMaxDate() {
        return maxDate;
    }
    public void setMaxDate(LocalDateTime maxDate) {
        this.maxDate = maxDate;
    }
    public Double getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    public Double getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    
}
