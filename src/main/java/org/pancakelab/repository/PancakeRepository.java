package org.pancakelab.repository;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.Ingredients;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.pancakes.impl.PancakeRecipeImpl;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.*;

public class PancakeRepository {
    private final Connection connection;

    public PancakeRepository(Connection connection) {
        this.connection = connection;
    }
    public void savePancake(String name, String description) {
        String sql = "INSERT INTO pancakes (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();

            System.out.println("Pancake added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    public List<String> getAllPancakes() {
        String sql = "SELECT name FROM pancakes";
        List<String> pancakes = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pancakes.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pancakes;
    }*/

    public void addPancake(Pancake pancake) throws SQLException {
        String sql = "INSERT INTO pancakes (id, recipe_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, pancake.getId());
            stmt.setObject(2, pancake.getRecipe().getOrderId()); // Using orderId as recipe_id
            stmt.executeUpdate();
        }
    }

    public List<Pancake> getAllPancakes() throws SQLException {
        List<Pancake> pancakes = new ArrayList<>();
        String sql = "SELECT p.id, r.id AS recipe_id FROM pancakes p JOIN Recipe r ON p.recipe_id = r.id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UUID pancakeId = rs.getObject("id", UUID.class);
                UUID recipeId = rs.getObject("recipe_id", UUID.class);

                // Fetch ingredients separately
                Ingredients ingredients = getIngredientsForRecipe(recipeId);
                PancakeRecipe recipe = new PancakeRecipeImpl(recipeId, ingredients);

                pancakes.add(new Pancake(pancakeId, recipe));
            }
        }
        return pancakes;
    }

    // Helper method to get ingredients
    private Ingredients getIngredientsForRecipe(UUID recipeId) throws SQLException {
        String sql = "SELECT ingredient_name FROM Recipe_Ingredient WHERE recipe_id = ?";
        Set<Ingredient> ingredientSet = new HashSet<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, recipeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredientSet.add(new Ingredient(rs.getString("ingredient_name")));
                }
            }
        }
        return new Ingredients(ingredientSet);
    }


}

