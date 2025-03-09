package org.pancakelab.repository.interfaces;

import org.pancakelab.model.ingredients.Ingredient;

import java.util.List;
import java.util.UUID;

public interface IngredientRepository {
    Ingredient findById(UUID id);
    List<Ingredient> findAll();
    void save(Ingredient ingredient);
    void delete(UUID id);
}

