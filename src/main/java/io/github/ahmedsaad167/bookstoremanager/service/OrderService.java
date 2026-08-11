package io.github.ahmedsaad167.bookstoremanager.service;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

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

    public Order getOrderById(int id) throws SQLException {
        validateOrderId(id);

        try (Connection connection = DatabaseManager.getConnection()) {
            Order order = orderDao.findById(connection, id);

            if (order == null) {
                return null;
            }

            loadOrderItems(connection, order);

            return order;
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection()) {
            List<Order> orders = orderDao.findAll(connection);
            
            for (Order order : orders) {
                loadOrderItems(connection, order);
            }

            return orders;
        }
    }

    public boolean updateOrder(Order order) throws SQLException {
        validateOrder(order);
        validateOrderId(order.getId());

        try (Connection connection = DatabaseManager.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Order oldOrder = orderDao.findById(connection, order.getId());

                if (oldOrder == null) {
                    throw new IllegalArgumentException(
                        "Order not found: " + order.getId()
                    );
                }

                loadOrderItems(connection, oldOrder);

                restoreOldStock(connection, oldOrder);

                validateCustomerExists(connection, order.getCustomerId());

                prepareOrderItems(connection, order);

                orderItemDao.deleteByOrderId(connection, order.getId());

                boolean updated = orderDao.update(connection, order);
                
                if (!updated) {
                    throw new SQLException("Failed to update order: " + order.getId());
                }

                saveOrderItems(connection, order, order.getId());

                decreaseBooksStock(connection, order);

                connection.commit();

                return true;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public boolean deleteOrder(int id) throws SQLException {
        validateOrderId(id);
        try (Connection connection = DatabaseManager.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Order order = orderDao.findById(connection, id);
                if (order == null) {
                    return false;
                }
                loadOrderItems(connection, order);

                restoreOldStock(connection, order);

                orderItemDao.deleteByOrderId(connection, id);

                boolean deleted = orderDao.deleteById(connection, id);

                if (!deleted) {
                    throw new SQLException("Failed to delete order: " + id);
                }

                connection.commit();

                return true;
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

    private void validateOrderId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid order ID.");
        }
    }

    private void loadOrderItems(Connection connection, Order order) throws SQLException {
        List<OrderItem> items = orderItemDao.findByOrderId(connection, order.getId());
        for (OrderItem item : items) {
            order.addItem(item);
        }
    }

    private void restoreOldStock(
        Connection connection,
        Order oldOrder) throws SQLException {

        for (OrderItem item : oldOrder.getItems()) {

            boolean increased = bookDao.increaseStock(
                connection,
                item.getBookId(),
                item.getQuantity()
            );

            if (!increased) {
                throw new IllegalStateException(
                    "Failed to restore stock for book: "
                    + item.getBookId()
                );
            }
        }
    }
}
