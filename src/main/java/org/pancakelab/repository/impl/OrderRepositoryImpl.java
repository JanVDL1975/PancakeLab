package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.orders.Order;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.impl.PancakeRecipeImpl;
import org.pancakelab.model.recipes.Recipe;
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

            // Save the associated pancakes AFTER the order is persisted
            saveOrderPancakes(order);
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
        String sql = "SELECT p.id, p.name, p.recipe_id, r.name AS recipe_name, r.description " +
                "FROM pancakes p " +
                "JOIN order_pancakes op ON p.id = op.pancake_id " +
                "JOIN recipes r ON p.recipe_id = r.id " +  // Join with recipes to get full details
                "WHERE op.order_id = ?";

        List<Pancake> pancakes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Construct the Recipe object properly
                    Recipe recipe = new Recipe(
                            resultSet.getObject("recipe_id", UUID.class),
                            resultSet.getString("recipe_name"),
                            resultSet.getString("description"),
                            new IngredientsList("Empty List") // Placeholder; fetch actual ingredients if needed
                    );

                    PancakeRecipe pancakeRecipe = new PancakeRecipeImpl(
                            orderId,
                            new IngredientsList("Empty List").getIngredients(),
                            UUID.fromString("")
                    );

                    // Construct the Pancake object
                    Pancake pancake = new Pancake(
                            resultSet.getObject("id", UUID.class),
                            resultSet.getString("name"),
                            pancakeRecipe
                    );

                    pancakes.add(pancake);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException ea) {
            ea.printStackTrace();
        }
        finally {
            return pancakes;
        }
    }




    private void saveOrderPancakes(Order order) {
        String insertSql = "INSERT INTO order_pancakes (order_id, pancake_id) " +
                "VALUES (?, ?) " +
                "ON CONFLICT (order_id, pancake_id) DO NOTHING";

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


