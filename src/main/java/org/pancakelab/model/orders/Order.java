package org.pancakelab.model.orders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.pancakelab.model.pancakes.Pancake;

public class Order {
    private final UUID id;
    private final String building;
    private final int room;
    private List<Pancake> pancakes;

    public Order(String building, int room) {
        this.id = UUID.randomUUID();
        this.building = building;
        this.room = room;
    }

    public Order(UUID id, String building, int room, List<Pancake> pancakesByOrderId) {
        this.id = id;
        this.building = building;
        this.room = room;
        this.pancakes = pancakesByOrderId;
    }

    public UUID getId() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Pancake[] getOrderPancakes() {
        return pancakes.toArray(new Pancake[pancakes.size()]);
    }
}
