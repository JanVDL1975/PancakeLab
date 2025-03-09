package org.pancakelab.repository.interfaces;

import org.pancakelab.model.orders.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    Order findById(UUID id);
    List<Order> findAll();
    void save(Order order);
    void delete(UUID id);
}

