package com.ezgroceries.shoppinglist.lists.repository;

import com.ezgroceries.shoppinglist.lists.entities.ShoppingListEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingListRepository extends CrudRepository<ShoppingListEntity, UUID> {
}
