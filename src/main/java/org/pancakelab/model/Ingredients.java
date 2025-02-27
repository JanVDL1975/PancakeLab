package org.pancakelab.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Ingredients {
    private Set<Ingredient> ingredientSet;

    public Ingredients() {
        this.ingredientSet = new HashSet<>(); // Ensures no duplicates
    }

    public Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(ingredientSet); // Read-only view
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredientSet = new HashSet<>(ingredients); // Ensures uniqueness
    }

    public boolean addIngredient(Ingredient ingredient) {
        return ingredientSet.add(ingredient); // Returns false if already exists
    }

    public boolean removeIngredient(Ingredient ingredient) {
        return ingredientSet.remove(ingredient);
    }

    @Override
    public String toString() {
        return "Ingredients: " + ingredientSet;
    }
}
