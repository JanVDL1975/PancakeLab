package org.pancakelab.repository.interfaces;

import org.pancakelab.model.venues.Building;
import org.pancakelab.model.venues.Room;

import java.util.List;

public interface VenueRepository {
    Building findBuildingById(int id);
    List<Building> findAllBuildings();
    void saveBuilding(Building building);
    void deleteBuilding(int id);

    Room findRoomById(int id);
    List<Room> findAllRooms();
    void saveRoom(Room room);
    void deleteRoom(int id);
}

