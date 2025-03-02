package org.pancakelab.repository;

import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {
    private final Connection connection;

    public RecipeRepository(Connection connection) {
        this.connection = connection;
    }

    public void addRecipe(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO Recipe (name, description) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                recipe.setId(rs.getInt("id"));
            }
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT id, name, description FROM Recipe";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recipes.add(new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        }
        return recipes;
    }

    public void saveRecipe(String name) {
        String sql = "INSERT INTO pancakes (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, "Custom pancake recipe");
            stmt.executeUpdate();

            System.out.println("Recipe added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addIngredientToRecipe(String pancakeName, String ingredientName) {
        String sql = """
            INSERT INTO pancake_ingredients (pancake_id, ingredient_id)
            VALUES (
                (SELECT id FROM pancakes WHERE name = ?),
                (SELECT id FROM ingredients WHERE name = ?)
            )
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pancakeName);
            stmt.setString(2, ingredientName);
            stmt.executeUpdate();

            System.out.println("Ingredient " + ingredientName + " added to " + pancakeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getIngredientsForPancake(String pancakeName) {
        String sql = """
            SELECT i.name FROM ingredients i
            JOIN pancake_ingredients pi ON i.id = pi.ingredient_id
            JOIN pancakes p ON p.id = pi.pancake_id
            WHERE p.name = ?
        """;
        List<String> ingredients = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pancakeName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }
}

