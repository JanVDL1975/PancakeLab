package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientSelectionPanel extends DualListBoxPanel<Ingredient> {
    private final IngredientListSelectionPanel<IngredientsList> ingredientListPanel;

    public IngredientSelectionPanel(List<Ingredient> availableIngredients, IngredientListSelectionPanel<IngredientsList> ingredientListPanel) {
        super(availableIngredients, "Available Ingredients", "Selected Ingredients", false);
        this.ingredientListPanel = ingredientListPanel;
    }

    @Override
    protected void moveItem(JList<Ingredient> sourceList, DefaultListModel<Ingredient> sourceModel, DefaultListModel<Ingredient> targetModel, boolean movingToSelected) {
        Ingredient item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);
            sourceList.clearSelection();

            if (movingToSelected) {
                // Notify IngredientListSelectionPanel if needed
                List<IngredientsList> updatedLists = ingredientListPanel.getAvailableModelValues();
                updatedLists.add(new IngredientsList("New item")); // Assuming IngredientsList can hold an Ingredient
                ingredientListPanel.refreshAvailableIngredientsLists(updatedLists);
            }
        }
    }

    public void setAvailableList(List<Ingredient> newAvailableList) {
        DefaultListModel<Ingredient> availableModel = (DefaultListModel<Ingredient>) getAvailableList().getModel();
        availableModel.clear();
        newAvailableList.forEach(availableModel::addElement);
    }
}







