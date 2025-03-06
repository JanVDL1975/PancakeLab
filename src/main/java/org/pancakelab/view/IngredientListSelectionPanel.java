package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientListSelectionPanel<T> extends DualListBoxPanel<T> {
    public IngredientListSelectionPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        super(availableItems, availableTitle, selectedTitle, showComboBox);
    }

    @Override
    public List<T> getAvailableModelValues() {
        List<T> list = new ArrayList<>();
        return list;
    }

    public void refreshAvailableIngredientsLists(List<T> updatedLists) {
        // Implement logic to update available items
    }

    @Override
    protected void moveItem(JList<T> sourceList, DefaultListModel<T> sourceModel, DefaultListModel<T> targetModel, boolean movingToSelected) {
        T item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);
            sourceList.clearSelection();
        }
    }
}


