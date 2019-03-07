package com.ezgroceries.shoppinglist.cocktails.service;

import com.ezgroceries.shoppinglist.cocktails.CocktailDBClient;
import com.ezgroceries.shoppinglist.cocktails.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktails.repository.CocktailRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CocktailService  {

    private CocktailRepository cocktailRepository;

    private CocktailDBClient cocktailDBClient;

    @Autowired
    public void setCocktailRepository(CocktailRepository cocktailRepository){
        this.cocktailRepository = cocktailRepository;
    }

    @Autowired
    public void setCocktailDBClient(CocktailDBClient cocktailDBClient) {
        this.cocktailDBClient = cocktailDBClient;
    }

    public List<CocktailResource> mergeCocktails(List<CocktailDBResponse.DrinkResource> drinks) {
        //Get all the idDrink attributes
        List<String> ids = drinks.stream().map(CocktailDBResponse.DrinkResource::getIdDrink).collect(Collectors.toList());

        //Get all the ones we already have from our DB, use a Map for convenient lookup
        Map<String, CocktailEntity> existingEntityMap = cocktailRepository.findByIdDrinkIn(ids).stream().collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Stream over all the drinks, map them to the existing ones, persist a new one if not existing
        Map<String, CocktailEntity> allEntityMap = drinks.stream().map(drinkResource -> {
            CocktailEntity cocktailEntity = existingEntityMap.get(drinkResource.getIdDrink());
            if (cocktailEntity == null) {
                CocktailEntity newCocktailEntity = new CocktailEntity();
                newCocktailEntity.setId(UUID.randomUUID());
                newCocktailEntity.setIdDrink(drinkResource.getIdDrink());
                newCocktailEntity.setName(drinkResource.getStrDrink());
                cocktailEntity = cocktailRepository.save(newCocktailEntity);
            }
            return cocktailEntity;
        }).collect(Collectors.toMap(CocktailEntity::getIdDrink, o -> o, (o, o2) -> o));

        //Merge drinks and our entities, transform to CocktailResource instances
        return mergeAndTransform(drinks, allEntityMap);
    }

    private List<CocktailResource> mergeAndTransform(List<CocktailDBResponse.DrinkResource> drinks, Map<String, CocktailEntity> allEntityMap) {
        return drinks.stream().map(drinkResource -> new CocktailResource(allEntityMap.get(drinkResource.getIdDrink()).getId(), drinkResource.getStrDrink(), drinkResource.getStrGlass(),
                drinkResource.getStrInstructions(), drinkResource.getStrDrinkThumb(), getIngredients(drinkResource))).collect(Collectors.toList());
    }

    private List<String> getIngredients(CocktailDBResponse.DrinkResource drinkResource) {
        return Stream.of(
                drinkResource.getStrIngredient1(),
                drinkResource.getStrIngredient2(),
                drinkResource.getStrIngredient3(),
                drinkResource.getStrIngredient4(),
                drinkResource.getStrIngredient5()
        ).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    public List<CocktailEntity> getAllById(List<String> cocktails) {
        return cocktailRepository.findAllById(cocktails.stream().map(UUID::fromString).collect(Collectors.toList()));
    }

    public List<CocktailResource> getAllResourcesById(List<String> cocktails) {
        List<CocktailEntity> allById = this.getAllById(cocktails);

        List<CocktailDBResponse.DrinkResource> resources = allById.stream().map(CocktailEntity::getIdDrink).map(cocktailDBClient::getCocktail).map(e -> e.getDrinks().get(0)).collect(Collectors.toList());
        return mergeCocktails(resources);
    }
}
