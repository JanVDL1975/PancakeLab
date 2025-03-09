package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.repository.interfaces.IngredientsListRepository;

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
        String sql = "SELECT id FROM ingredients_lists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new IngredientsList(
                            UUID.fromString(resultSet.getString("id")),
                            findIngredientsForList(id)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

    @Override
    public List<IngredientsList> findAll() {
        String sql = "SELECT id FROM ingredients_lists";
        List<IngredientsList> lists = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID id = UUID.fromString(resultSet.getString("id"));
                lists.add(new IngredientsList(id, findIngredientsForList(id)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public void save(IngredientsList list) {
        String sql = "INSERT INTO ingredients_lists (id) VALUES (?) ON CONFLICT (id) DO NOTHING";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, list.getId());
            statement.executeUpdate();

            saveIngredientsForList(list); // Save associated ingredients
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM ingredients_lists WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No ingredients list found with ID: " + id);
            } else {
                System.out.println("Ingredients list deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Ingredient> findIngredientsForList(UUID listId) {
        String sql = "SELECT ingredient_id, quantity, unit FROM ingredients_list_items WHERE ingredients_list_id = ?";
        List<Ingredient> ingredients = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, listId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ingredients.add(new Ingredient(
                            UUID.fromString(resultSet.getString("ingredient_id")),
                            null, // Name can be fetched separately if needed
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

    private void saveIngredientsForList(IngredientsList list) {
        String deleteSql = "DELETE FROM ingredients_list_items WHERE ingredients_list_id = ?";
        String insertSql = "INSERT INTO ingredients_list_items (ingredients_list_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setObject(1, list.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
            for (Ingredient ingredient : list.getIngredientList()) {
                insertStatement.setObject(1, list.getId());
                insertStatement.setObject(2, ingredient.getId());
                insertStatement.setDouble(3, ingredient.getQuantity());
                insertStatement.setString(4, ingredient.getUnit());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

