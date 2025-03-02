package org.pancakelab.model.pancakes;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PancakeTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Recipe Description"};
    private List<Pancake> pancakes;

    public PancakeTableModel(List<Pancake> pancakes) {
        this.pancakes = pancakes;
    }

    @Override
    public int getRowCount() { return pancakes.size(); }
    @Override
    public int getColumnCount() { return columnNames.length; }
    @Override
    public String getColumnName(int column) { return columnNames[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pancake pancake = pancakes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pancake.getId();
            case 1 -> pancake.getRecipe().description();
            default -> null;
        };
    }

    public void updateData(List<Pancake> newPancakes) {
        this.pancakes = newPancakes;
        fireTableDataChanged();
    }
}

