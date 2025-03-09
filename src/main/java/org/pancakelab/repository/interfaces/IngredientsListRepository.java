package org.pancakelab.repository.interfaces;

import org.pancakelab.model.ingredients.IngredientsList;

import java.util.List;
import java.util.UUID;

public interface IngredientsListRepository {
    IngredientsList findById(UUID id);
    List<IngredientsList> findAll();
    void save(IngredientsList list);
    void delete(UUID id);
}

