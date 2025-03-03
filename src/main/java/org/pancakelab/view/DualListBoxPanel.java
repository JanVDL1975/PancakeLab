package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DualListBoxPanel<T> extends JPanel {
    private DefaultListModel<T> availableModel;
    private DefaultListModel<T> selectedModel;
    private JList<T> availableList;
    private JList<T> selectedList;

    public DualListBoxPanel(List<T> availableItems, String availableTitle, String selectedTitle) {
        setLayout(new BorderLayout());

        // Initialize list models
        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();

        // Populate available list
        for (T item : availableItems) {
            availableModel.addElement(item);
        }

        // Create JLists
        availableList = new JList<>(availableModel);
        selectedList = new JList<>(selectedModel);

        // Create scroll panes
        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton(">>"); // Move to Selected
        JButton removeButton = new JButton("<<"); // Move back to Available

        // Button actions
        addButton.addActionListener((ActionEvent e) -> moveItem(availableList, availableModel, selectedModel));
        removeButton.addActionListener((ActionEvent e) -> moveItem(selectedList, selectedModel, availableModel));

        // Add buttons to panel
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        // Layout setup
        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        listsPanel.add(createTitledPanel(availableTitle, availableScrollPane));
        listsPanel.add(buttonPanel);
        listsPanel.add(createTitledPanel(selectedTitle, selectedScrollPane));

        add(listsPanel, BorderLayout.CENTER);
    }

    // Move an item from one list to another
    private void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel) {
        int selectedIndex = sourceList.getSelectedIndex();
        if (selectedIndex != -1) {
            T item = sourceModel.remove(selectedIndex);
            targetModel.addElement(item);
        }
    }

    // Utility method to create a titled panel
    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    // Get selected items
    public List<T> getSelectedItems() {
        return selectedList.getSelectedValuesList();
    }

    public void setAvailableList(JList<Ingredient> list) {
        availableList = (JList<T>) list;
    }
}
