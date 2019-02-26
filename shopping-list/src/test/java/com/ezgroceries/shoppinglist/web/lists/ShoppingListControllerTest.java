package com.ezgroceries.shoppinglist.web.lists;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void createShoppingList() throws Exception {
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
    }

    @Test
    public void addCocktailsToList() throws Exception {
        this.mockMvc
                .perform(
                        post("/shopping-lists/97c8e5bd-5353-426e-b57b-69eb2260ace3/cocktails")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("[{\"cocktailId\": \"23b3d85a-3928-41c0-a533-6538a71e17c4\"}, {\"cocktailId\": \"d615ec78-fe93-467b-8d26-5d26d8eab073\"}]")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].cocktailId").value("23b3d85a-3928-41c0-a533-6538a71e17c4"))
        ;
    }

}