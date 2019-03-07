package com.ezgroceries.shoppinglist.lists.entities;

import com.ezgroceries.shoppinglist.cocktails.entities.CocktailEntity;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SHOPPING_LIST")
public class ShoppingListEntity {

    @Id
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable( name="COCKTAIL_SHOPPING_LIST",
            joinColumns = @JoinColumn(name="SHOPPING_LIST_ID"),
            inverseJoinColumns = @JoinColumn(name = "COCKTAIL_ID"))
    private List<CocktailEntity> cocktails;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CocktailEntity> getCocktails() {
        return cocktails;
    }

    public void setCocktails(List<CocktailEntity> cocktails){
        this.cocktails = cocktails;
    }

    public void addCocktails(CocktailEntity... cocktails) {
        this.cocktails.addAll(Arrays.asList(cocktails));
    }

    public void addCocktails(List<CocktailEntity> cocktails) {
        this.cocktails.addAll(cocktails);
    }
}
