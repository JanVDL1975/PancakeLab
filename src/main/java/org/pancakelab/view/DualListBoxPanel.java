package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DualListBoxPanel<T> extends JPanel {
    private final DefaultListModel<T> availableModel;
    private final DefaultListModel<T> selectedModel;
    private JList<T> availableList;
    private final JList<T> selectedList;
    private final Map<T, Integer> selectedQuantities;
    private final boolean showComboBox;
    
    private void showComboBoxPopup(T item, int index) {
        JComboBox<Integer> comboBox = new JComboBox<>();
        for (int i = 1; i <= 10; i++) {
            comboBox.addItem(i);
        }
        comboBox.setSelectedItem(selectedQuantities.get(item));

        Rectangle cellBounds = availableList.getCellBounds(index, index);
        if (cellBounds != null) {
            JPopupMenu popup = new JPopupMenu();
            popup.add(comboBox);
            popup.show(availableList, cellBounds.x + cellBounds.width - 50, cellBounds.y);

            comboBox.addActionListener(e -> {
                selectedQuantities.put(item, (Integer) comboBox.getSelectedItem());
                availableList.repaint();
            });
        }
    }

    public DualListBoxPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        this.showComboBox = showComboBox;
        this.selectedQuantities = new HashMap<>();
        setLayout(new BorderLayout());

        // Initialize list models
        availableModel = new DefaultListModel<>();
        selectedModel = new DefaultListModel<>();
        // Create JLists
        availableList = new JList<>(availableModel);
        selectedList = new JList<>(selectedModel);

        // Add ListDataListener for both models
        availableModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                notifyListChanged();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                notifyListChanged();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                notifyListChanged();
            }
        });

        selectedModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                notifyListChanged();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                notifyListChanged();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                notifyListChanged();
            }
        });

        // Populate available list with default quantities
        for (T item : availableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1); // Default quantity
        }

        // Set custom renderer
        availableList.setCellRenderer(new ComboBoxListCellRenderer<>(showComboBox, selectedQuantities));

        // Create scroll panes
        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonPanel.setPreferredSize(new Dimension(100, 120)); // Adjust width

        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");

        addButton.addActionListener(e -> moveItem(availableList, availableModel, selectedModel, true));
        removeButton.addActionListener(e -> moveItem(selectedList, selectedModel, availableModel, false));

        // Center buttons
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(80, 40));
        removeButton.setMaximumSize(new Dimension(80, 40));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Layout Setup
        JPanel listsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        listsPanel.add(createTitledPanel(availableTitle, availableScrollPane), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.CENTER;
        listsPanel.add(buttonPanel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.4;
        listsPanel.add(createTitledPanel(selectedTitle, selectedScrollPane), gbc);

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

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private void notifyListChanged() {
    }

    protected void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel, boolean movingToSelected) {
        T item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);

            if (movingToSelected) {
                selectedQuantities.putIfAbsent(item, 1); // Ensure default quantity
            } else {
                selectedQuantities.remove(item); // Remove quantity when moving back
            }

            sourceList.clearSelection();
        }
    }

    protected DefaultListModel<T> getAvailableModel() {
        return new DefaultListModel<>();
    }

    public Map<T, Integer> getSelectedQuantities() {
        return selectedQuantities;
    }

    public void resetModels(List<T> initialAvailableItems) {
        System.out.println("Resetting models with: " + initialAvailableItems);
        availableModel.clear();
        selectedModel.clear();
        selectedQuantities.clear();

        for (T item : initialAvailableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1);
        }
    }

    public void refreshAvailableIngredientsLists(List<T> newList) {
        resetModels(newList); // Reset with the new list of items
    }

    protected DefaultListModel<T> getSelectedModel() {
        return selectedModel;
    }

    /*protected T getAvailableListComponent() {
        return (T) availableList;
    }*/

    protected JList<T> getAvailableListComponent() {
        return availableList; // Return JList<T> directly
    }

    public List<T> getSelectedModelValues() {
        List<T> selectedItems = new ArrayList<>();
        for (int i = 0; i < selectedModel.getSize(); i++) {
            selectedItems.add(selectedModel.getElementAt(i));
        }

        return selectedItems;
    }

    public List<T> getAvailableModelValues() {
        List<T> availableItems = new ArrayList<>();
        for (int i = 0; i < availableModel.getSize(); i++) {
            availableItems.add(availableModel.getElementAt(i));
        }
        return availableItems;
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


