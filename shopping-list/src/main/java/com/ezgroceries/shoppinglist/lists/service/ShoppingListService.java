package com.ezgroceries.shoppinglist.lists.service;

import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import java.util.List;

public interface ShoppingListService {
    ShoppingListResource create(ShoppingListResource shoppingListResource);

    ShoppingListResource get(String uuid);

    ShoppingListResource getWithDetails(String uuid);

    List<ShoppingListResource> getAll();

    List<ShoppingListResource> getAllWithDetails();

    ShoppingListResource addCocktails(ShoppingListResource resource, List<String> cocktails);
}
