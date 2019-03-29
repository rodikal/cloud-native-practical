package com.ezgroceries.shoppinglist.cocktails.service;

import com.ezgroceries.shoppinglist.cocktails.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import java.util.List;

public interface CocktailService {
    List<CocktailResource> mergeCocktails(List<CocktailDBResponse.DrinkResource> drinks);

    List<CocktailEntity> getAllById(List<String> cocktails);

    List<CocktailResource> getAllResourcesById(List<String> cocktails);

    List<CocktailResource> searchCocktails(String search);
}
