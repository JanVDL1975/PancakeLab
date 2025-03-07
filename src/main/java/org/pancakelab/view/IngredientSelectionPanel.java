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
/*
    public void setAvailableList(List<Ingredient> ingredientList) {
        availableModel.clear(); // Clear existing data
        for (Ingredient ingredient : ingredientList) {
            availableModel.addElement(ingredient);
        }

        System.out.println("setAvailableList(): availableModel size = " + availableModel.getSize());

        availableList.setModel(availableModel); // Explicitly set the model again
        availableList.revalidate(); // Ensure UI refresh
        availableList.repaint();
    }*/



    public JList<Ingredient> getAvailableList() {
        if (availableList == null) {
            availableList = new JList<>(new DefaultListModel<Ingredient>());
        }
        return availableList; // Return JList<Ingredient>
    }
}







