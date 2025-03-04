package org.pancakelab.repository;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.service.DatabaseService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository {
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
}


