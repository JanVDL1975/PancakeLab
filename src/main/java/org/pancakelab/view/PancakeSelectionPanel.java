package org.pancakelab.view;

import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.recipes.Recipe;

import javax.swing.*;
import java.util.List;

public class PancakeSelectionPanel extends DualListBoxPanel<Pancake> {

    public PancakeSelectionPanel(List<Pancake> availableRecipes) {
        super(availableRecipes, "Available Pancake Recipes", "Selected Pancake Recipes", false);
    }

    @Override
    protected DefaultListModel<Pancake> getAvailableModel() {
        return super.getAvailableModel();
    }

    @Override
    protected DefaultListModel<Pancake> getSelectedModel() {
        return super.getSelectedModel();
    }
}

