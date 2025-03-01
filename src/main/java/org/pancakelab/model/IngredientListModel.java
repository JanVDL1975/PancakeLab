package org.pancakelab.model;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.repository.IngredientRepository;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;

public class IngredientListModel extends DefaultListModel<Ingredient> {
    private IngredientRepository ingredientRepository;

    // Fetch existing ingredients from database and populate the list
    private void loadExistingIngredients() {
        List<Ingredient> existingIngredients = ingredientRepository.getAllIngredients();
        for (Ingredient ingredient : existingIngredients) {
            addElement(ingredient);
        }
    }

    public IngredientListModel() {
        ingredientRepository = new IngredientRepository();

        // Load existing ingredients from database
        loadExistingIngredients();

        // Add a listener to detect changes in the list
        this.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                // Trigger database persistence when an item is added
                int index = e.getIndex0();
                Ingredient ingredient = getElementAt(index);
                ingredientRepository.saveIngredient(ingredient);  // Persist to DB
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                // No action needed when items are removed
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                // No action needed when contents change
            }
        });
    }
}

