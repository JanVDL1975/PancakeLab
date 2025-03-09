package org.pancakelab.repository.interfaces;

import org.pancakelab.model.ingredients.Ingredient;

import java.util.List;
import java.util.UUID;

public interface IngredientsListItemRepository {
    void addIngredient(UUID listId, UUID ingredientId, double quantity, String unit);
    void removeIngredient(UUID listId, UUID ingredientId);
    List<Ingredient> findIngredientsByList(UUID listId);
}

