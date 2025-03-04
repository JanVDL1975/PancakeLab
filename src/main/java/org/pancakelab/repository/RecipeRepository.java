package org.pancakelab.repository;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeRepository {

    public void addRecipe(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO Recipe (name, description) VALUES (?, ?) RETURNING id";

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeQuery(sql, rs -> {
                if (rs.next()) {
                    recipe.setId(rs.getInt("id")); // Retrieve the 'id' correctly
                }
            }, recipe.getName(), recipe.getDescription());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT id, name, description FROM Recipe";

        try {
            // Using DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    recipes.add(new Recipe(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description")
                    ));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public void saveRecipe(String name) {
        String sql = "INSERT INTO pancakes (name, description) VALUES (?, ?)";

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeUpdate(sql, name, "Custom pancake recipe");
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

        try {
            // Using DatabaseService to perform the insert operation
            DatabaseService.executeUpdate(sql, pancakeName, ingredientName);
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

        try {
            // Using DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    ingredients.add(rs.getString("name"));
                }
            }, pancakeName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public List<Ingredient> getIngredientsForRecipe(UUID recipeId) throws SQLException {
        String sql = """
        SELECT i.name, ri.quantity, ri.unit 
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
                    String name = rs.getString("name");  // Use "name" from ingredients table
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



}



