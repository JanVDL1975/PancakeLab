package org.pancakelab.model.recipes;

import org.pancakelab.model.ingredients.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private List<Ingredient> ingredients;

    public Recipe(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void addIngredient(Ingredient ingredient) { this.ingredients.add(ingredient); }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}

