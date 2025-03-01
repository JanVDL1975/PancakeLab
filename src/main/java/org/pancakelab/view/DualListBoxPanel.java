package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.repository.IngredientRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DualListBoxPanel extends JPanel {
    private DefaultListModel<Ingredient> availableModel;
    private DefaultListModel<Ingredient> selectedModel;
    private JList<Ingredient> availableList;
    private JList<Ingredient> selectedList;
    private IngredientRepository ingredientRepository;

    public DualListBoxPanel() {
        setLayout(new BorderLayout());
        ingredientRepository = new IngredientRepository();

        // Left List - Available Ingredients
        availableModel = new DefaultListModel<>();
        availableList = new JList<>(availableModel);
        loadAvailableIngredients(); // Load ingredients from the database
        JScrollPane availableScrollPane = new JScrollPane(availableList);

        // Right List - Selected Ingredients
        selectedModel = new DefaultListModel<>();
        selectedList = new JList<>(selectedModel);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton(">>"); // Move to Selected
        JButton removeButton = new JButton("<<"); // Move back to Available

        // Move from Available to Selected
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveIngredient(availableList, availableModel, selectedModel);
            }
        });

        // Move from Selected back to Available
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveIngredient(selectedList, selectedModel, availableModel);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        // Layout Setup
        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        listsPanel.add(createTitledPanel("Available Ingredients", availableScrollPane));
        listsPanel.add(buttonPanel);
        listsPanel.add(createTitledPanel("Selected Ingredients", selectedScrollPane));

        add(listsPanel, BorderLayout.CENTER);
    }

    // Fetch ingredients from the database and add them to the available list
    private void loadAvailableIngredients() {
        List<Ingredient> ingredients = ingredientRepository.getAllIngredients();
        for (Ingredient ingredient : ingredients) {
            availableModel.addElement(ingredient);
        }
    }

    // Move an item from one list to another
    private void moveIngredient(JList<Ingredient> sourceList, DefaultListModel<Ingredient> sourceModel, DefaultListModel<Ingredient> targetModel) {
        int selectedIndex = sourceList.getSelectedIndex();
        if (selectedIndex != -1) {
            Ingredient ingredient = sourceModel.remove(selectedIndex);
            targetModel.addElement(ingredient);
        }
    }

    // Utility method to create a titled panel
    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}

