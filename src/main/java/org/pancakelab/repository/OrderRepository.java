package org.pancakelab.repository;

import org.pancakelab.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class OrderRepository {
    public void saveOrder(String building, int room) {
        String sql = "INSERT INTO orders (id, building, room) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            UUID orderId = UUID.randomUUID();
            stmt.setObject(1, orderId);
            stmt.setString(2, building);
            stmt.setInt(3, room);
            stmt.executeUpdate();

            System.out.println("Order created with ID: " + orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

