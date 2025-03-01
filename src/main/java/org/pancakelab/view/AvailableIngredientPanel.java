package org.pancakelab.view;

import org.pancakelab.model.IngredientListModel;
import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;

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
        //add(new JLabel("Available Ingredients:"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to refresh the ingredient list when new items are added
    public void refreshList() {
        ingredientJList.updateUI();
    }

    public IngredientListModel getIngredientListModel() {
        return ingredientListModel;
    }
}
