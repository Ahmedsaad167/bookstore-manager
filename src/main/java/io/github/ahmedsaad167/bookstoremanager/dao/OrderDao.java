package io.github.ahmedsaad167.bookstoremanager.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.github.ahmedsaad167.bookstoremanager.model.Order;
import io.github.ahmedsaad167.bookstoremanager.model.OrderStatus;

public class OrderDao {
    
    public int save(Connection connection, Order order) throws SQLException {
        String sql = """
            INSERT INTO "orders" (
                "customer_id",
                "order_date",
                "order_status",
                "total_price",
                "discount",
                "price_after_discount",
                "notes"
            )
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapOrderToRow(preparedStatement, order);
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 0) {
                throw new SQLException("Saving order failed.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Failed to retrieve generated order ID.");
            }
        }
    }

    public Order findById(Connection connection, int id) throws SQLException {
        String sql = """
            SELECT * FROM "orders"
            WHERE "id" = ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToOrder(resultSet);
                }
            }
        }
        return null;
    }
    

    public List<Order> findAll(Connection connection) throws SQLException {
        String sql = """
            SELECT * FROM "orders";
        """;

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapRowToOrder(resultSet));
                }
            }
        }
        return orders;
    }

    public boolean update(Connection connection, Order order) throws SQLException {
        String sql = """
            UPDATE "orders"
            SET "customer_id" = ?,
                "order_date" = ?,
                "order_status" = ?,
                "total_price" = ?,
                "discount" = ?,
                "price_after_discount" = ?,
                "notes" = ?
            WHERE "id" = ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            mapOrderToRow(preparedStatement, order);
            preparedStatement.setInt(8, order.getId());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(Connection connection, int id) throws SQLException {
        String sql = """
            DELETE FROM "orders"
            WHERE "id" = ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        }
    }


    private static void mapOrderToRow(PreparedStatement preparedStatement, Order order) throws SQLException {
        preparedStatement.setInt(1, order.getCustomerId());
        preparedStatement.setString(2, order.getOrderDate().toString());
        preparedStatement.setString(3, order.getOrderStatus().name());
        preparedStatement.setDouble(4, order.getTotalPrice());
        preparedStatement.setDouble(5, order.getDiscount());
        preparedStatement.setDouble(6, order.getPriceAfterDiscount());
        preparedStatement.setString(7, order.getNotes());
    }

    private static Order mapRowToOrder(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int customerId = resultSet.getInt("customer_id");

        LocalDateTime orderDate = LocalDateTime.parse(resultSet.getString("order_date"));

        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        double totalPrice = resultSet.getDouble("total_price");
        double discount = resultSet.getDouble("discount");
        double priceAfterDiscount = resultSet.getDouble("price_after_discount");

        String notes = resultSet.getString("notes");

        return new Order(
            id,
            customerId,
            orderDate,
            orderStatus,
            totalPrice,
            discount,
            priceAfterDiscount,
            notes
        );
    }
}
