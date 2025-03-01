package org.pancakelab.repository;

import org.pancakelab.model.venues.Building;
import org.pancakelab.model.venues.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueRepository {
    private final String url = "jdbc:postgresql://localhost:5432/pancakeshop";
    private final String user = "your_username";
    private final String password = "your_password";

    public VenueRepository() {
        try {
            Class.forName("org.postgresql.Driver"); // Ensure JDBC driver is loaded
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found!", e);
        }
    }

    // Add a new building
    public void addBuilding(String name) {
        String sql = "INSERT INTO Building (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all buildings
    public List<Building> getAllBuildings() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT * FROM Building";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                buildings.add(new Building(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    // Add a room to a building
    public void addRoom(int buildingId, String roomNumber) {
        String sql = "INSERT INTO Room (building_id, room_number) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildingId);
            stmt.setString(2, roomNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get rooms by building ID
    public List<Room> getRoomsByBuilding(int buildingId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE building_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, buildingId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(rs.getInt("id"), rs.getInt("building_id"), rs.getString("room_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
}

