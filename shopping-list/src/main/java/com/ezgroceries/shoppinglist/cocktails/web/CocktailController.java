package com.ezgroceries.shoppinglist.cocktails.web;

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

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping
    public ResponseEntity<List<CocktailResource>> get(@RequestParam String search) {
        List<CocktailResource> cocktailDBResponse = cocktailService.searchCocktails(search);
        return ResponseEntity.ok(cocktailDBResponse);
    }

}