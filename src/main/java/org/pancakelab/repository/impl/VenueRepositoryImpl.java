package org.pancakelab.repository.impl;

import org.pancakelab.model.venues.Building;
import org.pancakelab.model.venues.Room;
import org.pancakelab.repository.interfaces.VenueRepository;
import org.pancakelab.service.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueRepositoryImpl implements VenueRepository {

    // Add a new building using DatabaseService
    public void addBuilding(String name) {
        String sql = "INSERT INTO Building (name) VALUES (?) ON CONFLICT (name) DO NOTHING";

        try {
            // Use DatabaseService to execute the update
            DatabaseService.executeUpdate(sql, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all buildings using DatabaseService
    public List<Building> getAllBuildings() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT * FROM Building";

        try {
            // Use DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    buildings.add(new Building(rs.getInt("id"), rs.getString("name")));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    // Add a room to a building using DatabaseService
    public void addRoom(int buildingId, String roomNumber) {
        String sql = "INSERT INTO Room (building_id, room_number) VALUES (?, ?)";

        try {
            // Use DatabaseService to execute the update
            DatabaseService.executeUpdate(sql, buildingId, roomNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get rooms by building ID using DatabaseService
    public List<Room> getRoomsByBuilding(int buildingId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM Room WHERE building_id = ?";

        try {
            // Use DatabaseService to execute the query
            DatabaseService.executeQuery(sql, rs -> {
                while (rs.next()) {
                    rooms.add(new Room(rs.getInt("id"), rs.getInt("building_id"), rs.getString("room_number")));
                }
            }, buildingId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public Building findBuildingById(int id) {
        return null;
    }

    @Override
    public List<Building> findAllBuildings() {
        return List.of();
    }

    @Override
    public void saveBuilding(Building building) {

    }

    @Override
    public void deleteBuilding(int id) {

    }

    @Override
    public Room findRoomById(int id) {
        return null;
    }

    @Override
    public List<Room> findAllRooms() {
        return List.of();
    }

    @Override
    public void saveRoom(Room room) {

    }

    @Override
    public void deleteRoom(int id) {

    }
}


