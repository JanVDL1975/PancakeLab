package org.pancakelab.view;

import org.pancakelab.model.pancakes.Pancake;
import org.pancakelab.model.recipes.Recipe;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PancakeSelectionPanel extends DualListBoxPanel<Pancake> {

    public PancakeSelectionPanel(List<Pancake> availablePancakes) {
        super(availablePancakes, "Available Pancakes", "Selected Pancakes", false);
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

