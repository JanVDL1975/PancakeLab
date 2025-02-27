package org.pancakelab.model.pancakes.impl;

import org.pancakelab.model.Ingredient;
import org.pancakelab.model.Ingredients;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PancakeRecipeImpl implements PancakeRecipe {
    private UUID orderId;
    private Ingredients ingredients;

    public PancakeRecipeImpl(UUID orderId, Ingredients ingredients) {
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
        // Convert the Ingredients Set to a List of Strings
        return ingredients.getIngredients().stream()
                .map(Ingredient::toString) // Convert each Ingredient to a String
                .collect(Collectors.toList());
    }
}
