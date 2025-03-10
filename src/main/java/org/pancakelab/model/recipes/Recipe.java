package org.pancakelab.model.recipes;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private final IngredientsList ingredients;
    private final ArrayList<Ingredient> ingredientsList;

    public Recipe(int id, String name, String description, IngredientsList ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.ingredientsList = (ArrayList<Ingredient>) ingredients.getIngredients();;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public IngredientsList getIngredients() { return ingredients; }
    public ArrayList<Ingredient> getIngredientsAsList() {return ingredientsList;}
    public void addIngredient(Ingredient ingredient) { this.ingredients.addIngredient(ingredient); }

    @Override
    public String toString() {
        return name + ": " + description;
    }

    public IngredientsList getIngredientsList() {
        IngredientsList list = new IngredientsList(UUID.randomUUID(), name, ingredientsList);
        return list;
    }
}

