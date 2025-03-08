package org.pancakelab.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        selectedList = new JList<>(selectedModel);
        availableTextArea = new JTextArea(5, 20);
        selectedTextArea = new JTextArea(5, 20);
        availableTextArea.setEditable(false);
        selectedTextArea.setEditable(false);

        for (T item : availableItems) {
            availableModel.addElement(item);
            selectedQuantities.put(item, 1);
        }

        availableList.setCellRenderer(new ComboBoxListCellRenderer<>(showComboBox, selectedQuantities));

        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);
        JScrollPane availableTextScrollPane = new JScrollPane(availableTextArea);
        JScrollPane selectedTextScrollPane = new JScrollPane(selectedTextArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton addButton = new JButton(">>");
        JButton removeButton = new JButton("<<");
        addButton.addActionListener(e -> moveItem(availableList, availableModel, selectedModel, true));
        removeButton.addActionListener(e -> moveItem(selectedList, selectedModel, availableModel, false));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(removeButton);

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
                T selectedItem = availableList.getSelectedValue();
                availableTextArea.setText(selectedItem != null ? selectedItem.toString() : "");
            }
        });

        selectedList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                T selectedItem = selectedList.getSelectedValue();
                selectedTextArea.setText(selectedItem != null ? selectedItem.toString() : "");
            }
        });
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

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}

