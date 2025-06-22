package com.recipeapp.planner.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.domain.dto.IngredientRequestDto;
import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import com.recipeapp.planner.errors.ingredient.IngredientNotFoundException;
import com.recipeapp.planner.services.IngredientService;
import com.recipeapp.planner.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IngredientController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class IngredientControllerTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientService ingredientService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public IngredientService ingredientService() {
            return mock(IngredientService.class);
        }
    }

    @Test
    @DisplayName("get all ingredients with no ingredients")
    @Order(1)
    void getAllIngredients() throws Exception {
        when(ingredientService.getAllIngredients()).thenReturn(List.of());
        mockMvc.perform(get("/ingredients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all ingredients with one ingredient")
    @Order(2)
    void getAllIngredientsWithOneIngredient() throws Exception{
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();
        when(ingredientService.getAllIngredients()).thenReturn(List.of(ingredient));
        mockMvc.perform(get("/ingredients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("Tomato"))
                .andExpect(jsonPath("$.data[0].unit").value("PCS"))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all ingredients with two ingredients")
    @Order(3)
    void getAllIngredientsWithTwoIngredients() throws Exception{
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity ingredient2 = IngredientEntity
                .builder()
                .name("Potato")
                .unit(Unit.CUP)
                .build();

        when(ingredientService.getAllIngredients()).thenReturn(List.of(
                ingredient,
                ingredient2
        ));

        mockMvc.perform(get("/ingredients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Tomato"))
                .andExpect(jsonPath("$.data[0].unit").value("PCS"))
                .andExpect(jsonPath("$.data[0].name").value("Potato"))
                .andExpect(jsonPath("$.data[0].unit").value("CUP"))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get ingredient by id")
    @Order(4)
    void getIngredientById() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .ingredientId(uuid)
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        when(ingredientService.getIngredientById(uuid)).thenReturn(ingredient);

        mockMvc.perform(get("/ingredients/" + uuid))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.name").value("Tomato"))
                .andExpect(jsonPath("$.data.unit").value("PCS"))
                .andExpect(jsonPath("$.data.ingredientId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get ingredient by id not found")
    @Order(5)
    void getIngredientByIdNotFound() throws Exception{
        UUID uuid = UUID.randomUUID();

        when(ingredientService.getIngredientById(uuid))
                .thenThrow(new IngredientNotFoundException("Ingredient with id: " + uuid + " does not exist"));

        mockMvc.perform(get("/ingredients/" + uuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient not found"));
    }

    @Test
    @DisplayName("get ingredient by name")
    @Order(6)
    void getIngredientByName() throws Exception{
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        when(ingredientService.getIngredientsByName("Tomato")).thenReturn(List.of(ingredient));

        mockMvc.perform(get("/ingredients/name/Tomato"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("Tomato"))
                .andExpect(jsonPath("$.data[0].unit").value("PCS"))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }
    @Test
    @DisplayName("get ingredients by name with 2 ingredients")
    @Order(7)
    void getIngredientsByNameWithTwoIngredients() throws Exception{
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity ingredient2 = IngredientEntity
                .builder()
                .name("Tomato")
                .unit(Unit.CUP)
                .build();

        when(ingredientService.getIngredientsByName("Tomato")).thenReturn(List.of(
                ingredient,
                ingredient2
        ));

        mockMvc.perform(get("/ingredients/name/Tomato"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("Tomato"))
                .andExpect(jsonPath("$.data[0].unit").value("PCS"))
                .andExpect(jsonPath("$.data[0].name").value("Tomato"))
                .andExpect(jsonPath("$.data[0].unit").value("CUP"))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get ingredient by name not found")
    @Order(8)
    void getIngredientByNameNotFound() throws Exception{
        when(ingredientService.getIngredientsByName("Tomato"))
                .thenThrow(new IngredientNotFoundException("Ingredient with name: Tomato does not exist"));

        mockMvc.perform(get("/ingredients/name/Tomato"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient not found"));
    }

    @Test
    @DisplayName("create ingredient")
    @Order(9)
    void createIngredient() throws Exception{
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", Unit.PCS);

        when(ingredientService.createIngredient("Tomato",Unit.PCS))
                .thenReturn(IngredientEntity
                        .builder()
                        .ingredientId(UUID.randomUUID())
                        .name("Tomato")
                        .unit(Unit.PCS)
                        .build());

        mockMvc.perform(post("/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.name").value("Tomato"))
                .andExpect(jsonPath("$.data.unit").value("PCS"))
                .andExpect(jsonPath("$.data.ingredientId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("create ingredient with empty name")
    @Order(10)
    void createIngredientWithEmptyName() throws Exception{
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("", Unit.PCS);

        mockMvc.perform(post("/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient name cannot be empty"));
    }

    @Test
    @DisplayName("create ingredient with empty unit")
    @Order(11)
    void createIngredientWithEmptyUnit() throws Exception{
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", null);

        mockMvc.perform(post("/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient unit cannot be empty"));
    }

    @Test
    @DisplayName("create ingredient with null name")
    @Order(12)
    void createIngredientWithNullName() throws Exception{
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto(null, Unit.PCS);

        mockMvc.perform(post("/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient name cannot be empty"));
    }

    @Test
    @DisplayName("create ingredient with null unit")
    @Order(13)
    void createIngredientWithNullUnit() throws Exception{
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", null);

        mockMvc.perform(post("/ingredients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient unit cannot be empty"));
    }

    @Test
    @DisplayName("update ingredient")
    @Order(14)
    void updateIngredient() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", Unit.PCS);

        when(ingredientService.updateIngredient(uuid, "Tomato", Unit.PCS))
                .thenReturn(IngredientEntity
                        .builder()
                        .ingredientId(uuid)
                        .name("Tomato")
                        .unit(Unit.PCS)
                        .build());

        mockMvc.perform(patch("/ingredients/" + uuid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.name").value("Tomato"))
                .andExpect(jsonPath("$.data.unit").value("PCS"))
                .andExpect(jsonPath("$.data.ingredientId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("update ingredient with empty name")
    @Order(15)
    void updateIngredientWithEmptyName() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("", Unit.PCS);

        mockMvc.perform(patch("/ingredients/" + uuid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient name cannot be empty"));
    }

    @Test
    @DisplayName("update ingredient with empty unit")
    @Order(16)
    void updateIngredientWithEmptyUnit() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", null);

        mockMvc.perform(patch("/ingredients/" + uuid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value("Ingredient unit cannot be empty"));
    }

    @Test
    @DisplayName("update ingredient with null name")
    @Order(17)
    void updateIngredientWithNullName() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto(null, Unit.PCS);

        when(ingredientService.updateIngredient(uuid, null, Unit.PCS))
                .thenReturn(IngredientEntity
                        .builder()
                        .ingredientId(uuid)
                        .name(null)
                        .unit(Unit.PCS)
                        .build());

        mockMvc.perform(patch("/ingredients/" + uuid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.name").doesNotExist())
                .andExpect(jsonPath("$.data.unit").value("PCS"))
                .andExpect(jsonPath("$.data.ingredientId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("update ingredient with null unit")
    @Order(18)
    void updateIngredientWithNullUnit() throws Exception{
        UUID uuid = UUID.randomUUID();
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomato", null);

        when(ingredientService.updateIngredient(uuid, "Tomato", null))
                .thenReturn(IngredientEntity
                        .builder()
                        .ingredientId(uuid)
                        .name("Tomato")
                        .unit(null)
                        .build());

        mockMvc.perform(patch("/ingredients/" + uuid)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data.name").value("Tomato"))
                .andExpect(jsonPath("$.data.unit").doesNotExist())
                .andExpect(jsonPath("$.data.ingredientId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("delete ingredient")
    @Order(19)
    void deleteIngredient() throws Exception{
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/ingredients/" + uuid))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("delete ingredient with invalid id")
    @Order(20)
    void deleteIngredientWithInvalidId() throws Exception{
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/ingredients/" + uuid))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }





}
