package org.pancakelab;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.orders.Order;
import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.service.PancakeService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PancakeOrderWorkflow {
    private final PancakeService pancakeService;
    private Order currentOrder = new Order(UUID.randomUUID(), "", 0,  new ArrayList<>());
    private boolean orderCreated;
    private boolean pancakesAdded;
    private boolean menuRequested;
    private boolean isInitialised;
    private boolean isIngredientsInitialised;
    private boolean isRecipesInitialised;
    private boolean isPancakesInitialised;
    private boolean isOrdersInitialised;
    private List<Pancake> availablePancakes;

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

    public void resetCurrentOrder() {
        currentOrder = new Order(UUID.randomUUID(), "", 0,  new ArrayList<>());
    }

    public Order createOrder(String building, int room) {
        if(currentOrder != null) {
            UUID id = currentOrder.getId();
            Order existingOrder = pancakeService.getOrder(id);
            if (existingOrder != null) {
                orderCreated = true;
            }
            else {
                orderCreated = false;
            }
        } else {
            orderCreated = false;
        }

        if (orderCreated) {
            throw new IllegalStateException("Order already created. Proceed to adding pancakes.");
        }
        currentOrder = pancakeService.createOrder(building, room);
        orderCreated = true;
        return currentOrder;
    }

    public String addPancakeToOrder(Pancake pancake, int count) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        boolean success = true;

        if (!orderCreated) {
            sb.append("You must create an order first.");
            success = false;
        }

        if (success) {
            pancakeService.addPancakeToOrder(currentOrder, pancake, count);
            pancakesAdded = true;
            sb.append("Order id: " + currentOrder.getId() + "\n");
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


    public void addRecipe(UUID id, String recipeName, IngredientsList ingredients) {
        pancakeService.addRecipe(new Recipe(id, recipeName, "", ingredients));
    }

    public void addPancake(Pancake pancake) throws SQLException {
        availablePancakes = pancakeService.getPancakeList();
        pancakeService.addPancake(pancake);
        availablePancakes.add(pancake);
    }

    public void addIngredientList(String listName, IngredientsList ingredients) {
        boolean isListnamePopulated = listName.isEmpty();
        boolean doesIngredientsListExist = ingredients != null;


    }

    public void addItem(String itemName, String itemQuantity, String  itemUnits) {
        double quantity = 0.0d;

        try {
             quantity = Double.parseDouble(itemQuantity);
        } catch (NumberFormatException e) {

        }

        pancakeService.addIngredient(itemName, quantity , itemUnits);

    }

    public List<Pancake> getPancakeList() {
        return pancakeService.getPancakeList();
    }

    public List<Ingredient> retrieveAllItems() {
        return pancakeService.getIngredientList();
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
