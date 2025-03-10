package org.pancakelab.service;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.orders.Order;
import org.pancakelab.model.pancakes.*;
import org.pancakelab.model.recipes.Recipe;
import org.pancakelab.repository.impl.*;
import org.pancakelab.repository.interfaces.PancakeRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
    private final List<Order>         orders          = new ArrayList<>();
    private final Set<UUID>           completedOrders = new HashSet<>();
    private final Set<UUID>           preparedOrders  = new HashSet<>();
    private final List<PancakeRecipe> pancakes        = new ArrayList<>();
    private PancakeMenu         pancakeMenu         = new PancakeMenu();
    private final OrderRepositoryImpl orderRepo = new OrderRepositoryImpl();
    private final IngredientRepositoryImpl ingredientRepo = new IngredientRepositoryImpl();
    private final PancakeRepositoryImpl pancakeRepo = new PancakeRepositoryImpl();
    private final Connection connection = DatabaseService.getConnection();
    private final RecipeRepositoryImpl recipeRepo = new RecipeRepositoryImpl(connection);
    private final VenueRepositoryImpl venueRepo = new VenueRepositoryImpl();
    List<Ingredient> ingredientList;
    List<Recipe> recipeList;
    List<Pancake> pancakeList;
    public PancakeService() throws SQLException {
    }

    public Order createOrder(String building, int room) {
        Order order = new Order(building, room);
        orders.add(order);
        orderRepo.saveOrder(building, room);
        return order;
    }

    public void addDarkChocolatePancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolatePancake(),
                       orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
        }
    }

    public void addDarkChocolateWhippedCreamPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolateWhippedCreamPancake(),
                       orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
        }
    }

    public void addDarkChocolateWhippedCreamHazelnutsPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new DarkChocolateWhippedCreamHazelnutsPancake(),
                       orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
        }
    }

    public void addMilkChocolatePancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new MilkChocolatePancake(),
                       orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
        }
    }

    public void addMilkChocolateHazelnutsPancake(UUID orderId, int count) {
        for (int i = 0; i < count; ++i) {
            addPancake(new MilkChocolateHazelnutsPancake(),
                       orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get());
        }
    }

    public List<String> viewOrder(UUID orderId) {
        return pancakes.stream()
                       .filter(pancake -> pancake.getOrderId().equals(orderId))
                       .map(PancakeRecipe::description).toList();
    }

    private void addPancake(PancakeRecipe pancake, Order order) {
        pancake.setOrderId(order.getId());
        pancakes.add(pancake);

        OrderLog.logAddPancake(order, pancake.description(), pancakes);
    }

    public void removePancakes(String description, UUID orderId, int count) {
        final AtomicInteger removedCount = new AtomicInteger(0);
        pancakes.removeIf(pancake -> {
            return pancake.getOrderId().equals(orderId) &&
                   pancake.description().equals(description) &&
                   removedCount.getAndIncrement() < count;
        });

        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
        OrderLog.logRemovePancakes(order, description, removedCount.get(), pancakes);
    }

    public void cancelOrder(UUID orderId) {
        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
        OrderLog.logCancelOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orders.removeIf(o -> o.getId().equals(orderId));
        completedOrders.removeIf(u -> u.equals(orderId));
        preparedOrders.removeIf(u -> u.equals(orderId));

        OrderLog.logCancelOrder(order,pancakes);
    }

    public void completeOrder(UUID orderId) {
        completedOrders.add(orderId);
    }

    public Set<UUID> listCompletedOrders() {
        return completedOrders;
    }

    public void prepareOrder(UUID orderId) {
        preparedOrders.add(orderId);
        completedOrders.removeIf(u -> u.equals(orderId));
    }

    public Set<UUID> listPreparedOrders() {
        return preparedOrders;
    }

    public Object[] deliverOrder(UUID orderId) {
        if (!preparedOrders.contains(orderId)) return null;

        Order order = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().get();
        List<String> pancakesToDeliver = viewOrder(orderId);
        OrderLog.logDeliverOrder(order, this.pancakes);

        pancakes.removeIf(pancake -> pancake.getOrderId().equals(orderId));
        orders.removeIf(o -> o.getId().equals(orderId));
        preparedOrders.removeIf(u -> u.equals(orderId));

        return new Object[] {order, pancakesToDeliver};
    }

    public void addPancakeToOrder(UUID id, String pancakeName, int count) {
    }

    public PancakeMenu getPancakeMenu() {
        return pancakeMenu;
    }

    public void setPancakeMenu(PancakeMenu pancakeMenu) {
        this.pancakeMenu = pancakeMenu;
    }

    public void addIngredient(String name, double quantity, String unit) {
        ingredientRepo.saveIngredient(name, quantity, unit);
    }

    public void addRecipe(Recipe recipe) {
        recipeRepo.addRecipe(recipe);
    }

    public void addPancake(Pancake pancake) throws SQLException {
        pancakeRepo.addPancake(pancake);
    }

    public void addIngredientsList(String name, IngredientsList ingredient) {

    }

    public boolean initialiseIngredients() {
        boolean result = false;

        ingredientList = ingredientRepo.findAll();


        return result;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }


    public boolean initialiseRecipes() throws SQLException {
        boolean result = false;

        recipeList = recipeRepo.getAllRecipes();

        return result;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public boolean initialisePancakes() throws SQLException {
        boolean result = false;

        pancakeList = pancakeRepo.getAllPancakes();

        return result;
    }

    public List<Pancake> getPancakeList() {
        return pancakeList;
    }

    public boolean initialiseOrders() {

        boolean result = false;

        return result;
    }
}
