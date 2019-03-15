package com.ezgroceries.shoppinglist.cocktails.web;

import com.ezgroceries.shoppinglist.cocktails.CocktailDBClient;
import com.ezgroceries.shoppinglist.cocktails.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import com.ezgroceries.shoppinglist.cocktails.service.CocktailService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {

    private final CocktailService cocktailService;
    private final CocktailDBClient cocktailDBClient;

    public CocktailController(CocktailService cocktailService, CocktailDBClient cocktailDBClient) {
        this.cocktailService = cocktailService;
        this.cocktailDBClient = cocktailDBClient;
    }

    @GetMapping
    public ResponseEntity<List<CocktailResource>> get(@RequestParam String search) {

        CocktailDBResponse cocktailDBResponse = cocktailDBClient.searchCocktails(search);
        return ResponseEntity.ok(convert(cocktailDBResponse));
    }

    private List<CocktailResource> convert(CocktailDBResponse dbResponse) {
        return cocktailService.mergeCocktails(dbResponse.getDrinks());

    }

}