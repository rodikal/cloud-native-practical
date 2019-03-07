package com.ezgroceries.shoppinglist.lists;

import java.util.List;
import java.util.UUID;

public class ShoppingListResource {
    private UUID shoppingListId;
    private String name;
    private List<String> ingredients;

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(UUID shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient){
        this.ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
