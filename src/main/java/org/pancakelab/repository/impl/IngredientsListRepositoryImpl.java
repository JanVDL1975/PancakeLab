package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.repository.interfaces.IngredientsListRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientsListRepositoryImpl implements IngredientsListRepository {
    private final Connection connection;

    public IngredientsListRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IngredientsList findById(UUID id) {
        String sql = "SELECT id, name FROM ingredients_lists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    List<Ingredient> ingredients = findIngredientsForList(id);
                    return new IngredientsList(id, name, ingredients);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding IngredientsList by ID: " + e.getMessage());
        }
        return null; // Return null if not found
    }

    @Override
    public List<IngredientsList> findAll() {
        String sql = "SELECT id, name FROM ingredients_lists";
        List<IngredientsList> lists = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                String name = resultSet.getString("name");
                lists.add(new IngredientsList(id, name, findIngredientsForList(id)));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all IngredientsLists: " + e.getMessage());
        }
        return lists;
    }

    @Override
    public void save(IngredientsList list) {
        String insertSql = "INSERT INTO ingredients_lists (id, name) VALUES (?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";

        try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
            statement.setObject(1, list.getId());
            statement.setString(2, list.getName());
            statement.executeUpdate();

            saveIngredientsForList(list);
        } catch (SQLException e) {
            System.err.println("Error saving IngredientsList: " + e.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        String deleteListSql = "DELETE FROM ingredients_lists WHERE id = ?";
        String deleteItemsSql = "DELETE FROM ingredients_list_items WHERE ingredients_list_id = ?";

        try (PreparedStatement deleteItemsStatement = connection.prepareStatement(deleteItemsSql);
             PreparedStatement deleteListStatement = connection.prepareStatement(deleteListSql)) {

            deleteItemsStatement.setObject(1, id);
            deleteItemsStatement.executeUpdate();

            deleteListStatement.setObject(1, id);
            int rowsAffected = deleteListStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No IngredientsList found with ID: " + id);
            } else {
                System.out.println("IngredientsList deleted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting IngredientsList: " + e.getMessage());
        }
    }

    private List<Ingredient> findIngredientsForList(UUID listId) {
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
            System.err.println("Error fetching ingredients for list: " + e.getMessage());
        }
        return ingredients;
    }

    private void saveIngredientsForList(IngredientsList list) {
        String deleteSql = "DELETE FROM ingredients_list_items WHERE ingredients_list_id = ?";
        String insertSql = "INSERT INTO ingredients_list_items (ingredients_list_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Start transaction

            // Delete existing ingredients for the list
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setObject(1, list.getId());
                deleteStatement.executeUpdate();
            }

            // Insert new ingredients
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                for (Ingredient ingredient : list.getIngredients()) {
                    insertStatement.setObject(1, list.getId());
                    insertStatement.setObject(2, ingredient.getId());
                    insertStatement.setDouble(3, ingredient.getQuantity());
                    insertStatement.setString(4, ingredient.getUnit());
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback if there's an error
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Error saving ingredients for list: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitEx) {
                System.err.println("Failed to reset auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }
}


