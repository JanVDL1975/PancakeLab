package org.pancakelab.repository.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.impl.PancakeRecipeImpl;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.repository.interfaces.PancakeRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.*;

public class PancakeRepositoryImpl implements PancakeRepository {
    private final Connection connection;

    public PancakeRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public PancakeRepositoryImpl() throws SQLException {
        connection = DatabaseService.getConnection();
    }

    // Save Pancake using DatabaseService
    public void savePancake(String name, String description) {
        String sql = "INSERT INTO pancakes (name, description) VALUES (?, ?)";

        try {
            // Using DatabaseService to execute the update (INSERT operation)
            DatabaseService.executeUpdate(sql, name, description);
            System.out.println("Pancake added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Pancake with recipe association using DatabaseService
    public void addPancake(Pancake pancake) throws SQLException {
        String sql = "INSERT INTO pancakes (id, name, recipe_id) VALUES (?, ?, ?)";

        try {
            // Using DatabaseService to execute the update
            DatabaseService.executeUpdate(sql, pancake.getId(), pancake.getName() ,pancake.getRecipe().getOrderId()); // Using orderId as recipe_id
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all Pancakes with recipe associations
    public List<Pancake> getAllPancakes() throws SQLException {
        List<Pancake> pancakes = new ArrayList<>();
        String sql = "SELECT * FROM pancakes";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UUID pancakeId = rs.getObject("id", UUID.class);
                UUID recipeId = rs.getObject("recipe_id", UUID.class);
                String name = rs.getString("name");

                // Fetch ingredients separately
                List<Ingredient> ingredients = getIngredientsForRecipe(recipeId);
                PancakeRecipe recipe = new PancakeRecipeImpl(recipeId, ingredients);

                pancakes.add(new Pancake(pancakeId, name, recipe));
            }
        }
        return pancakes;
    }

    // Helper method to get ingredients
    public List<Ingredient> getIngredientsForRecipe(UUID recipeId) throws SQLException {
        String sql = """
        SELECT i.name AS ingredient_name, ri.quantity, ri.unit 
        FROM ingredients i
        JOIN recipe_ingredient ri ON i.id = ri.ingredient_id
        WHERE ri.recipe_id = ?
    """;

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("ingredient_name");  // Correct alias usage
                    double quantity = rs.getDouble("quantity");
                    String unit = rs.getString("unit");

                    ingredients.add(new Ingredient(name, quantity, unit));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }


    // Get all Pancakes by name (returns list of pancake names)
    public List<String> getAllPancakesByName() {
        List<String> pancakeNames = new ArrayList<>();
        String sql = "SELECT 'name' FROM pancakes";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pancakeNames.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pancakeNames;
    }

    @Override
    public Pancake findById(UUID id) {
        String sql = "SELECT id, recipe_id FROM pancakes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Pancake(
                            UUID.fromString(resultSet.getString("id")),
                            (PancakeRecipe) findRecipeById(resultSet.getString("recipe_id")) // Fetch associated Recipe
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }

    @Override
    public List<Pancake> findAll() {
        String sql = "SELECT id, recipe_id FROM pancakes";
        List<Pancake> pancakes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                pancakes.add(new Pancake(
                        UUID.fromString(resultSet.getString("id")),
                        (PancakeRecipe) findRecipeById(resultSet.getString("recipe_id"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pancakes;
    }

    @Override
    public void save(Pancake pancake) {
        String sql = "INSERT INTO pancakes (id, recipe_id) VALUES (?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET recipe_id = EXCLUDED.recipe_id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, pancake.getId());
            statement.setInt(2, pancake.getRecipe().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM pancakes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("No pancake found with ID: " + id);
            } else {
                System.out.println("Pancake deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Recipe findRecipeById(String recipeId) {
        String sql = "SELECT id, name, description FROM recipes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, recipeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Recipe(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            null // Ingredients list can be fetched separately if needed
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found
    }
}


