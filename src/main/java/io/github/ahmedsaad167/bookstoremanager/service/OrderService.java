package io.github.ahmedsaad167.bookstoremanager.service;

import java.util.Set;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.SQLException;

import io.github.ahmedsaad167.bookstoremanager.dao.CustomerDao;
import io.github.ahmedsaad167.bookstoremanager.model.Book;
import io.github.ahmedsaad167.bookstoremanager.dao.BookDao;
import io.github.ahmedsaad167.bookstoremanager.dao.OrderDao;
import io.github.ahmedsaad167.bookstoremanager.model.OrderItem;
import io.github.ahmedsaad167.bookstoremanager.dao.OrderItemDao;
import io.github.ahmedsaad167.bookstoremanager.database.DatabaseManager;
import io.github.ahmedsaad167.bookstoremanager.model.Order;


public class OrderService {
    private final CustomerDao customerDao;
    private final BookDao bookDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;

    public OrderService(CustomerDao customerDao, BookDao bookDao, OrderDao orderDao, OrderItemDao orderItemDao) {
        this.customerDao = customerDao;
        this.bookDao = bookDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
    }

    public int createOrder(Order order) throws SQLException {
        validateOrder(order);
        
        try (Connection connection = DatabaseManager.getConnection()) {
            try {
                connection.setAutoCommit(false);
                
                validateCustomerExists(connection , order.getCustomerId());

                prepareOrderItems(connection, order);

                int orderId = orderDao.save(connection, order);

                saveOrderItems(connection, order, orderId);

                decreaseBooksStock(connection, order);

                connection.commit();
                
                return orderId;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        
        if (order.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }
        
        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date cannot be null.");
        }
        
        if (order.getOrderStatus() == null) {
            throw new IllegalArgumentException("Order status cannot be null.");
        }
        
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        
        if (order.getDiscount() < 0) {
            throw new IllegalArgumentException("Discount cannot be negative.");
        }

        for (OrderItem item : order.getItems()) {
            validateOrderItem(item);
        }

        validateNoDeplicateBooks(order);

    }

    private void validateOrderItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null.");
        }
        
        if (item.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }
        
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Order item quantity must be greater than zero.");
        }
    }

    private void prepareOrderItems(Connection connection, Order order) throws SQLException {
        for (OrderItem item : order.getItems()) {
            Book book = bookDao.findById(connection, item.getBookId());

            if (book == null) {
                throw new IllegalArgumentException("Book not found: " + item.getBookId());
            }

            if (book.getStockQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }

            item.setUnitPrice(book.getSellingPrice());
        }

        order.recalculatePrices();

        if (order.getDiscount() > order.getTotalPrice()) {
            throw new IllegalArgumentException("Discount cannot be greater than total price.");
        }
    }

    private void saveOrderItems(Connection connection, Order order, int orderId) throws SQLException {
        for(OrderItem item : order.getItems()) {
            item.setOrderId(orderId);
            orderItemDao.save(connection, item);
        }
    }

    private void decreaseBooksStock(Connection connection, Order order) throws SQLException {
        for (OrderItem item : order.getItems()) {
            boolean decreased = bookDao.decreaseStock(connection, item.getBookId(), item.getQuantity());
            if (!decreased) {
                throw new IllegalStateException("Failed to decrease stock for book: " + item.getBookId());
            }
        }
    }

    private void validateCustomerExists(Connection connection, int customerId) throws SQLException {
        if (customerDao.findById(connection, customerId) == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
    }

    private void validateNoDeplicateBooks(Order order) {
        Set<Integer> bookIds = new HashSet<>();

        for (OrderItem item : order.getItems()) {
            if (!bookIds.add(item.getBookId())) {
                throw new IllegalArgumentException("Book appears more than once in the order: " + item.getBookId());
            }
        }
    }
}
