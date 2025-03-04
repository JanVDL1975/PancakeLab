package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DualListBoxPanel<T> extends JPanel {
    private DefaultListModel<T> availableModel;
    private DefaultListModel<T> selectedModel;
    private JList<T> availableList;
    private JList<T> selectedList;
    private Map<T, Integer> selectedQuantities;
    private boolean showComboBox;

    public DualListBoxPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        this.showComboBox = showComboBox;
        this.selectedQuantities = new HashMap<>();
        setLayout(new BorderLayout());

        // Initialize list models
        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();

        // Populate available list with default quantities
        for (T item : availableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1); // Default quantity
        }

        // Create JLists
        availableList = new JList<>(availableModel);
        selectedList = new JList<>(selectedModel);

        // Set custom renderer
        availableList.setCellRenderer(new ComboBoxListCellRenderer<>(showComboBox, selectedQuantities));

        // Create scroll panes
        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");

        addButton.addActionListener(e -> moveItem(availableList, availableModel, selectedModel, true));
        removeButton.addActionListener(e -> moveItem(selectedList, selectedModel, availableModel, false));

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        // Layout Setup
        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        listsPanel.add(createTitledPanel(availableTitle, availableScrollPane));
        listsPanel.add(buttonPanel);
        listsPanel.add(createTitledPanel(selectedTitle, selectedScrollPane));

        add(listsPanel, BorderLayout.CENTER);

        // MouseListener to show ComboBox on click
        availableList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = availableList.locationToIndex(e.getPoint());
                if (index != -1 && showComboBox) {
                    T item = availableModel.getElementAt(index);
                    showComboBoxPopup(item, index);
                }
            }
        });
    }

    private void showComboBoxPopup(T item, int index) {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            comboBox.addItem(i);
        }
        comboBox.setSelectedItem(selectedQuantities.get(item));

        // Position popup at the right location
        Rectangle cellBounds = availableList.getCellBounds(index, index);
        if (cellBounds != null) {
            JPopupMenu popup = new JPopupMenu();
            popup.add(comboBox);
            popup.show(availableList, cellBounds.x + cellBounds.width - 50, cellBounds.y);

            comboBox.addActionListener(e -> {
                selectedQuantities.put(item, (Integer) comboBox.getSelectedItem());
                availableList.repaint(); // Refresh list
            });
        }
    }

    private void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel, boolean movingToSelected) {
        //int selectedIndex = sourceList.getSelectedIndex();
        T item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);
            if (!movingToSelected) {
                selectedQuantities.remove(item);
            }
        }

    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public Map<T, Integer> getSelectedQuantities() {
        return selectedQuantities;
    }

    public void setAvailableList(JList<Ingredient> list) {
        availableList = (JList<T>) list;
    }

    public List<T> getSelectedModelValues() {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < selectedModel.getSize(); i++) {
            selectedItems.add(selectedModel.getElementAt(i));
        }
        return selectedItems;
    }

}



