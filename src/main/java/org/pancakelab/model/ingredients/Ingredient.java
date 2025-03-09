package org.pancakelab.model.ingredients;

import java.util.Objects;
import java.util.UUID;

public class Ingredient {
    private UUID id;
    private String name;
    private double quantity;
    private String unit;

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Ingredient(String ingredientName) {
        this.name = ingredientName;
    }

    public Ingredient(int ingredientId, Object o, double quantity, String unit) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equals(that.name) && unit.equals(that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit); // Hash based on name and unit to ensure uniqueness
    }

    @Override
    public String toString() {
        return "item: " + name + " quantity: " + quantity + " " + "unit: " + unit + " \n";
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
