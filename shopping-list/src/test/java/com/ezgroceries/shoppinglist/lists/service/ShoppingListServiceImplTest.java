package com.ezgroceries.shoppinglist.lists.service;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import com.ezgroceries.shoppinglist.cocktails.service.CocktailService;
import com.ezgroceries.shoppinglist.cocktails.service.CocktailServiceImpl;
import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import com.ezgroceries.shoppinglist.lists.entities.ShoppingListEntity;
import com.ezgroceries.shoppinglist.lists.repository.ShoppingListRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ShoppingListServiceImplTest {

    private ShoppingListRepository shoppingListRepository;

    private ShoppingListService shoppingListService;
    private ShoppingListEntity mockEntity;
    private CocktailService cocktailService;
    private List<CocktailResource> mockCocktails;
    private ShoppingListResource mockResource;
    private List<CocktailEntity> mockCocktailEntities;
    private ShoppingListEntity mockEntity2;
    private List<String> allIngredients;

    @BeforeEach
    void setup() {
        shoppingListRepository = mock(ShoppingListRepository.class);
        cocktailService = mock(CocktailServiceImpl.class);
        shoppingListService = new ShoppingListServiceImpl(shoppingListRepository, cocktailService);

        UUID cocktailId1 = UUID.randomUUID();
        UUID cocktailId2 = UUID.randomUUID();
        mockCocktails = Arrays.asList(
                new CocktailResource(cocktailId1, "cocktail 1", "a glass", "just do it", "image", Arrays.asList("thing 1", "thing 2")),
                new CocktailResource(cocktailId2, "cocktail 2", "a glass", "just do it", "image", Arrays.asList("thing 3", "thing 4"))
        );

        CocktailEntity cocktailEntity = new CocktailEntity();
        cocktailEntity.setId(cocktailId1);
        cocktailEntity.setIdDrink("0001");
        cocktailEntity.setName("cocktail 1");

        CocktailEntity cocktailEntity2 = new CocktailEntity();
        cocktailEntity2.setId(cocktailId2);
        cocktailEntity2.setIdDrink("0002");
        cocktailEntity2.setName("cocktail 2");

        allIngredients = mockCocktails.stream().map(CocktailResource::getIngredients).flatMap(List::stream).collect(Collectors.toList());

        mockCocktailEntities = Arrays.asList(cocktailEntity, cocktailEntity2);

        mockEntity = new ShoppingListEntity();
        mockEntity.setName("test");
        UUID id = UUID.randomUUID();
        mockEntity.setId(id);

        mockEntity2 = new ShoppingListEntity();
        mockEntity2.setId(UUID.randomUUID());
        mockEntity2.setName("test 2");
        mockEntity2.addCocktails(mockCocktailEntities);

        mockResource = new ShoppingListResource();
        mockResource.setName("test");
        mockResource.setShoppingListId(id);

        mockResource.setIngredients(allIngredients);

        when(shoppingListRepository.findAll()).thenReturn(Arrays.asList(mockEntity, mockEntity2));
        when(shoppingListRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockEntity));
        when(shoppingListRepository.save(any(ShoppingListEntity.class))).thenReturn(mockEntity);

        when(cocktailService.getAllById(any())).thenReturn(mockCocktailEntities);
        when(cocktailService.getAllResourcesById(argThat(CollectionUtils::isNotEmpty))).thenReturn(mockCocktails);
    }

    @Test
    void create() {
        ArgumentCaptor<ShoppingListEntity> entityCaptor = ArgumentCaptor.forClass(ShoppingListEntity.class);

        ShoppingListResource resource = new ShoppingListResource();
        resource.setName("test");
        ShoppingListResource newResource = shoppingListService.create(resource);

        verify(shoppingListRepository).save(entityCaptor.capture());
        ShoppingListEntity newEntity = entityCaptor.getValue();
        assertAll(
                () -> assertEquals("test", newEntity.getName()),
                () -> assertNotNull(newEntity.getId()),
                () -> assertEquals("test", newResource.getName()),
                () -> assertEquals(mockEntity.getId(), newResource.getShoppingListId())
        );
    }

    @Test
    void get() {
        UUID uuid = UUID.randomUUID();
        ShoppingListResource resource = shoppingListService.get(uuid.toString());
        validateShoppingList(resource, mockEntity.getId(), mockEntity.getName());

        verify(shoppingListRepository).findById(uuid);
    }

    @Test
    void getWithDetails() {

        when(shoppingListRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockEntity2));

        UUID uuid = UUID.randomUUID();
        ShoppingListResource resource = shoppingListService.getWithDetails(uuid.toString());
        validateShoppingList(resource, mockEntity2.getId(), mockEntity2.getName(), allIngredients);

        verify(shoppingListRepository).findById(uuid);

    }

    @Test
    void getAll() {

        List<ShoppingListResource> resources = shoppingListService.getAll();
        assertEquals(2, resources.size());
        validateShoppingList(resources.get(0), mockEntity.getId(), mockEntity.getName());
        validateShoppingList(resources.get(1), mockEntity2.getId(), mockEntity2.getName());

        verify(shoppingListRepository).findAll();
    }

    @Test
    void getAllWithDetails() {

        List<ShoppingListResource> resources = shoppingListService.getAllWithDetails();
        assertEquals(2, resources.size());
        validateShoppingList(resources.get(0), mockEntity.getId(), mockEntity.getName());
        validateShoppingList(resources.get(1), mockEntity2.getId(), mockEntity2.getName(), allIngredients);

        verify(shoppingListRepository).findAll();
    }

    @Test
    void addCocktails() {

        List<String> cocktailsToAdd = mockCocktails
                .stream()
                .map(CocktailResource::getCocktailId)
                .map(UUID::toString)
                .collect(Collectors.toList());

        shoppingListService.addCocktails(mockResource, cocktailsToAdd);
        verify(cocktailService).getAllById(cocktailsToAdd);
        verify(shoppingListRepository).findById(mockResource.getShoppingListId());
        verify(shoppingListRepository).save(any(ShoppingListEntity.class));
    }

    private void validateShoppingList(ShoppingListResource resource, UUID id, String name) {
        assertAll(
                () -> assertEquals(id, resource.getShoppingListId()),
                () -> assertEquals(name, resource.getName()),
                () -> assertTrue(isEmpty(resource.getIngredients()), "Ingredient list should be empty")
        );
    }

    private void validateShoppingList(ShoppingListResource resource, UUID id, String name, Collection<?> ingredients) {
        assertAll(
                () -> assertEquals(id, resource.getShoppingListId()),
                () -> assertEquals(name, resource.getName()),
                () -> assertEquals(ingredients, resource.getIngredients())
        );
    }
}
