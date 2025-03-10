package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;
import org.pancakelab.model.recipes.Recipe;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DualListAndTextAreaPanel<T> extends JPanel {
    private final DefaultListModel<T> availableModel;
    private final DefaultListModel<T> selectedModel;
    private JList<T> availableList;
    private final JList<T> selectedList;
    private final JTextArea availableTextArea;
    private final JTextArea selectedTextArea;
    private final Map<T, Integer> selectedQuantities;
    private final boolean showComboBox;

    public DualListAndTextAreaPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        this.showComboBox = showComboBox;
        this.selectedQuantities = new HashMap<>();
        setLayout(new BorderLayout());

        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();
        availableList = new JList<>(availableModel);
        availableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectedList = new JList<>(selectedModel);
        selectedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        availableTextArea = new JTextArea(5, 20);
        selectedTextArea = new JTextArea(5, 20);
        availableTextArea.setEditable(false);
        selectedTextArea.setEditable(false);

        for (T item : availableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1);
        }

        availableList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Display only the name of IngredientsList
                if (value instanceof IngredientsList) {
                    value = ((IngredientsList) value).getIngredientsListName(); // Display only the name
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        selectedList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Display only the name of IngredientsList in the selected list
                if (value instanceof IngredientsList) {
                    value = ((IngredientsList) value).getIngredientsListName(); // Display only the name
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);
        JScrollPane availableTextScrollPane = new JScrollPane(availableTextArea);
        JScrollPane selectedTextScrollPane = new JScrollPane(selectedTextArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.insets = new Insets(0, 0, 0, 0);
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 0;
        gbcButtons.weighty = 0;
        gbcButtons.anchor = GridBagConstraints.CENTER;

        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");
        Dimension buttonSize = new Dimension(100, 30);
        addButton.setPreferredSize(buttonSize);
        removeButton.setPreferredSize(buttonSize);

        //addButton.addActionListener(e -> moveItem(availableList, availableModel, selectedModel, true));
        addButton.addActionListener(e -> moveToSelected());
        removeButton.addActionListener(e -> moveItem(selectedList, selectedModel, availableModel, false));

        buttonPanel.add(addButton, gbcButtons);
        gbcButtons.gridy = 1;
        buttonPanel.add(removeButton, gbcButtons);

        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(createTitledPanel(availableTitle, availableScrollPane), BorderLayout.CENTER);
        availablePanel.add(createTitledPanel("Details", availableTextScrollPane), BorderLayout.SOUTH);

        JPanel selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.add(createTitledPanel(selectedTitle, selectedScrollPane), BorderLayout.CENTER);
        selectedPanel.add(createTitledPanel("Details", selectedTextScrollPane), BorderLayout.SOUTH);

        JPanel listsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        listsPanel.add(availablePanel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        listsPanel.add(buttonPanel, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.4;
        listsPanel.add(selectedPanel, gbc);

        add(listsPanel, BorderLayout.CENTER);

        availableList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object selectedValue = availableList.getSelectedValue();

                if (selectedValue instanceof IngredientsList) {
                    IngredientsList selectedItem = (IngredientsList) selectedValue;
                    StringBuilder sb = new StringBuilder();
                    for (Ingredient ingredient : selectedItem.getIngredients()) {
                        sb.append(ingredient.toString()).append("\n"); // Assuming Ingredient has a toString() method
                    }
                    availableTextArea.setText(sb.toString()); // Show the contents of IngredientsList in the Details section
                }
                else if (selectedValue instanceof Recipe) {
                    Recipe selectedRecipe = (Recipe) selectedValue;
                    availableTextArea.setText("Recipe: " + selectedRecipe.getName() + "\n" + selectedRecipe.getDescription());
                }
                else {
                    availableTextArea.setText(""); // Clear text area if selection is invalid
                }
            }
        });

        selectedList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedDetails(selectedList.getSelectedValue());
            }
        });

    }

    protected void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel, boolean movingToSelected) {
        T item = sourceList.getSelectedValue();

        if (item != null) {
            // Ensure only one IngredientsList can be selected at a time
            if (movingToSelected && item instanceof IngredientsList && !selectedModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Only one IngredientsList can be selected!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Move the item between available and selected models
            sourceModel.removeElement(item);
            targetModel.addElement(item);

            // Update selectedQuantities map
            if (movingToSelected) {
                selectedQuantities.putIfAbsent(item, 1);
                // Populate the details on the selected side when an item moves to selected
                if (item instanceof IngredientsList) {
                    IngredientsList selectedItem = (IngredientsList) item;
                    StringBuilder sb = new StringBuilder();
                    for (Ingredient ingredient : selectedItem.getIngredients()) {
                        sb.append(ingredient.toString()).append("\n");
                    }
                    selectedTextArea.setText(sb.toString());  // Populate the details in the selectedTextArea
                }
            } else {
                selectedQuantities.remove(item);
                // Clear the details on the selected side when the item moves back
                selectedTextArea.setText("");  // Clear the details
            }

            // Clear the details on the side the item is coming from
            if (sourceList == availableList) {
                availableTextArea.setText("");  // Clear details on the available side
            } else if (sourceList == selectedList) {
                selectedTextArea.setText("");  // Clear details on the selected side
            }

            // Clear the selection in the source list
            sourceList.clearSelection();
        }
    }

    public void moveSelectedItem() {
        T selectedValue = availableList.getSelectedValue();
        if (selectedValue != null) {
            selectedModel.clear(); // Ensure only one item in selected list
            selectedModel.addElement(selectedValue);
            availableModel.removeElement(selectedValue);
        }
    }

    private void updateSelectedDetails(Object selectedValue) {
        if (selectedValue instanceof IngredientsList) {
            IngredientsList selectedItem = (IngredientsList) selectedValue;
            StringBuilder sb = new StringBuilder();
            for (Ingredient ingredient : selectedItem.getIngredients()) {
                sb.append(ingredient.toString()).append("\n");
            }
            selectedTextArea.setText(sb.toString()); // Show IngredientsList details
        }
        else if (selectedValue instanceof Recipe) {
            Recipe selectedRecipe = (Recipe) selectedValue;
            selectedTextArea.setText("Recipe: " + selectedRecipe.getName() + "\n" + selectedRecipe.getDescription());
        }
        else {
            selectedTextArea.setText(""); // Clear text if selection is invalid
        }
    }


    private void moveToSelected() {
        Object selectedItem = availableList.getSelectedValue();

        if (selectedItem != null) {
            DefaultListModel<Object> availableModel = (DefaultListModel<Object>) availableList.getModel();
            DefaultListModel<Object> selectedModel = (DefaultListModel<Object>) selectedList.getModel();

            // Check if selected list already contains an item
            if (!selectedModel.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Only one item can be moved at a time!", "Move Error", JOptionPane.WARNING_MESSAGE);
                return; // Exit without moving if there's already an item
            }

            // Move the item
            availableModel.removeElement(selectedItem);
            selectedModel.addElement(selectedItem);

            // Set the moved item as selected
            selectedList.setSelectedValue(selectedItem, true);

            // Force update of the details panel
            updateSelectedDetails(selectedItem);
        }
    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
/*
    public IngredientsList getSelectedModelValues() {
        Enumeration<T> elements = selectedModel.elements();

        while (elements.hasMoreElements()) {
            T element = elements.nextElement();

            if (element instanceof IngredientsList) {
                return (IngredientsList) element; // Return first IngredientsList found
            }
        }

        return null; // If nothing found, return null
    }*/

    public List<T> getSelectedModelValues() {
        List<T> selectedValues = new ArrayList<>();
        Enumeration<T> elements = selectedModel.elements();

        while (elements.hasMoreElements()) {
            selectedValues.add(elements.nextElement());
        }

        return selectedValues; // Return all selected elements
    }

    public void setAvailableList(List<T> newAvailableItems) {
        availableModel.clear(); // Remove old elements
        for (T item : newAvailableItems) {
            availableModel.addElement(item);
        }

        System.out.println("setAvailableList(): availableModel size = " + availableModel.getSize());

        availableList.setModel(availableModel); // Ensure the list uses the updated model
        availableList.revalidate(); // Ensure the UI refreshes
        availableList.repaint();
    }
}


