package org.pancakelab.view;

import org.pancakelab.model.ingredients.Ingredient;
import org.pancakelab.model.ingredients.IngredientsList;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ListDisplayPanel<T, I> extends JPanel implements Consumer<I> {
    private final DefaultListModel<T> availableModel = new DefaultListModel<>();
    List<T> availableItems;
    private final JPanel displayPanel = new JPanel();
    JList<T> availableList;
    JTextArea availableTextArea;

    public ListDisplayPanel(List<T> availableItems) {
        this.availableItems = availableItems;

        // Populate available list with default quantities
        for (T item : availableItems) {
            availableModel.addElement(item);
        }

        // Create JList and add it to a JScrollPane
        availableList = new JList<>(availableModel);

        // Set the layout and add the scroll pane
        setLayout(new BorderLayout());

        // Optionally create and add a JTextArea for details
        availableTextArea = new JTextArea();
        availableTextArea.setPreferredSize(new Dimension(300, 200));
        availableTextArea.append(availableItems.size() + " items available");
        JScrollPane textScrollPane = new JScrollPane(availableTextArea);
        add(textScrollPane, BorderLayout.CENTER); // Add the text area to the bottom of the panel
    }

    @Override
    public void accept(I ingredientsList) {
        // Check if ingredientsList is an instance of List<Ingredient>
        if (ingredientsList instanceof IngredientsList) {
            List<Ingredient> ingredientList = ((IngredientsList) ingredientsList).getIngredientList();

            // Clear the text area (optional) or append the formatted list
            for (Ingredient ingredient : ingredientList) {
                // Append each ingredient, formatted as needed
                availableTextArea.append(ingredient.toString() + "\n");
            }
        } else {
            // Handle the case when ingredientsList is not a List<Ingredient>
            availableTextArea.append("Invalid ingredients list.\n");
        }
    }

}

