package com.ezgroceries.shoppinglist.lists.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ezgroceries.shoppinglist.cocktails.service.CocktailService;
import com.ezgroceries.shoppinglist.cocktails.service.CocktailServiceImpl;
import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import com.ezgroceries.shoppinglist.lists.entities.ShoppingListEntity;
import com.ezgroceries.shoppinglist.lists.repository.ShoppingListRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ShoppingListServiceImplTest {

    private ShoppingListRepository shoppingListRepository;

    private ShoppingListService shoppingListService;

    @BeforeEach
    void setup() {
        shoppingListRepository = mock(ShoppingListRepository.class);
        CocktailService cocktailService = mock(CocktailServiceImpl.class);
        shoppingListService = new ShoppingListServiceImpl(shoppingListRepository, cocktailService);
    }

    @Test
    void create() {
        ShoppingListResource resource = new ShoppingListResource();
        resource.setName("test");

        ShoppingListEntity entity = new ShoppingListEntity();
        entity.setName("test");
        entity.setId(UUID.randomUUID());
        when(shoppingListRepository.save(any(ShoppingListEntity.class))).thenReturn(entity);

        ArgumentCaptor<ShoppingListEntity> entityCaptor = ArgumentCaptor.forClass(ShoppingListEntity.class);

        ShoppingListResource newResource = shoppingListService.create(resource);

        verify(shoppingListRepository).save(entityCaptor.capture());
        ShoppingListEntity newEntity = entityCaptor.getValue();
        assertEquals("test", newEntity.getName());
        assertNotNull(newEntity.getId());
        assertEquals("test", newResource.getName());
        assertEquals(entity.getId(), newResource.getShoppingListId());
    }

    @Test
    void get() {
    }

    @Test
    void getWithDetails() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllWithDetails() {
    }

    @Test
    void addCocktails() {
    }
}