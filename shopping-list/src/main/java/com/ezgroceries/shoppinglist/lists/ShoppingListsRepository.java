package com.ezgroceries.shoppinglist.lists;

import com.ezgroceries.shoppinglist.lists.entities.ShoppinglistEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingListsRepository extends CrudRepository<ShoppinglistEntity, UUID> {
}
