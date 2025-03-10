package org.pancakelab.model.ingredients;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class IngredientsList implements ListModel<Ingredient> {
    private UUID id;
    private String name;

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    private List<Ingredient> ingredients;
    private final List<ListDataListener> listeners = new ArrayList<>();

    // Constructors
    public IngredientsList(String name) {
        this(UUID.randomUUID(), name, new ArrayList<>());
    }

    public IngredientsList(UUID id, String name) {
        this(id, name, new ArrayList<>());
    }

    public IngredientsList(UUID id, String name, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
    }

    // ListModel methods
    @Override
    public int getSize() {
        return ingredients.size();
    }

    @Override
    public Ingredient getElementAt(int index) {
        return ingredients.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    // Method to notify listeners of data changes
    private void notifyListeners() {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(event);
        }
    }

    // Modify ingredient list and notify listeners
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        notifyListeners(); // Notify Swing listeners about the update
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        notifyListeners();
    }

    public Object getIngredientsListName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ListDataListener> getListDataListeners() {
        return new ArrayList<>(listeners); // Return a copy to prevent direct modification
    }

}




