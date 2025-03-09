package org.pancakelab.repository.impl;

import org.pancakelab.model.orders.Order;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.repository.interfaces.OrderRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepositoryImpl implements OrderRepository {
    Connection connection = DatabaseService.getConnection();

    public OrderRepositoryImpl() throws SQLException {
    }

    // Save order using DatabaseService
    public void saveOrder(String building, int room) {
        String sql = "INSERT INTO orders (id, building, room) VALUES (?, ?, ?)";
        UUID orderId = UUID.randomUUID();

        try {
            // Using DatabaseService to perform the update
            DatabaseService.executeUpdate(sql, orderId, building, room);
            System.out.println("Order created with ID: " + orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order findById(UUID id) {
        String sql = "SELECT id, building, room FROM orders WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Order(
                            UUID.fromString(resultSet.getString("id")),
                            resultSet.getString("building"),
                            resultSet.getInt("room"),
                            findPancakesByOrderId(id) // Fetch related pancakes
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT id, building, room FROM orders";
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID orderId = UUID.fromString(resultSet.getString("id"));
                orders.add(new Order(
                        orderId,
                        resultSet.getString("building"),
                        resultSet.getInt("room"),
                        findPancakesByOrderId(orderId) // Fetch related pancakes
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void save(Order order) {
        String sql = "INSERT INTO orders (id, building, room) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET building = EXCLUDED.building, room = EXCLUDED.room";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, order.getId());
            statement.setString(2, order.getBuilding());
            statement.setInt(3, order.getRoom());
            statement.executeUpdate();

            saveOrderPancakes(order); // Save related pancakes
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No order found with ID: " + id);
            } else {
                System.out.println("Order deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Pancake> findPancakesByOrderId(UUID orderId) {
        String sql = "SELECT pancake_id FROM order_pancakes WHERE order_id = ?";
        List<Pancake> pancakes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pancakes.add(new Pancake(UUID.fromString(resultSet.getString("pancake_id")), null));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pancakes;
    }

    private void saveOrderPancakes(Order order) {
        String deleteSql = "DELETE FROM order_pancakes WHERE order_id = ?";
        String insertSql = "INSERT INTO order_pancakes (order_id, pancake_id) VALUES (?, ?)";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setObject(1, order.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Pancake pancake : order.getOrderPancakes()) {
                insertStatement.setObject(1, order.getId());
                insertStatement.setObject(2, pancake.getId());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


