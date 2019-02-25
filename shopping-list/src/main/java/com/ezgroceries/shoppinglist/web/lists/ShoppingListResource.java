package com.ezgroceries.shoppinglist.web.lists;

import java.util.UUID;

public class ShoppingListResource {
    private final UUID shoppingListId;
    private final String name;

    public ShoppingListResource(UUID shoppingListId, String name) {
        this.shoppingListId = shoppingListId;
        this.name = name;
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }

    public String getName() {
        return name;
    }
}
