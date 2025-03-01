package org.pancakelab.model;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.repository.IngredientRepository;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class IngredientListModel extends DefaultListModel<Ingredient> {
    private IngredientRepository ingredientRepository;

    public IngredientListModel() {
        ingredientRepository = new IngredientRepository();

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

