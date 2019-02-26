package com.ezgroceries.shoppinglist.web.lists;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/shopping-lists", produces = "application/json")
public class ShoppingListController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ShoppingListResource> create(@RequestBody Map<String, String> body) {
        UUID id = UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
        ShoppingListResource shoppingList = new ShoppingListResource(id, body.get("name"));
        // TODO store shoppingList
        return entityWithLocation(id).body(shoppingList);
    }

    @PostMapping(path = "/{id}/cocktails")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String, String>>> addCocktails(@PathVariable String id, @RequestBody List<Map<String, String>> body) {
        // TODO get shoppingList
        // TODO add cocktails to shopping list
        return ResponseEntity.ok(body.subList(0, 1));
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ShoppingListResource> getList(@PathVariable String id) {
        // TODO get shoppingList
        UUID uuid = UUID.fromString(id);
        ShoppingListResource shoppingList = new ShoppingListResource(uuid, "Stephanie's birthday");
        Arrays.asList("Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                "Blue Curacao").forEach(shoppingList::addIngredient);
        return ResponseEntity.ok(shoppingList);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ShoppingListResource>> getList() {
        // TODO get shoppingLists
        List<ShoppingListResource> lists = new ArrayList<>();
        {
            UUID uuid = UUID.fromString("4ba92a46-1d1b-4e52-8e38-13cd56c7224c");
            ShoppingListResource shoppingList = new ShoppingListResource(uuid, "Stephanie's birthday");
            Arrays.asList("Tequila",
                    "Triple sec",
                    "Lime juice",
                    "Salt",
                    "Blue Curacao").forEach(shoppingList::addIngredient);
            lists.add(shoppingList);
        }
        {
            UUID uuid = UUID.fromString("6c7d09c2-8a25-4d54-a979-25ae779d2465");
            ShoppingListResource shoppingList = new ShoppingListResource(uuid, "My birthday");
            Arrays.asList("Tequila",
                    "Triple sec",
                    "Lime juice",
                    "Salt",
                    "Blue Curacao").forEach(shoppingList::addIngredient);
            lists.add(shoppingList);
        }
        return ResponseEntity.ok(lists);
    }


    private ResponseEntity.BodyBuilder entityWithLocation(Object resourceId) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location);
    }

}