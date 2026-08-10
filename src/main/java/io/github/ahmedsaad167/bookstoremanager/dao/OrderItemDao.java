package io.github.ahmedsaad167.bookstoremanager.dao;

import java.util.ArrayList;
import java.util.List;

import io.github.ahmedsaad167.bookstoremanager.model.OrderItem;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemDao {
    public int save (Connection connection, OrderItem orderItem) throws SQLException {
        String sql = """
            INSERT INTO "order_items" (
                "order_id",
                "book_id",
                "quantity",
                "unit_price",
                "total_price"
            )
            VALUES (?, ?, ?, ?, ?);
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapOrderItemToRow(preparedStatement, orderItem);

            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected == 0) {
                throw new SQLException("Saving order item failed.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Failed to retrieve generated order item ID.");
            }
        }
    }

    public OrderItem findById(Connection connection, int id) throws SQLException {
        String sql = """
            SELECT * FROM "order_items"
            WHERE "id" = ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToOrderItem(resultSet);
                }
            }
        }
        return null;
    }

    public List<OrderItem> findByOrderId(Connection connection, int orderId) throws SQLException {
        String sql = """
            SELECT * FROM "order_items"
            WHERE "order_id" = ? ;
        """;

        List<OrderItem> orderItems = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orderItems.add(mapRowToOrderItem(resultSet));
                }
            }
        }
        return orderItems;
    }

    public List<OrderItem> findAll(Connection connection) throws SQLException {
        String sql = """
            SELECT * FROM "order_items";
        """;

        List<OrderItem> orderItems = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    orderItems.add(mapRowToOrderItem(resultSet));
                }
            }
        }
        return orderItems;
    }

    public boolean update(Connection connection, OrderItem orderItem) throws SQLException {
        String sql = """
            UPDATE "order_items"
            SET "order_id" = ?,
                "book_id" = ?,
                "quantity" = ?,
                "unit_price" = ?,
                "total_price" = ?
            WHERE "id" = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            mapOrderItemToRow(preparedStatement, orderItem);
            preparedStatement.setInt(6, orderItem.getId());

            return preparedStatement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(Connection connection, int id) throws SQLException {
        String sql = """
            DELETE FROM "order_items"
            WHERE "id" = ? ;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        }
    }

    private static void mapOrderItemToRow(
        PreparedStatement preparedStatement,
        OrderItem orderItem) throws SQLException {

        preparedStatement.setInt(1, orderItem.getOrderId());
        preparedStatement.setInt(2, orderItem.getBookId());
        preparedStatement.setInt(3, orderItem.getQuantity());
        preparedStatement.setDouble(4, orderItem.getUnitPrice());
        preparedStatement.setDouble(5, orderItem.getTotalPrice());
    }

    private static OrderItem mapRowToOrderItem(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int orderId = resultSet.getInt("order_id");
        int bookId = resultSet.getInt("book_id");
        int quantity = resultSet.getInt("quantity");
        double unitPrice = resultSet.getDouble("unit_price");

        return new OrderItem(id, orderId, bookId, quantity, unitPrice);
    }
}
