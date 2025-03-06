package org.pancakelab.model.ingredients;

import org.pancakelab.repository.IngredientRepository;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;

public class IngredientsListModel extends DefaultListModel<Ingredient> {
    private String ingredientsListName;
    private final IngredientRepository ingredientRepository;
    private final List<Ingredient> ingredientList;

    public IngredientsListModel() {
        ingredientRepository = new IngredientRepository();
        ingredientList = ingredientRepository.getAllIngredients(); // Initialize the list

        // Load existing ingredients from database
        for (Ingredient ingredient : ingredientList) {
            super.addElement(ingredient);  // Use super to trigger ListDataEvent
        }

        // Add a listener to detect changes in the list
        this.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                int index = e.getIndex0();
                Ingredient ingredient = getElementAt(index);
                ingredientRepository.saveIngredient(ingredient);  // Persist to DB
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {}

            @Override
            public void contentsChanged(ListDataEvent e) {}
        });
    }

    @Override
    public void addElement(Ingredient ingredient) {
        super.addElement(ingredient); // Call the parent method to trigger ListDataEvent
        ingredientList.add(ingredient); // Maintain the internal list for reference
    }

    public JList<Ingredient> getList() {
        DefaultListModel<Ingredient> listModel = new DefaultListModel<>();
        for (Ingredient ingredient : ingredientList) { // assuming ingredientList is a List<Ingredient>
            listModel.addElement(ingredient);
        }
        return new JList<>(listModel);
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }
}



