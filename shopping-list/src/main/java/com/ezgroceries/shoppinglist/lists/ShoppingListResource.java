package com.ezgroceries.shoppinglist.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingListResource {
    private final UUID shoppingListId;
    private final String name;
    private List<String> ingredients;

    public ShoppingListResource(UUID shoppingListId, String name) {
        this.shoppingListId = shoppingListId;
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void addIngredient(String ingredient){
        this.ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
