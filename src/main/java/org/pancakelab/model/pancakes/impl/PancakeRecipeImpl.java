package org.pancakelab.model.pancakes.impl;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PancakeRecipeImpl implements PancakeRecipe {
    private UUID orderId;
    private final List<Ingredient> ingredients;

    public PancakeRecipeImpl(UUID orderId, List<Ingredient> ingredients) {
        this.orderId = orderId;
        this.ingredients = ingredients;
    }

    @Override
    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<String> ingredients() {
        // Convert the List<Ingredient> to a List<String>
        return ingredients.stream()
                .map(Ingredient::getName) // Assuming Ingredient has getName()
                .collect(Collectors.toList());
    }

}
