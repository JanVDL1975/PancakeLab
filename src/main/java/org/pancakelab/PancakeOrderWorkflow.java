package org.pancakelab;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.orders.Order;
import org.pancakelab.service.PancakeService;

import java.util.List;
import java.util.Set;

public class PancakeOrderWorkflow {
    private final PancakeService pancakeService;
    public Order currentOrder;
    private boolean orderCreated;
    private boolean pancakesAdded;
    private boolean menuRequested;
    private boolean isInitialised;
    private boolean isIngredientsInitialised;
    private boolean isRecipesInitialised;
    private boolean isPancakesInitialised;
    private boolean isOrdersInitialised;

    public PancakeOrderWorkflow(PancakeService pancakeService) {
        this.pancakeService = pancakeService;
        this.orderCreated = false;
        this.pancakesAdded = false;
        this.menuRequested = false;

        this.isIngredientsInitialised = false;
        this.isRecipesInitialised = false;
        this.isPancakesInitialised = false;
        this.isOrdersInitialised = false;

    }

    public boolean isMenuRequested() {
        return menuRequested;
    }

    public void setMenuRequested(boolean menuRequested) {this.menuRequested = menuRequested;}

    public void requestMenu(boolean isMenuRequested) {
        setMenuRequested(isMenuRequested);
    }

    public Order createOrder(String building, int room) {
        if (orderCreated) {
            throw new IllegalStateException("Order already created. Proceed to adding pancakes.");
        }
        currentOrder = pancakeService.createOrder(building, room);
        orderCreated = true;
        return currentOrder;
    }

    public String addPancakeToOrder(String pancakeName, int count) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        boolean success = true;

        if (!orderCreated) {
            sb.append("You must create an order first.");
            success = false;
            //throw new IllegalStateException("You must create an order first."); TODO: Remove
        }
        if (pancakesAdded) {
            sb.append("You must create an order first.");
            success = false;
            //throw new IllegalStateException("Pancakes have already been added to this order."); TODO: Remove
        }

        //TODO: Need to do something in this method???
        if (success) {
            pancakeService.addPancakeToOrder(currentOrder.getId(), pancakeName, count);
            pancakesAdded = true;
        }
        else {
            sb.append("The pancake was not added.");
        }

        result = sb.toString();

        return result;
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

    public void removePancakeFromOrder(String pancakeDescription, int count) {
        if (currentOrder == null) {
            throw new IllegalStateException("No active order. Create an order first.");
        }
        pancakeService.removePancakes(pancakeDescription, currentOrder.getId(), count);
    }

    public void buildNewPancake(String pancakeName) {
    }

    public void addRecipe(String recipeName, List<IngredientsList> ingredients) {
    }

    public void addItem(String itemName, String itemQuantity, String  itemUnits) {
        double quantity = 0.0d;

        try {
             quantity = Double.parseDouble(itemQuantity);
        } catch (NumberFormatException e) {

        }

        pancakeService.addIngredient(itemName, quantity , itemUnits);

    }

    public boolean initializePancakeOrderSystem() {
        try {
            this.isIngredientsInitialised = pancakeService.initialiseIngredients();
            this.isRecipesInitialised = pancakeService.initialiseRecipes();
            this.isPancakesInitialised = pancakeService.initialisePancakes();
            this.isOrdersInitialised = pancakeService.initialiseOrders();

            isInitialised = isIngredientsInitialised &&
                    isRecipesInitialised &&
                    isPancakesInitialised &&
                    isOrdersInitialised;
        } catch (Exception e) {
            isInitialised = false;
        }

        return isInitialised;
    }
}
