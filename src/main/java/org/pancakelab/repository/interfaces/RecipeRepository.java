package org.pancakelab.repository.interfaces;

import org.pancakelab.model.recipes.Recipe;

import java.util.List;
import java.util.UUID;

public interface RecipeRepository {
    Recipe findById(UUID id);
    List<Recipe> findAll();
    void save(Recipe recipe);
    void delete(int id);
}

