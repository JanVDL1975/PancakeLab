package org.pancakelab.repository;

import org.pancakelab.service.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class OrderRepository {
    // Save order using DatabaseService
    public void saveOrder(String building, int room) {
        String sql = "INSERT INTO orders (id, building, room) VALUES (?, ?, ?)";
        UUID orderId = UUID.randomUUID();

        try {
            // Using DatabaseService to perform the update
            DatabaseService.executeUpdate(sql, orderId, building, room);
            System.out.println("Order created with ID: " + orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


