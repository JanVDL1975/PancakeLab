package org.pancakelab.model.ingredients;

import org.pancakelab.repository.IngredientRepository;

import javax.swing.*;
import javax.swing.event.ListDataEvent;

import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

import static com.sun.java.accessibility.util.SwingEventMonitor.addListDataListener;

public class IngredientsList {
    public String ingredientsListName;
    private IngredientRepository ingredientRepository;
    private List<Ingredient> ingredientList;

    public IngredientsList() {
        ingredientRepository = new IngredientRepository();
        ingredientList = ingredientRepository.getAllIngredients(); // Initialize the list

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
                ingredientRepository.saveIngredient(ingredient);  // Persist to DB
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

    public void addListDataListener(ListDataListener listDataListener) {
    }

    public ListDataListener[] getListDataListeners() {
        return new ListDataListener[0];
    }

    public void removeListDataListener(ListDataListener listener) {
    }
}


