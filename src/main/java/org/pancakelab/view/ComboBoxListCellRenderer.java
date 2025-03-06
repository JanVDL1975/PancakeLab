package org.pancakelab.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ComboBoxListCellRenderer<T> extends JPanel implements ListCellRenderer<T> {
    private final JLabel label;
    private final boolean showComboBox;
    private final Map<T, Integer> selectedQuantities;

    public ComboBoxListCellRenderer(boolean showComboBox, Map<T, Integer> selectedQuantities) {
        this.showComboBox = showComboBox;
        this.selectedQuantities = selectedQuantities;
        setLayout(new BorderLayout());
        label = new JLabel();
        add(label, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
        label.setText(value.toString() + (showComboBox ? " (" + selectedQuantities.getOrDefault(value, 1) + ")" : ""));

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


