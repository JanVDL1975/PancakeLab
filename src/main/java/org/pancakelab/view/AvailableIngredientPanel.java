package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientModel;
import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AvailableIngredientPanel extends JPanel {
    private IngredientModel ingredientModel;
    private JList<Ingredient> ingredientJList;

    public AvailableIngredientPanel() {
        setLayout(new BorderLayout());

        // Initialize list model and JList
        ingredientModel = new IngredientModel();
        ingredientJList = new JList<>(ingredientModel);
        ingredientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane for the ingredient list
        JScrollPane scrollPane = new JScrollPane(ingredientJList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // Add components to panel
        add(scrollPane, BorderLayout.CENTER);

        // Add a listener to auto-refresh when the model changes
        ingredientModel.addListDataListener(new ListDataListener() {
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
        ingredientJList.setModel(ingredientModel);
        ingredientJList.repaint();
    }

    public void setIngredientListModel(IngredientModel newModel) {
        if (this.ingredientModel != null) {
            // Remove old listener to avoid memory leaks
            for (ListDataListener listener : this.ingredientModel.getListDataListeners()) {
                this.ingredientModel.removeListDataListener(listener);
            }
        }

        this.ingredientModel = newModel;
        ingredientJList.setModel(ingredientModel);

        // Re-add listener to the new model
        ingredientModel.addListDataListener(new ListDataListener() {
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

    public IngredientModel getIngredientListModel() {
        return ingredientModel;
    }
}
