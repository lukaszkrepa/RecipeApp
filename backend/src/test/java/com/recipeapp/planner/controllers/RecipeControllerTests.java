package com.recipeapp.planner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipeapp.planner.domain.dto.RecipeRequestDto;
import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.services.RecipeService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class RecipeControllerTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeService recipeService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RecipeService recipeService() {
            return mock(RecipeService.class);
        }
    }

    @Test
    @DisplayName("get all recipes with no recipes")
    @Order(1)
    public void getAllRecipesWithNoRecipes() throws Exception {

        when(recipeService.getAllRecipes()).thenReturn(List.of());

        mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all recipes with one recipe")
    @Order(2)
    public void getAllRecipesWithOneRecipe() throws Exception {

        RecipeEntity recipe = RecipeEntity
                .builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.","2.","3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build();

        when(recipeService.getAllRecipes()).thenReturn(List.of(recipe));

        mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].recipeId").exists())
                .andExpect(jsonPath("$.data[0].name").value("Test"))
                .andExpect(jsonPath("$.data[0].description").value("test"))
                .andExpect(jsonPath("$.data[0].instructions").isArray())
                .andExpect(jsonPath("$.data[0].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[0].createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all recipes with two recipes")
    @Order(3)
    public void getAllRecipesWithTwoRecipes() throws Exception {

        RecipeEntity recipe1 = RecipeEntity
                .builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build();

        RecipeEntity recipe2 = RecipeEntity
                .builder()
                .recipeId(UUID.randomUUID())
                .name("Test2")
                .description("test2")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build();

        when(recipeService.getAllRecipes()).thenReturn(List.of(recipe1, recipe2));

        mockMvc.perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].recipeId").exists())
                .andExpect(jsonPath("$.data[0].name").value("Test"))
                .andExpect(jsonPath("$.data[0].description").value("test"))
                .andExpect(jsonPath("$.data[0].instructions").isArray())
                .andExpect(jsonPath("$.data[0].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[0].createdByUserId").exists())
                .andExpect(jsonPath("$.data[1].recipeId").exists())
                .andExpect(jsonPath("$.data[1].name").value("Test2"))
                .andExpect(jsonPath("$.data[1].description").value("test2"))
                .andExpect(jsonPath("$.data[1].instructions").isArray())
                .andExpect(jsonPath("$.data[1].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[1].createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get recipe by id")
    @Order(4)
    public void getRecipeById() throws Exception {

        RecipeEntity recipe = RecipeEntity
                .builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build();

        when(recipeService.getRecipeById(recipe.getRecipeId())).thenReturn(recipe);

        mockMvc.perform(get("/recipes/" + recipe.getRecipeId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.recipeId").exists())
                .andExpect(jsonPath("$.data.name").value("Test"))
                .andExpect(jsonPath("$.data.description").value("test"))
                .andExpect(jsonPath("$.data.instructions").isArray())
                .andExpect(jsonPath("$.data.instructions", hasSize(3)))
                .andExpect(jsonPath("$.data.createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get recipe by id with invalid id")
    @Order(5)
    public void getRecipeByIdWithInvalidId() throws Exception {
        UUID id = UUID.randomUUID();
        when(recipeService.getRecipeById(id))
                .thenThrow(new RecipeNotFoundException("Recipe with id: " + id + "does not exist"));

        mockMvc.perform(get("/recipes/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Recipe not found"))
                .andExpect(jsonPath("$.error.details[0]").value("Recipe with id: " + id + "does not exist"));
    }

    @Test
    @DisplayName("create recipe")
    @Order(6)
    public void createRecipe() throws Exception {
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        RecipeEntity recipe = RecipeEntity
                .builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build();

        when(recipeService.createRecipe(recipeRequestDto)).thenReturn(recipe);

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.recipeId").exists())
                .andExpect(jsonPath("$.data.name").value("Test"))
                .andExpect(jsonPath("$.data.description").value("test"))
                .andExpect(jsonPath("$.data.instructions").isArray())
                .andExpect(jsonPath("$.data.instructions", hasSize(3)))
                .andExpect(jsonPath("$.data.createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("create recipe with empty name")
    @Order(7)
    public void createRecipeWithEmptyName() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "",
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Name is required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Name is required"));
    }

    @Test
    @DisplayName("create recipe with null name")
    @Order(8)
    public void createRecipeWithNullName() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                null,
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Name is required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Name is required"));
    }

    @Test
    @DisplayName("create recipe with empty description")
    @Order(9)
    public void createRecipeWithEmptyDescription() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Description is required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Description is required"));
    }

    @Test
    @DisplayName("create recipe with null description")
    @Order(10)
    public void createRecipeWithNullDescription() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                null,
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Description is required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Description is required"));
    }

    @Test
    @DisplayName("create recipe with empty instructions")
    @Order(11)
    public void createRecipeWithEmptyInstructions() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of(),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Instructions are required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Instructions are required"));
    }

    @Test
    @DisplayName("create recipe with null instructions")
    @Order(12)
    public void createRecipeWithNullInstructions() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                null,
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("Instructions are required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Instructions are required"));
    }

    @Test
    @DisplayName("create recipe with null user id")
    @Order(13)
    public void createRecipeWithEmptyUserId() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of("1.", "2.", "3."),
                null
        );
        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("User id is required"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("User id is required"));
    }

    @Test
    @DisplayName("create recipe with non-existing userId")
    @Order(14)
    public void createRecipeWithNonExistingUserId() throws Exception {

        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.createRecipe(recipeRequestDto))
                .thenThrow(new UserNotFoundException("User with id: " + recipeRequestDto.userId() + " does not exist"));

        mockMvc.perform(post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("User not found"))
                .andExpect(jsonPath("$.error.details[0]").value("User with id: " + recipeRequestDto.userId() + " does not exist"));
    }

    @Test
    @DisplayName("get all recipes by userId")
    @Order(15)
    public void getAllRecipesByUserId() throws Exception {

        UUID userId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(userId).username("test").build())
                .build();

        when(recipeService.getAllRecipesByUserId(userId)).thenReturn(List.of(recipe));

        mockMvc.perform(get("/recipes/user/" + userId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].recipeId").exists())
                .andExpect(jsonPath("$.data[0].name").value("Test"))
                .andExpect(jsonPath("$.data[0].description").value("test"))
                .andExpect(jsonPath("$.data[0].instructions").isArray())
                .andExpect(jsonPath("$.data[0].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[0].createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }
    @Test
    @DisplayName("get all recipes by userId with two Recipes")
    @Order(16)
    public void getAllRecipesByUserIdWithTwoRecipes() throws Exception {

        UUID userId = UUID.randomUUID();
        RecipeEntity recipe1 = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(userId).username("test").build())
                .build();
        RecipeEntity recipe2 = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(userId).username("test").build())
                .build();

        when(recipeService.getAllRecipesByUserId(userId)).thenReturn(List.of(recipe1, recipe2));

        mockMvc.perform(get("/recipes/user/" + userId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].recipeId").exists())
                .andExpect(jsonPath("$.data[0].name").value("Test"))
                .andExpect(jsonPath("$.data[0].description").value("test"))
                .andExpect(jsonPath("$.data[0].instructions").isArray())
                .andExpect(jsonPath("$.data[0].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[0].createdByUserId").exists())
                .andExpect(jsonPath("$.data[1].recipeId").exists())
                .andExpect(jsonPath("$.data[1].name").value("Test"))
                .andExpect(jsonPath("$.data[1].description").value("test"))
                .andExpect(jsonPath("$.data[1].instructions").isArray())
                .andExpect(jsonPath("$.data[1].instructions", hasSize(3)))
                .andExpect(jsonPath("$.data[1].createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all recipes by userId with no Recipes")
    @Order(17)
    public void getAllRecipesByUserIdWithNoRecipes() throws Exception {

        UUID userId = UUID.randomUUID();

        when(recipeService.getAllRecipesByUserId(userId)).thenReturn(List.of());

        mockMvc.perform(get("/recipes/user/" + userId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("get all recipes by userId with null userId")
    @Order(18)
    public void getAllRecipesByUserIdWithNullUserId() throws Exception {

        mockMvc.perform(get("/recipes/user/" + null)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("User id is invalid"));
    }

    @Test
    @DisplayName("get all recipes by userId with non-existing userId")
    @Order(19)
    public void getAllRecipesByUserIdWithNonExistingUserId() throws Exception {

        UUID userId = UUID.randomUUID();

        when(recipeService.getAllRecipesByUserId(userId))
                .thenThrow(new UserNotFoundException("User with id: " + userId + " does not exist"));

        mockMvc.perform(get("/recipes/user/" + userId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("User not found"))
                .andExpect(jsonPath("$.error.details[0]").value("User with id: " + userId + " does not exist"));
    }

    @Test
    @DisplayName("update recipe")
    @Order(20)
    public void updateRecipe() throws Exception {

        UUID recipeId = UUID.randomUUID();
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.updateRecipe(recipeId, recipeRequestDto)).thenReturn(RecipeEntity
                .builder()
                .recipeId(recipeId)
                .name("Test")
                .description("test")
                .instructions(List.of("1.", "2.", "3."))
                .createdBy(UserEntity.builder().userId(UUID.randomUUID()).username("test").build())
                .build());

        mockMvc.perform(patch("/recipes/" + recipeId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.name").value("Test"))
                .andExpect(jsonPath("$.data.description").value("test"))
                .andExpect(jsonPath("$.data.instructions").isArray())
                .andExpect(jsonPath("$.data.instructions", hasSize(3)))
                .andExpect(jsonPath("$.data.createdByUserId").exists())
                .andExpect(jsonPath("$.error").value(nullValue()));
    }

    @Test
    @DisplayName("update recipe with non-existing recipeId")
    @Order(21)
    public void updateRecipeWithNonExistingRecipeId() throws Exception {

        UUID recipeId = UUID.randomUUID();
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "Test",
                "test",
                List.of("1.", "2.", "3."),
                UUID.randomUUID()
        );

        when(recipeService.updateRecipe(recipeId, recipeRequestDto))
                .thenThrow(new RecipeNotFoundException("Recipe with id: " + recipeId + " does not exist"));

        mockMvc.perform(patch("/recipes/" + recipeId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Recipe not found"))
                .andExpect(jsonPath("$.error.details[0]").value("Recipe with id: " + recipeId + " does not exist"));
    }

    @Test
    @DisplayName("update recipe with null recipe")
    @Order(22)
    public void updateRecipeWithNullRecipe() throws Exception {

        UUID recipeId = UUID.randomUUID();

        when(recipeService.updateRecipe(recipeId,null))
                .thenThrow(new InvalidRecipeInputException("Recipe is required"));
        mockMvc.perform(patch("/recipes/" + recipeId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("Recipe is required"));
    }

    @Test
    @DisplayName("update recipe with empty data")
    @Order(23)
    public void updateRecipeWithEmptyData() throws Exception {

        UUID recipeId = UUID.randomUUID();
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(
                "",
                "",
                List.of(),
                null
        );

        when(recipeService.updateRecipe(recipeId, recipeRequestDto))
                .thenThrow(new InvalidRecipeInputException("At least one field must be provided for update"));

        mockMvc.perform(patch("/recipes/" + recipeId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(recipeRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Invalid input"))
                .andExpect(jsonPath("$.error.details[0]").value("At least one field must be provided for update"));
    }

    @Test
    @DisplayName("delete recipe")
    @Order(24)
    public void deleteRecipe() throws Exception {
        UUID recipeId = UUID.randomUUID();

        doNothing().when(recipeService).deleteRecipe(recipeId);

        mockMvc.perform(delete("/recipes/" + recipeId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").value(nullValue()));

    }

    @Test
    @DisplayName("delete recipe with non-existing recipeId")
    @Order(25)
    public void deleteRecipeWithNonExistingRecipeId() throws Exception {

        UUID recipeId = UUID.randomUUID();

        doThrow(new RecipeNotFoundException("Recipe with id: " + recipeId + " does not exist"))
                .when(recipeService).deleteRecipe(recipeId);

        mockMvc.perform(delete("/recipes/" + recipeId)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.data").value(nullValue()))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.message").value("Recipe not found"))
                .andExpect(jsonPath("$.error.details[0]").value("Recipe with id: " + recipeId + " does not exist"));
    }

}

