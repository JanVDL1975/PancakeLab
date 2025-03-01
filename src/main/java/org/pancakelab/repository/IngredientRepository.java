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
    public void saveIngredient(String name, double quantity, String unit) {
        String sql = "INSERT INTO ingredients (name, quantity, unit) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, quantity);
            stmt.setString(3, unit);
            stmt.executeUpdate();

            System.out.println("Ingredient added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveIngredient(Ingredient ingredient) {
        saveIngredient(ingredient.getName(), ingredient.getQuantity(), ingredient.getUnit());
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT name, quantity, unit FROM ingredients";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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

