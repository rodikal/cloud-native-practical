package com.ezgroceries.shoppinglist.cocktails.repository;

import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface CocktailRepository extends CrudRepository<CocktailEntity, UUID> {
    List<CocktailEntity> findByIdDrinkIn(List<String> ids);
    List<CocktailEntity> findAllByIdIn(List<UUID> ids);
}
