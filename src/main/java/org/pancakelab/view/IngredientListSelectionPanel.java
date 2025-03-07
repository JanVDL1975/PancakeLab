package org.pancakelab.view;

import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class IngredientListSelectionPanel<I extends IngredientsList> extends DualListBoxPanel<I> {
    private Consumer<I> selectionListener;

    public IngredientListSelectionPanel(List<I> availableItems, String availableTitle, String selectedTitle, boolean showComboBox) {
        super(availableItems, availableTitle, selectedTitle, showComboBox);
        addAvailableListSelectionListener();
    }

    @Override
    public List<I> getAvailableModelValues() {
        return new ArrayList<>();
    }

    @Override
    protected void moveItem(JList<I> sourceList, DefaultListModel<I> sourceModel, DefaultListModel<I> targetModel, boolean movingToSelected) {
        I item = sourceList.getSelectedValue();
        if (item != null) {
            sourceModel.removeElement(item);
            targetModel.addElement(item);
            sourceList.clearSelection();
        }
    }

    private void addAvailableListSelectionListener() {
        getAvailableList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Only trigger when selection is finalized
                    I selectedItem = (I) getAvailableList().getSelectedValue();
                    if (selectedItem != null && selectionListener != null) {
                        selectionListener.accept(selectedItem);
                    }
                }
            }
        });
    }

    public void setSelectionListener(Consumer<I> listener) {
        this.selectionListener = listener;
    }

    public JList<I> getAvailableList() {
        return super.getAvailableListComponent(); // This now correctly returns JList<I>
    }

}
