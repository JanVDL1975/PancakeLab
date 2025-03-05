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
        //JPanel buttonPanel = new JPanel(new GridLayout(1, 1, 5, 5));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonPanel.setPreferredSize(new Dimension(100, 120)); // Adjust width to better center

        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");

// Ensure buttons are centered
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

// Ensure buttons do not stretch to the left
        addButton.setMaximumSize(new Dimension(80, 40));
        removeButton.setMaximumSize(new Dimension(80, 40));

        buttonPanel.add(Box.createVerticalGlue()); // Push buttons toward center
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Space between buttons
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons toward center



        // Layout Setup
        // Layout Setup for Main Panel
        JPanel listsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10); // Add padding to balance spacing

// Available List Panel (Left)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4; // Adjust to balance width
        gbc.fill = GridBagConstraints.BOTH;
        listsPanel.add(createTitledPanel(availableTitle, availableScrollPane), gbc);

// Buttons Panel (Center)
        gbc.gridx = 1;
        gbc.weightx = 0.2; // Center column width
        gbc.anchor = GridBagConstraints.CENTER; // Ensure it is centered
        listsPanel.add(buttonPanel, gbc);

// Selected List Panel (Right)
        gbc.gridx = 2;
        gbc.weightx = 0.4;
        listsPanel.add(createTitledPanel(selectedTitle, selectedScrollPane), gbc);


// Add to main panel
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



