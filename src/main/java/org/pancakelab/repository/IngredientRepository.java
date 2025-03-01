package org.pancakelab.repository;

import org.pancakelab.service.DatabaseService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}

