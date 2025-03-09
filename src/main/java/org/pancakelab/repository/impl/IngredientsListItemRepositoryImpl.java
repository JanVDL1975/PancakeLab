package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.repository.interfaces.IngredientsListItemRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientsListItemRepositoryImpl implements IngredientsListItemRepository {
    private final Connection connection;

    public IngredientsListItemRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addIngredient(UUID listId, UUID ingredientId, double quantity, String unit) {
        String sql = "INSERT INTO ingredients_list_items (ingredients_list_id, ingredient_id, quantity, unit) " +
                "VALUES (?, ?, ?, ?) ON CONFLICT (ingredients_list_id, ingredient_id) " +
                "DO UPDATE SET quantity = EXCLUDED.quantity, unit = EXCLUDED.unit";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, listId);
            statement.setObject(2, ingredientId);
            statement.setDouble(3, quantity);
            statement.setString(4, unit);
            statement.executeUpdate();
            System.out.println("Ingredient added/updated in list successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeIngredient(UUID listId, UUID ingredientId) {
        String sql = "DELETE FROM ingredients_list_items WHERE ingredients_list_id = ? AND ingredient_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, listId);
            statement.setObject(2, ingredientId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Ingredient removed from list successfully.");
            } else {
                System.out.println("No matching ingredient found in the list.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ingredient> findIngredientsByList(UUID listId) {
        String sql = "SELECT i.id, i.name, ili.quantity, ili.unit " +
                "FROM ingredients_list_items ili " +
                "JOIN ingredients i ON ili.ingredient_id = i.id " +
                "WHERE ili.ingredients_list_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, listId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ingredients.add(new Ingredient(
                            resultSet.getInt(resultSet.getString("id")),
                            resultSet.getString("name"),
                            resultSet.getDouble("quantity"),
                            resultSet.getString("unit")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}

