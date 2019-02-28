package com.ezgroceries.shoppinglist.cocktails.web;

import com.ezgroceries.shoppinglist.cocktails.CocktailDBClient;
import com.ezgroceries.shoppinglist.cocktails.CocktailDBResponse;
import com.ezgroceries.shoppinglist.cocktails.CocktailResource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cocktails", produces = "application/json")
public class CocktailController {


    private CocktailDBClient cocktailDBClient;

    @Autowired
    public void setCocktailDBClient(CocktailDBClient cocktailDBClient) {
        this.cocktailDBClient = cocktailDBClient;
    }

    @GetMapping
    public ResponseEntity<List<CocktailResource>> get(@RequestParam String search) {

        CocktailDBResponse cocktailDBResponse = cocktailDBClient.searchCocktails(search);
        return ResponseEntity.ok(convert(cocktailDBResponse));
    }

    private List<CocktailResource> convert(CocktailDBResponse dbResponse) {
        return dbResponse.getDrinks().stream()
                .map(drinkResource -> new CocktailResource(
                                UUID.randomUUID(),
                                drinkResource.getStrDrink(),
                                drinkResource.getStrGlass(),
                                drinkResource.getStrInstructions(),
                                drinkResource.getStrDrinkThumb(),
                                Stream.of(
                                        drinkResource.getStrIngredient1(),
                                        drinkResource.getStrIngredient2(),
                                        drinkResource.getStrIngredient3(),
                                        drinkResource.getStrIngredient4(),
                                        drinkResource.getStrIngredient5()
                                ).filter(StringUtils::isNotBlank).collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());

    }

}