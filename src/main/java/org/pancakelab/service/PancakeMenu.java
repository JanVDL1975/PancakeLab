package org.pancakelab.service;

import org.pancakelab.model.pancakes.*;

import java.util.*;

public class PancakeMenu {
    private final Map<String, Class<? extends PancakeRecipe>> menuItems = new HashMap<>();

    public PancakeMenu() {
        menuItems.put("Dark Chocolate", DarkChocolatePancake.class);
        menuItems.put("Milk Chocolate", MilkChocolatePancake.class);
        menuItems.put("Dark Chocolate Whipped Cream", DarkChocolateWhippedCreamPancake.class);
        menuItems.put("Milk Chocolate Hazelnuts", MilkChocolateHazelnutsPancake.class);
    }

    public Set<String> getMenuItems() {
        return menuItems.keySet();
    }

    public PancakeRecipe createPancake(String name) {
        if (!menuItems.containsKey(name)) {
            throw new IllegalArgumentException("Pancake not on menu.");
        }
        try {
            return menuItems.get(name).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create pancake: " + name, e);
        }
    }

    public Set<String> getAvailablePancakes() {
        return menuItems.keySet();
    }
}

