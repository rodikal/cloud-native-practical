package com.ezgroceries.shoppinglist.lists.service;

import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktails.service.CocktailService;
import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import com.ezgroceries.shoppinglist.lists.entities.ShoppingListEntity;
import com.ezgroceries.shoppinglist.lists.repository.ShoppingListRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    private final CocktailService cocktailService;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CocktailService cocktailService) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailService = cocktailService;
    }

    public ShoppingListResource create(ShoppingListResource shoppingListResource) {
        ShoppingListEntity entity = new ShoppingListEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(shoppingListResource.getName());
        entity = shoppingListRepository.save(entity);
        shoppingListResource.setShoppingListId(entity.getId());
        return shoppingListResource;
    }

    public ShoppingListResource get(String uuid) {
        Optional<ShoppingListEntity> entity = shoppingListRepository.findById(UUID.fromString(uuid));
        return entity.map(this::mapEntity).orElse(null);
    }

    public ShoppingListResource getWithDetails(String uuid) {
        Optional<ShoppingListEntity> entity = shoppingListRepository.findById(UUID.fromString(uuid));
        return entity.map(this::mapEntityWithIngredients).orElse(null);
    }

    public List<ShoppingListResource> getAll() {
        List<ShoppingListEntity> entity = shoppingListRepository.findAll();
        return entity.stream().map(this::mapEntity).collect(Collectors.toList());
    }

    public List<ShoppingListResource> getAllWithDetails() {
        List<ShoppingListEntity> entity = shoppingListRepository.findAll();
        return entity.stream().map(this::mapEntityWithIngredients).collect(Collectors.toList());
    }

    private ShoppingListResource mapEntity(ShoppingListEntity entity){
        ShoppingListResource resource = new ShoppingListResource();
        resource.setName(entity.getName());
        resource.setShoppingListId(entity.getId());
        return resource;
    }

    private ShoppingListResource mapEntityWithIngredients(ShoppingListEntity entity){
        ShoppingListResource resource = mapEntity(entity);
        List<String> collect = entity.getCocktails().stream().map(CocktailEntity::getId).map(UUID::toString).collect(Collectors.toList());
        List<String> ingredients = cocktailService.getAllResourcesById(collect).stream().map(CocktailResource::getIngredients).flatMap(List::stream).distinct().collect(Collectors.toList());
        resource.setIngredients(ingredients);
        return resource;
    }

    public ShoppingListResource addCocktails(ShoppingListResource resource, List<String> cocktails) {
        List<CocktailEntity> cocktailEntities = cocktailService.getAllById(cocktails);
        Optional<ShoppingListEntity> shoppingListEntity = shoppingListRepository.findById(resource.getShoppingListId());
        if(!shoppingListEntity.isPresent()){
            return null;
        }
        ShoppingListEntity entity = shoppingListEntity.get();
        entity.addCocktails(cocktailEntities);
        shoppingListRepository.save(entity);
        return mapEntity(entity);
    }

}