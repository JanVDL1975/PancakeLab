package org.pancakelab.model.recipes;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID id;
    private String name;
    private String description;
    private final IngredientsList ingredientsList;

    public Recipe(UUID id, String name, String description, IngredientsList ingredientsList) {
        this.id = id;
        this.name = name;
        this.description = description;
        if (ingredientsList != null) {
            this.ingredientsList = ingredientsList;
        } else {
            // Handle the case where ingredientsList is null (e.g., set an empty list or handle the error)
            this.ingredientsList = new IngredientsList("Empty List"); // or use a default empty object
        }
    }


    public String getId() { return id.toString(); }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public IngredientsList getIngredients() { return ingredientsList; }
    public IngredientsList getIngredientsAsList() {return ingredientsList;}
    public void addIngredient(Ingredient ingredient) { this.ingredientsList.addIngredient(ingredient); }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    public IngredientsList getIngredientsList() {
        IngredientsList list = new IngredientsList(UUID.randomUUID(), name, (List<Ingredient>) ingredientsList);
        return list;
    }
}

