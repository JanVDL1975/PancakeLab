package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;

public class MilkChocolatePancake implements PancakeRecipe {
    private UUID orderId;

    @Override
    public String getName() {
        return "MilkChocolatePancake";
    }

    @Override
    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<String> ingredients() {
        return List.of("milk chocolate");
    }

    @Override
    public UUID getRecipeId() {
        return UUID.randomUUID();
    }
}
