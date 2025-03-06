package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AvailableIngredientPanel extends JPanel {
    private IngredientsList ingredientsList;
    private final JList<Ingredient> ingredientJList;

    public AvailableIngredientPanel() {
        setLayout(new BorderLayout());

        // Initialize list model and JList
        ingredientsList = new IngredientsList("Custom List");
        ingredientJList = new JList<>((ListModel) ingredientsList);
        ingredientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane for the ingredient list
        JScrollPane scrollPane = new JScrollPane(ingredientJList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // Add components to panel
        add(scrollPane, BorderLayout.CENTER);

        // Add a listener to auto-refresh when the model changes
        ingredientsList.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                refreshList();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                refreshList();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                refreshList();
            }
        });
    }

    // Method to refresh the ingredient list when new items are added
    public void refreshList() {
        ingredientJList.setModel((ListModel<Ingredient>) ingredientsList);
        ingredientJList.repaint();
    }

    public void setIngredientListModel(IngredientsList newModel) {
        if (this.ingredientsList != null) {
            // Remove old listener to avoid memory leaks
            for (ListDataListener listener : this.ingredientsList.getListDataListeners()) {
                this.ingredientsList.removeListDataListener(listener);
            }
        }

        this.ingredientsList = newModel;
        ingredientJList.setModel((ListModel<Ingredient>) ingredientsList);

        // Re-add listener to the new model
        ingredientsList.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                refreshList();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                refreshList();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                refreshList();
            }
        });

        refreshList();
    }

    public IngredientsList getIngredientListModel() {
        return ingredientsList;
    }
}
