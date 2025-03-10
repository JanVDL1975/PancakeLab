package org.pancakelab.model.recipes;

import org.pancakelab.repository.impl.RecipeRepositoryImpl;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;

public class RecipeListModel extends DefaultListModel<Recipe> {
    private RecipeRepositoryImpl recipeRepositoryImpl;
    private List<Recipe> recipesList;

    public RecipeListModel() {
        try {
            recipeRepositoryImpl = new RecipeRepositoryImpl();
            recipesList = recipeRepositoryImpl.getAllRecipes(); // Load existing recipes

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
                    recipeRepositoryImpl.save(recipe); // Save new recipe to DB
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

