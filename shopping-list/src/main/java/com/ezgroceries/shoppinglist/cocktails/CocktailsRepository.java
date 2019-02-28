package com.ezgroceries.shoppinglist.cocktails;

import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CocktailsRepository extends CrudRepository<CocktailEntity, UUID> {
}
