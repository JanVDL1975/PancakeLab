package org.pancakelab.repository;

import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PancakeRepository {

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
    }
}

