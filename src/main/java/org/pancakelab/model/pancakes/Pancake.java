package org.pancakelab.model.pancakes;

import java.util.UUID;

public class Pancake {
    private UUID id;
    private PancakeRecipe recipe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Pancake(UUID id, PancakeRecipe recipe) {
        this.id = id;
        this.recipe = recipe;
    }

    public Pancake(UUID id, String name, PancakeRecipe recipe) {
        this.id = id;
        this.name = name;
        this.recipe = recipe;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public PancakeRecipe getRecipe() { return recipe; }
    public void setRecipe(PancakeRecipe recipe) { this.recipe = recipe; }

    @Override
    public String toString() {
        return "Pancake with Recipe: " + recipe.description();
    }
}

