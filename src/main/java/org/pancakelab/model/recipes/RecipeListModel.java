package org.pancakelab.model.recipes;

import org.pancakelab.repository.RecipeRepository;
import org.pancakelab.service.DatabaseService;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;

public class RecipeListModel extends DefaultListModel<Recipe> {
    private RecipeRepository recipeRepository;
    private List<Recipe> recipesList;

    public RecipeListModel() {
        try {
            recipeRepository = new RecipeRepository();
            recipesList = recipeRepository.getAllRecipes(); // Load existing recipes

            // Populate the model with recipes from the database
            for (Recipe recipe : recipesList) {
                super.addElement(recipe);
            }

            // Listen for list changes to persist new elements
            this.addListDataListener(new ListDataListener() {
                @Override
                public void intervalAdded(ListDataEvent e) {
                    int index = e.getIndex0();
                    Recipe recipe = getElementAt(index);
                    recipeRepository.saveRecipe(recipe.getName()); // Save new recipe to DB
                }

                @Override
                public void intervalRemoved(ListDataEvent e) {}

                @Override
                public void contentsChanged(ListDataEvent e) {}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addElement(Recipe recipe) {
        super.addElement(recipe); // Update UI
        recipesList.add(recipe);   // Maintain internal list
    }

    public JList<Recipe> getList() {
        DefaultListModel<Recipe> listModel = new DefaultListModel<>();
        for (Recipe recipe : recipesList) {
            listModel.addElement(recipe);
        }
        return new JList<>(listModel);
    }

    public List<Recipe> getRecipesList() {
        return recipesList;
    }
}

