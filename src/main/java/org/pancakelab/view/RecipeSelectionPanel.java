package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.recipes.Recipe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeSelectionPanel<R> extends DualListBoxPanel<Recipe> {

    public RecipeSelectionPanel(List<Recipe> availableRecipes) {
        super(availableRecipes, "Available Recipes", "Selected Recipes", false);
    }

    protected void moveItem(JList<Recipe> sourceList, DefaultListModel<Recipe> sourceModel,
                            DefaultListModel<Recipe> targetModel, boolean movingToSelected) {
        Recipe recipe = sourceList.getSelectedValue();
        if (recipe != null) {
            sourceModel.removeElement(recipe);
            targetModel.addElement(recipe);

            if (movingToSelected) {
                getSelectedQuantities().putIfAbsent(recipe, 1);
            } else {
                getSelectedQuantities().remove(recipe);
            }

            sourceList.clearSelection();

            if (movingToSelected) {
                // Update the selected recipe list if necessary
                List<Recipe> updatedRecipes = getSelectedModelValues();
                // Implement any additional behavior if needed
            }
        }
    }

    // Get available recipes from the available list model
    public List<Recipe> getAvailableModelValues() {
        List<Recipe> availableRecipes = new ArrayList<>();
        for (int i = 0; i < getAvailableModel().getSize(); i++) {
            availableRecipes.add(getAvailableModel().getElementAt(i));
        }
        return availableRecipes;
    }

    // Get selected recipes from the selected list model
    /*public IngredientsList getSelectedModelValues() {
        List<Recipe> selectedRecipes = new ArrayList<>();
        for (int i = 0; i < getSelectedModel().getSize(); i++) {
            selectedRecipes.add(getSelectedModel().getElementAt(i));
        }
        return selectedRecipes;
    }*/

    @Override
    // Get selected recipes from the selected list model
    public List<Recipe> getSelectedModelValues() {
        List<Recipe> selectedRecipes = new ArrayList<>();
        for (int i = 0; i < getSelectedModel().getSize(); i++) {
            selectedRecipes.add(getSelectedModel().getElementAt(i));
        }
        return selectedRecipes;
    }

    // Get the model for the available recipes
    @Override
    protected DefaultListModel<Recipe> getAvailableModel() {
        return super.getAvailableModel();
    }

    // Get the model for the selected recipes
    @Override
    protected DefaultListModel<Recipe> getSelectedModel() {
        return super.getSelectedModel();
    }


    // Refresh available recipes with a new list of recipes
    public void refreshAvailableRecipes(List<Recipe> newRecipes) {
        resetModels(newRecipes);
    }

    @Override
    public Map<Recipe, Integer> getSelectedQuantities() {
        return super.getSelectedQuantities();
    }
}


