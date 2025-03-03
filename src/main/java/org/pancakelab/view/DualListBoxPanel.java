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

    public List<T> getSelectedList() {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < availableModel.size(); i++) {
            selectedItems.add(availableModel.get(i));
        }
        return selectedItems;
    }

    public List<T> getAvailableList() {
        List<T> availableItems = new ArrayList<>();
        for (int i = 0; i < availableModel.size(); i++) {
            availableItems.add(availableModel.get(i));
        }
        return availableItems;
    }

    private JList<T> selectedList;
    private boolean showComboBox;
    private Map<T, Integer> selectedQuantities = new HashMap<>();

    public DualListBoxPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        this.showComboBox = showComboBox;
        setLayout(new BorderLayout());

        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();

        // Populate available list
        for (T item : availableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1); // Default quantity
        }

        availableList = new JList<>(availableModel);
        selectedList = new JList<>(selectedModel);

        availableList.setCellRenderer(new ComboBoxListCellRenderer<>(showComboBox, selectedQuantities));

        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");

        addButton.addActionListener(e -> moveItem(availableList, availableModel, selectedModel));
        removeButton.addActionListener(e -> moveItem(selectedList, selectedModel, availableModel));

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

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

        JPopupMenu popup = new JPopupMenu();
        popup.add(comboBox);
        popup.show(availableList, availableList.getCellBounds(index, index).x, availableList.getCellBounds(index, index).y);

        comboBox.addActionListener(e -> {
            selectedQuantities.put(item, (Integer) comboBox.getSelectedItem());
            availableList.repaint(); // Refresh list
        });
    }

    private void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel) {
        int selectedIndex = sourceList.getSelectedIndex();
        if (selectedIndex != -1) {
            T item = sourceModel.remove(selectedIndex);
            targetModel.addElement(item);
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
}


