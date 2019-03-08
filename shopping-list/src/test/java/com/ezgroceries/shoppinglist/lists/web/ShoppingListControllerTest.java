package com.ezgroceries.shoppinglist.lists.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ezgroceries.shoppinglist.lists.ShoppingListResource;
import com.ezgroceries.shoppinglist.lists.service.ShoppingListService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ComponentScan("com.ezgroceries.shoppinglist.web")
public class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListService shoppingListService;

    @Test
    public void createShoppingList() throws Exception {
        given(shoppingListService.create(any(ShoppingListResource.class))).willReturn(getShoppingListResource());
        this.mockMvc
                .perform(
                        post("/shopping-lists")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"Stephanie's birthday\"}")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("shoppingListId").value("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"))
                .andExpect(jsonPath("name").value("Stephanie's birthday"))
        ;
        ShoppingListResource expected = new ShoppingListResource();
        expected.setName("Stephanie's birthday");
        verify(shoppingListService).create(eq(expected));
    }

    @Test
    public void addCocktailsToList() throws Exception {
        given(shoppingListService.get(any())).willReturn(getShoppingListResource());
        given(shoppingListService.addCocktails(any(ShoppingListResource.class), any(List.class))).willReturn(getShoppingListResource());


        this.mockMvc
                .perform(
                        post("/shopping-lists/97c8e5bd-5353-426e-b57b-69eb2260ace3/cocktails")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("[{\"cocktailId\": \"23b3d85a-3928-41c0-a533-6538a71e17c4\"}, {\"cocktailId\": \"d615ec78-fe93-467b-8d26-5d26d8eab073\"}]")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].cocktailId").value("23b3d85a-3928-41c0-a533-6538a71e17c4"))
                .andExpect(jsonPath("[1].cocktailId").value("d615ec78-fe93-467b-8d26-5d26d8eab073"))
        ;
        verify(shoppingListService).get("97c8e5bd-5353-426e-b57b-69eb2260ace3");
        verify(shoppingListService).addCocktails(any(ShoppingListResource.class), eq(Arrays.asList("23b3d85a-3928-41c0-a533-6538a71e17c4", "d615ec78-fe93-467b-8d26-5d26d8eab073")));

    }

    @Test
    public void getShoppingList() throws Exception {
        given(shoppingListService.getWithDetails(any(String.class))).willReturn(getShoppingListResourceWithIngredients());
        this.mockMvc
                .perform(
                        get("/shopping-lists/eb18bb7c-61f3-4c9f-981c-55b1b8ee8915")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.shoppingListId").value("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"))
                .andExpect(jsonPath("$.name").value("Stephanie's birthday"))
                .andExpect(jsonPath("$.ingredients.length()").value(5))
                .andExpect(jsonPath("$.ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$.ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$.ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$.ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$.ingredients[4]").value("Blue Curacao"))
        ;
        verify(shoppingListService).getWithDetails("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
    }


    @Test
    public void getShoppingLists() throws Exception {
        given(shoppingListService.getAllWithDetails()).willReturn(getShoppingListResources());

        this.mockMvc
                .perform(
                        get("/shopping-lists")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].shoppingListId").value("4ba92a46-1d1b-4e52-8e38-13cd56c7224c"))
                .andExpect(jsonPath("$[0].name").value("Stephanie's birthday"))
                .andExpect(jsonPath("$[0].ingredients.length()").value(5))
                .andExpect(jsonPath("$[0].ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$[0].ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$[0].ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$[0].ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$[0].ingredients[4]").value("Blue Curacao"))
                .andExpect(jsonPath("$[1].shoppingListId").value("6c7d09c2-8a25-4d54-a979-25ae779d2465"))
                .andExpect(jsonPath("$[1].name").value("My birthday"))
                .andExpect(jsonPath("$[1].ingredients.length()").value(5))
                .andExpect(jsonPath("$[1].ingredients[0]").value("Tequila"))
                .andExpect(jsonPath("$[1].ingredients[1]").value("Triple sec"))
                .andExpect(jsonPath("$[1].ingredients[2]").value("Lime juice"))
                .andExpect(jsonPath("$[1].ingredients[3]").value("Salt"))
                .andExpect(jsonPath("$[1].ingredients[4]").value("Blue Curacao"))
        ;
        verify(shoppingListService).getAllWithDetails();
    }

    private ShoppingListResource getShoppingListResource() {
        ShoppingListResource resource = new ShoppingListResource();
        resource.setName("Stephanie's birthday");
        resource.setShoppingListId(UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915"));
        return resource;
    }

    private List<ShoppingListResource> getShoppingListResources() {
        List<ShoppingListResource> lists = new ArrayList<>();
        {
            UUID uuid = UUID.fromString("4ba92a46-1d1b-4e52-8e38-13cd56c7224c");
            ShoppingListResource shoppingList = new ShoppingListResource();
            shoppingList.setShoppingListId(uuid);
            shoppingList.setName("Stephanie's birthday");
            Arrays.asList("Tequila",
                    "Triple sec",
                    "Lime juice",
                    "Salt",
                    "Blue Curacao").forEach(shoppingList::addIngredient);
            lists.add(shoppingList);
        }
        {
            UUID uuid = UUID.fromString("6c7d09c2-8a25-4d54-a979-25ae779d2465");
            ShoppingListResource shoppingList = new ShoppingListResource();
            shoppingList.setShoppingListId(uuid);
            shoppingList.setName("My birthday");
            Arrays.asList("Tequila",
                    "Triple sec",
                    "Lime juice",
                    "Salt",
                    "Blue Curacao").forEach(shoppingList::addIngredient);
            lists.add(shoppingList);
        }
        return lists;
    }


    private ShoppingListResource getShoppingListResourceWithIngredients() {
        UUID uuid = UUID.fromString("eb18bb7c-61f3-4c9f-981c-55b1b8ee8915");
        ShoppingListResource shoppingList = new ShoppingListResource();
        shoppingList.setShoppingListId(uuid);
        shoppingList.setName("Stephanie's birthday");
        Arrays.asList("Tequila",
                "Triple sec",
                "Lime juice",
                "Salt",
                "Blue Curacao").forEach(shoppingList::addIngredient);
        return shoppingList;
    }

}