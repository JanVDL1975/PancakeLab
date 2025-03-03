package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DualListBoxPanel<T> extends JPanel {
    private DefaultListModel<T> availableModel;
    private DefaultListModel<T> selectedModel;
    private JList<T> availableList;
    private JList<T> selectedList;
    private List<Integer> availableQuantities; // Holds selected quantities per item
    private boolean enableQuantitySelection; // Controls visibility of ComboBox

    public DualListBoxPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean enableQuantitySelection) {
        this.enableQuantitySelection = enableQuantitySelection; // Set visibility flag
        setLayout(new BorderLayout());

        // Initialize list models
        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();
        availableQuantities = new ArrayList<>();

        // Populate available list
        for (T item : availableItems) {
            availableModel.addElement(item);
            availableQuantities.add(1); // Default quantity = 1
        }

        // Create JLists
        availableList = new JList<>(availableModel);
        selectedList = new JList<>(selectedModel);

        // Set custom renderer for available list
        availableList.setCellRenderer(new ComboBoxListCellRenderer<>());

        // Create scroll panes
        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton(">>"); // Move to Selected
        JButton removeButton = new JButton("<<"); // Move back to Available

        // Button actions
        addButton.addActionListener((ActionEvent e) -> moveItem());
        removeButton.addActionListener((ActionEvent e) -> moveBackItem());

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

    // Move an item from available to selected
    private void moveItem() {
        int selectedIndex = availableList.getSelectedIndex();
        if (selectedIndex != -1) {
            T item = availableModel.getElementAt(selectedIndex);
            int quantity = enableQuantitySelection ? availableQuantities.get(selectedIndex) : 1; // Use selected quantity if enabled

            // Add multiple copies based on selected quantity
            for (int i = 0; i < quantity; i++) {
                selectedModel.addElement(item);
            }

            availableModel.remove(selectedIndex);
            availableQuantities.remove(selectedIndex);
        }
    }

    // Move item back to available list
    private void moveBackItem() {
        int selectedIndex = selectedList.getSelectedIndex();
        if (selectedIndex != -1) {
            T item = selectedModel.remove(selectedIndex);
            availableModel.addElement(item);
            availableQuantities.add(1); // Default quantity back to 1
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

    // Custom Renderer for List with ComboBox
    private class ComboBoxListCellRenderer<T> extends JPanel implements ListCellRenderer<T> {
        private JLabel label;
        private JComboBox<Integer> comboBox;

        public ComboBoxListCellRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            comboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5}); // Selectable quantities

            if (enableQuantitySelection) {
                add(comboBox, BorderLayout.EAST); // Show ComboBox only if enabled
            }

            add(label, BorderLayout.WEST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            label.setText(value.toString());

            if (enableQuantitySelection) {
                comboBox.setSelectedIndex(0); // Default to 1
                comboBox.addActionListener(e -> availableQuantities.set(index, (Integer) comboBox.getSelectedItem()));
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }
}
