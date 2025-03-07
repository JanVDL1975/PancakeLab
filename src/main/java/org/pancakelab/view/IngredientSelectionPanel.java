package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.List;

public class IngredientSelectionPanel extends DualListBoxPanel<Ingredient> {
    private final IngredientListSelectionPanel<IngredientsList> ingredientListPanel;
    JList<Ingredient> availableList;

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
        System.out.println("Setting available list: " + newAvailableList);

        // Ensure you're working with the JList for Ingredient
        JList<Ingredient> ingredientList = getAvailableList();
        DefaultListModel<Ingredient> availableModel = (DefaultListModel<Ingredient>) ingredientList.getModel();

        // Clear the existing items
        availableModel.clear();

        // Add new ingredients to the model
        for (Ingredient ingredient : newAvailableList) {
            availableModel.addElement(ingredient);
        }

        revalidate();  // Revalidate the UI after changing the model
        repaint();     // Ensure the UI is painted with the updated list
    }


    public JList<Ingredient> getAvailableList() {
        if (availableList == null) {
            availableList = new JList<>(new DefaultListModel<Ingredient>());
        }
        return availableList; // Return JList<Ingredient>
    }
}







