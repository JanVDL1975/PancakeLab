package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ListDisplayPanel<T, I> extends JPanel implements Consumer<I> {
    private final JTextArea availableTextArea;

    public ListDisplayPanel(DualListBoxPanel<I> dualListBoxPanel) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Ingredients: "));
        add(dualListBoxPanel, BorderLayout.NORTH);

        JPanel ingredientsDisplayPanel = new JPanel();
        ingredientsDisplayPanel.setBorder(BorderFactory.createTitledBorder("Ingredients:"));

        availableTextArea = new JTextArea(5, 30);
        availableTextArea.setEditable(false);
        ingredientsDisplayPanel.add(availableTextArea);

        add(ingredientsDisplayPanel, BorderLayout.SOUTH);
    }

    @Override
    public void accept(I ingredientsList) {
        if (ingredientsList instanceof IngredientsList) {
            availableTextArea.setText(""); // Clear previous text
            for (Ingredient ingredient : ((IngredientsList) ingredientsList).getIngredients()) {
                availableTextArea.append(ingredient.getName() + ": " + ingredient.getQuantity() + " " + ingredient.getUnit() + "\n");
            }
        } else {
            availableTextArea.setText("Invalid ingredients list.");
        }
    }
}




