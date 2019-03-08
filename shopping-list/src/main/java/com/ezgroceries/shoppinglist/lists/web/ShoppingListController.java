package com.ezgroceries.shoppinglist.lists.web;

import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import com.ezgroceries.shoppinglist.lists.service.ShoppingListService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ShoppingListService shoppingListService;

    @Autowired
    public void setShoppingListService(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ShoppingListResource> create(@RequestBody Map<String, String> body) {
        ShoppingListResource shoppingList = new ShoppingListResource();
        shoppingList.setName(body.get("name"));
        shoppingList = shoppingListService.create(shoppingList);
        return entityWithLocation(shoppingList.getShoppingListId()).body(shoppingList);
    }

    @PostMapping(path = "/{id}/cocktails")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Map<String, String>>> addCocktails(@PathVariable String id, @RequestBody List<Map<String, String>> body) {
        ShoppingListResource resource = shoppingListService.get(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        List<String> cocktails = body.stream().map(map -> map.get("cocktailId")).collect(Collectors.toList());

        shoppingListService.addCocktails(resource, cocktails);

        return ResponseEntity.ok(body);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ShoppingListResource> getList(@PathVariable String id) {
        ShoppingListResource shoppingList = shoppingListService.getWithDetails(id);
        if (shoppingList == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(shoppingList);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ShoppingListResource>> getList() {
        return ResponseEntity.ok(shoppingListService.getAllWithDetails());
    }


    private ResponseEntity.BodyBuilder entityWithLocation(Object resourceId) {
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location);
    }

}