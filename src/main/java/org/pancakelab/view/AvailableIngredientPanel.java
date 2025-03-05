package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientListModel;
import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class AvailableIngredientPanel extends JPanel {
    private IngredientListModel ingredientListModel;
    private JList<Ingredient> ingredientJList;

    public AvailableIngredientPanel() {
        setLayout(new BorderLayout());

        // Initialize list model and JList
        ingredientListModel = new IngredientListModel();
        ingredientJList = new JList<>(ingredientListModel);
        ingredientJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Scroll pane for the ingredient list
        JScrollPane scrollPane = new JScrollPane(ingredientJList);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // Add components to panel
        add(scrollPane, BorderLayout.CENTER);

        // Add a listener to auto-refresh when the model changes
        ingredientListModel.addListDataListener(new ListDataListener() {
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
        ingredientJList.setModel(ingredientListModel);
        ingredientJList.repaint();
    }

    public void setIngredientListModel(IngredientListModel newModel) {
        if (this.ingredientListModel != null) {
            // Remove old listener to avoid memory leaks
            for (ListDataListener listener : this.ingredientListModel.getListDataListeners()) {
                this.ingredientListModel.removeListDataListener(listener);
            }
        }

        this.ingredientListModel = newModel;
        ingredientJList.setModel(ingredientListModel);

        // Re-add listener to the new model
        ingredientListModel.addListDataListener(new ListDataListener() {
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

    public IngredientListModel getIngredientListModel() {
        return ingredientListModel;
    }
}
