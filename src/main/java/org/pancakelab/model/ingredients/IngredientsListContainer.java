package org.pancakelab.model.ingredients;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.*;

public class IngredientsListContainer {
    private String ingredientsListName;
    private Set<IngredientsList> ingredientSet; // Stores multiple IngredientsList objects

    public IngredientsListContainer(String ingredientsListName) {
        this.ingredientsListName = ingredientsListName;
        this.ingredientSet = new HashSet<>(); // Ensures no duplicates
    }

    public List<IngredientsList> getAllIngredientsLists() {
        return new ArrayList<>(ingredientSet); // Returns a read-only list of ingredient lists
    }

    public boolean addIngredientsList(IngredientsList ingredientList) {
        return ingredientSet.add(ingredientList); // Adds a new IngredientsList if not already present
    }

    public boolean removeIngredientsList(IngredientsList ingredientList) {
        return ingredientSet.remove(ingredientList); // Removes an IngredientsList if present
    }

    // Method to get a specific IngredientsList by name
    public IngredientsList getIngredientsListByName(String name) {
        for (IngredientsList list : ingredientSet) {
            if (list.ingredientsListName.equals(name)) {
                return list;
            }
        }
        return null; // Returns null if no match is found
    }

    // Method to remove an IngredientsList by name
    public boolean removeIngredientsListByName(String name) {
        IngredientsList toRemove = getIngredientsListByName(name);
        if (toRemove != null) {
            return ingredientSet.remove(toRemove);
        }
        return false; // Returns false if no matching list was found
    }

    @Override
    public String toString() {
        return "Ingredients lists: " + ingredientSet;
    }
}



