package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class IngredientSelectionPanel<T> extends DualListBoxPanel<T> {
    private final IngredientListSelectionPanel<T> ingredientListPanel;

    public IngredientSelectionPanel(List<Ingredient> availableIngredients, IngredientListSelectionPanel<IngredientsList> ingredientListPanel) {
        super((List<T>) availableIngredients, "Available Ingredients", "Selected Ingredients", false);
        this.ingredientListPanel = (IngredientListSelectionPanel<T>) ingredientListPanel;
    }

    @Override
    protected void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel,
                            DefaultListModel<T> targetModel, boolean movingToSelected) {
        T item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);

            if (movingToSelected) {
                getSelectedQuantities().putIfAbsent(item, 1);
            } else {
                getSelectedQuantities().remove(item);
            }

            sourceList.clearSelection();

            if (movingToSelected) {
                List<T> updatedLists = ingredientListPanel.getAvailableModelValues();
                updatedLists.add(item); // Add the item to the list
                ingredientListPanel.refreshAvailableIngredientsLists(updatedLists);
            }
        }
    }

    public void setAvailableList(List<Ingredient> newAvailableList) {
        System.out.println("Setting available list: " + newAvailableList);
        DefaultListModel<T> availableModel = getAvailableModel();
        availableModel.clear(); // Clear existing items
        for (Ingredient ingredient : newAvailableList) {
            availableModel.addElement((T) ingredient); // Add new items
        }

        //super.resetModels((List<T>) Arrays.stream(availableModel.toArray()).toList());
    }
}






