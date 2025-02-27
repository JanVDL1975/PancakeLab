package org.pancakelab;

import org.pancakelab.model.Order;
import org.pancakelab.service.PancakeService;

import java.util.List;
import java.util.Set;

public class PancakeOrderWorkflow {
    private PancakeService pancakeService;
    public Order currentOrder;
    private boolean orderCreated;
    private boolean pancakesAdded;
    private boolean menuRequested;

    public PancakeOrderWorkflow(PancakeService pancakeService) {
        this.pancakeService = pancakeService;
        this.orderCreated = false;
        this.pancakesAdded = false;
        this.menuRequested = false;
    }

    public boolean isMenuRequested() {
        return menuRequested;
    }

    public void setMenuRequested(boolean menuRequested) {this.menuRequested = menuRequested;}

    public void requestMenu(boolean isMenuRequested) {
        setMenuRequested(isMenuRequested);
    }

    public Order createOrder(int building, int room) {
        if (orderCreated) {
            throw new IllegalStateException("Order already created. Proceed to adding pancakes.");
        }
        currentOrder = pancakeService.createOrder(building, room);
        orderCreated = true;
        return currentOrder;
    }

    public void addPancakeToOrder(String pancakeName, int count) {
        if (!orderCreated) {
            throw new IllegalStateException("You must create an order first.");
        }
        if (pancakesAdded) {
            throw new IllegalStateException("Pancakes have already been added to this order.");
        }

        pancakeService.addPancakeToOrder(currentOrder.getId(), pancakeName, count);
        pancakesAdded = true;
    }

    public List<String> viewOrder() {
        if (!pancakesAdded) {
            throw new IllegalStateException("No pancakes have been added to the order.");
        }
        return pancakeService.viewOrder(currentOrder.getId());
    }

    public Set<String> listAvailablePancakes() {
        return pancakeService.getPancakeMenu().getAvailablePancakes();
    }
}
