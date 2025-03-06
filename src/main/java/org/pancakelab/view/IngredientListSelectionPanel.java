package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IngredientListSelectionPanel<T> extends DualListBoxPanel<T> {
    private Consumer<T> selectionListener; // Listener to notify selection changes

    public IngredientListSelectionPanel(List<T> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        super(availableItems, availableTitle, selectedTitle, showComboBox);
        addAvailableListSelectionListener();
    }

    @Override
    public List<T> getAvailableModelValues() {
        return new ArrayList<>();
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

    // 🔥 New method to add a selection listener for the available list
    private void addAvailableListSelectionListener() {
        getAvailableList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Only trigger when selection is finalized
                    T selectedItem = (T) getAvailableList().getSelectedValue();
                    if (selectedItem != null && selectionListener != null) {
                        selectionListener.accept(selectedItem);
                    }
                }
            }
        });
    }

    private JList<Object> getAvailableList() {
        return (JList<Object>) super.getAvailableListComponent();
    }


    // 🔥 Method to allow external components to listen for selection changes
    public void setSelectionListener(Consumer<T> listener) {
        this.selectionListener = listener;
    }
}



