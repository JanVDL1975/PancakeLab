package org.pancakelab.model.recipes;

import org.pancakelab.repository.impl.RecipeRepositoryImpl;
import org.pancakelab.service.DatabaseService;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.sql.Connection;
import java.util.List;

public class RecipeIngredientsListModel extends DefaultListModel<Recipe> {
    private RecipeRepositoryImpl recipeRepositoryImpl;
    private List<Recipe> recipeIngredientsList;

    public RecipeIngredientsListModel() {
        try {
            // Get the connection from your DatabaseService or wherever you get your connections
            Connection connection = DatabaseService.getConnection();

            recipeRepositoryImpl = new RecipeRepositoryImpl(connection);
            recipeIngredientsList = recipeRepositoryImpl.getAllRecipes(); // Load existing recipes

            // Populate the model with recipes from the database
            for (Recipe recipe : recipeIngredientsList) {
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
        recipeIngredientsList.add(recipe);   // Maintain internal list
    }

    public JList<Recipe> getList() {
        DefaultListModel<Recipe> listModel = new DefaultListModel<>();
        for (Recipe recipe : recipeIngredientsList) {
            listModel.addElement(recipe);
        }
        return new JList<>(listModel);
    }

    public List<Recipe> getRecipeIngredientsList() {
        return recipeIngredientsList;
    }
}

