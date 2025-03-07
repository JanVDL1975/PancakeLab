package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IngredientListSelectionPanel<I> extends DualListBoxPanel<IngredientsList> {
    private Consumer<IngredientsList> selectionListener;

    public IngredientListSelectionPanel(List<IngredientsList> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        super(availableItems, availableTitle, selectedTitle, showComboBox);
        addAvailableListSelectionListener();
    }

    @Override
    public List<IngredientsList> getAvailableModelValues() {
        return new ArrayList<>(); // Customize if needed
    }

    @Override
    protected void moveItem(JList<IngredientsList> sourceList, DefaultListModel<IngredientsList> sourceModel, DefaultListModel<IngredientsList> targetModel, boolean movingToSelected) {
        IngredientsList item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);
            sourceList.clearSelection();
        }
    }

    public JList<IngredientsList> getAvailableList() {
        return new JList<>(new DefaultListModel<IngredientsList>());
    }

    public void setSelectionListener(Consumer<IngredientsList> listener) {
        this.selectionListener = listener;
    }

    private void addAvailableListSelectionListener() {
        getAvailableList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                IngredientsList selectedItem = getAvailableList().getSelectedValue();
                if (selectedItem != null && selectionListener != null) {
                    selectionListener.accept(selectedItem);
                }
            }
        });
    }
}
