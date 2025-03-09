package org.pancakelab.repository.interfaces;

import org.pancakelab.model.recipes.Recipe;

import java.util.List;

public interface RecipeRepository {
    Recipe findById(int id);
    List<Recipe> findAll();
    void save(Recipe recipe);
    void delete(int id);
}

