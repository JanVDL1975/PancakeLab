package org.pancakelab.model.ingredients;

import org.pancakelab.repository.impl.IngredientRepositoryImpl;

import javax.swing.*;
import javax.swing.event.ListDataEvent;

import javax.swing.event.ListDataListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IngredientsList {
    private UUID id;

    public IngredientsList(int recipeId, String recipeIngredients, List<Ingredient> ingredients, String ingredientsListName, IngredientRepositoryImpl ingredientRepositoryImpl, List<Ingredient> ingredientList) {

        this.ingredientsListName = ingredientsListName;
        this.ingredientRepositoryImpl = ingredientRepositoryImpl;
        this.ingredientList = ingredientList;
    }

    public IngredientsList(UUID id, String ingredientsListName, IngredientRepositoryImpl ingredientRepositoryImpl, List<Ingredient> ingredientsForList) {
        this.id = id;
        this.ingredientsListName = ingredientsListName;
        this.ingredientRepositoryImpl = ingredientRepositoryImpl;
        this.ingredientList = ingredientsForList;
    }

    public String getIngredientsListName() {
        return ingredientsListName;
    }

    private final String ingredientsListName;
    private final IngredientRepositoryImpl ingredientRepositoryImpl;
    private final List<Ingredient> ingredientList;

    public IngredientsList(String customList) throws SQLException {
        ingredientsListName = customList;
        ingredientRepositoryImpl = new IngredientRepositoryImpl();
        ingredientList = ingredientRepositoryImpl.findAll(); // Initialize the list

        // Make a copy before modifying the list
        List<Ingredient> copyList = new ArrayList<>(ingredientList);

        // Load existing ingredients from database
        for (Ingredient ingredient : copyList) {
            addElement(ingredient);  // Use super to trigger ListDataEvent
        }

        // Add a listener to detect changes in the list
        addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                int index = e.getIndex0();
                Ingredient ingredient = getElementAt(index);
                ingredientRepositoryImpl.saveIngredient(ingredient);  // Persist to DB
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {}
        });
    }


    public Ingredient getElementAt(int index) {
        if (index >= 0 && index < ingredientList.size()) {
            return ingredientList.get(index); // ✅ Accesses the ingredient at index
        } else {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }


    public void addElement(Ingredient ingredient) {
        ingredientList.add(ingredient); // Maintain the internal list for reference
    }

    public List<Ingredient> getList() {
        DefaultListModel<Ingredient> listModel = new DefaultListModel<>();
        for (Ingredient ingredient : ingredientList) { // assuming ingredientList is a List<Ingredient>
            listModel.addElement(ingredient);
        }
        return new JList<>(listModel).getSelectedValuesList();
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void addListDataListener(ListDataListener listDataListener) {
    }

    public ListDataListener[] getListDataListeners() {
        return new ListDataListener[0];
    }

    public void removeListDataListener(ListDataListener listener) {
    }

    public void addIngredient(Ingredient ingredient) {
        System.out.println("Adding ingredient: " + ingredient);
        ingredientList.add(ingredient);
        System.out.println("Calling addIngredient: Updated Ingredient Model: " + ingredientList);
    }

    public List<Ingredient> getIngredients() {
        return ingredientList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IngredientsList Name: ").append(ingredientsListName).append("\n");

        return builder.toString();
    }


}


