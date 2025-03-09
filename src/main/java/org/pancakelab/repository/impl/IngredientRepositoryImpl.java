package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.repository.interfaces.IngredientRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientRepositoryImpl implements IngredientRepository {
    private Connection connection = DatabaseService.getConnection();

    public IngredientRepositoryImpl() throws SQLException {
    }

    // Save ingredient using DatabaseService
    public void saveIngredient(String name, double quantity, String unit) {
        String sql = "INSERT INTO ingredients (name, quantity, unit) VALUES (?, ?, ?)";
        try {
            DatabaseService.executeUpdate(sql, name, quantity, unit);
            System.out.println("Ingredient added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveIngredient(Ingredient ingredient) {
        saveIngredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
    }

    // Get all ingredients using DatabaseService
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT name, quantity, unit FROM ingredients";
        try (ResultSet rs = DatabaseService.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                ingredients.add(new Ingredient(name, quantity, unit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    @Override
    public Ingredient findById(UUID id) {
        String sql = "SELECT id, name, quantity, unit FROM ingredients WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Ingredient ingredient = new Ingredient("Ingredient " + resultSet.getString("name"), resultSet.getDouble("quantity"), resultSet.getString("unit"));
                    ingredient.setId(UUID.fromString(resultSet.getString("id")));
                    ingredient.setName(resultSet.getString("name"));
                    ingredient.setQuantity(resultSet.getDouble("quantity"));
                    ingredient.setUnit(resultSet.getString("unit"));
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in real applications
        }
        return null; // Return null if no ingredient is found
    }

    @Override
    public List<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT name, quantity, unit FROM ingredients";
        try (ResultSet rs = DatabaseService.executeQuery(sql)) {
            while (rs.next()) {
                String name = rs.getString("name");
                double quantity = rs.getDouble("quantity");
                String unit = rs.getString("unit");
                ingredients.add(new Ingredient(name, quantity, unit));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    @Override
    public void save(Ingredient ingredient) {
        saveIngredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM ingredients WHERE id = ?";

        Connection connection = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No ingredient found with ID: " + id);
            } else {
                System.out.println("Ingredient deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in real-world cases
        }
    }
}


