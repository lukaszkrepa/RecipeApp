package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.repositories.RecipeRepository;
import com.recipeapp.planner.repositories.UserRepository;
import com.recipeapp.planner.services.impl.RecipeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@DisplayName("<= IngredientServiceTests =>")
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @Test
    @DisplayName("create recipe with correct data")
    @Order(1)
    public void createRecipeWithCorrectData(){

        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(),"test"))
                .instructions(List.of("1.","2.","3."))
                .build();

        when(recipeRepository.save(recipe)).thenReturn(recipe);

        RecipeEntity savedRecipe = recipeService.createRecipe(recipe);
        assertNotNull(savedRecipe);
        assertAll(
                () -> assertNotNull(savedRecipe.getRecipeId()),
                () -> assertEquals(recipe.getCreatedBy().getUserId(), savedRecipe.getCreatedBy().getUserId()),
                () -> assertEquals(recipe.getInstructions().size(), savedRecipe.getInstructions().size()),
                () -> assertEquals(recipe.getInstructions().get(0), savedRecipe.getInstructions().get(0)),
                () -> assertEquals(recipe.getInstructions().get(1), savedRecipe.getInstructions().get(1)),
                () -> assertEquals(recipe.getInstructions().get(2), savedRecipe.getInstructions().get(2)),
                () -> assertEquals(recipe.getDescription(), savedRecipe.getDescription())
        );
    }

    @Test
    @DisplayName("create recipe with empty name")
    @Order(2)
    public void createRecipeWithEmptyName(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    @DisplayName("create recipe with null name")
    @Order(3)
    public void createRecipeWithNullName(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name(null)
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    @DisplayName("create recipe with empty description")
    @Order(4)
    public void createRecipeWithEmptyDescription(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    @DisplayName("create recipe with null description")
    @Order(5)
    public void createRecipeWithNullDescription(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description(null)
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    @DisplayName("create recipe without instructions")
    @Order(6)
    public void createRecipeWithoutInstructions(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of())
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }

    @Test
    @DisplayName("create recipe with null instructions")
    @Order(7)
    public void createRecipeWithNullInstructions(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(null)
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.createRecipe(recipe));
    }


    @Test
    @DisplayName("get recipe by id")
    @Order(8)
    public void getRecipeById(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findById(recipe.getRecipeId())).thenReturn(java.util.Optional.of(recipe));

        RecipeEntity savedRecipe = recipeService.getRecipeById(recipe.getRecipeId());
        assertNotNull(savedRecipe);
        assertAll(
                () -> assertNotNull(savedRecipe.getRecipeId()),
                () -> assertEquals(recipe.getCreatedBy().getUserId(), savedRecipe.getCreatedBy().getUserId()),
                () -> assertEquals(recipe.getInstructions().size(), savedRecipe.getInstructions().size()),
                () -> assertEquals(recipe.getInstructions().get(0), savedRecipe.getInstructions().get(0)),
                () -> assertEquals(recipe.getInstructions().get(1), savedRecipe.getInstructions().get(1)),
                () -> assertEquals(recipe.getInstructions().get(2), savedRecipe.getInstructions().get(2)),
                () -> assertEquals(recipe.getDescription(), savedRecipe.getDescription())
        );
    }


    @Test
    @DisplayName("get recipe by id with non-existing id")
    @Order(9)
    public void getRecipeByIdWithNonExistentId(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findById(recipe.getRecipeId())).thenReturn(java.util.Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById(recipe.getRecipeId()));
    }

    @Test
    @DisplayName("get recipe by id with null id")
    @Order(10)
    public void getRecipeByIdWithNullId(){
        assertThrows(InvalidRecipeInputException.class, () -> recipeService.getRecipeById(null));
    }

    @Test
    @DisplayName("get all recipes")
    @Order(11)
    public void getAllRecipes(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeEntity> savedRecipes = recipeService.getAllRecipes();
        assertNotNull(savedRecipes);
        assertEquals(1, savedRecipes.size());
        assertAll(
                () -> assertNotNull(savedRecipes.get(0).getRecipeId()),
                () -> assertEquals(recipe.getCreatedBy().getUserId(), savedRecipes.get(0).getCreatedBy().getUserId()),
                () -> assertEquals(recipe.getInstructions().size(), savedRecipes.get(0).getInstructions().size()),
                () -> assertEquals(recipe.getInstructions().get(0), savedRecipes.get(0).getInstructions().get(0)),
                () -> assertEquals(recipe.getInstructions().get(1), savedRecipes.get(0).getInstructions().get(1)),
                () -> assertEquals(recipe.getInstructions().get(2), savedRecipes.get(0).getInstructions().get(2)),
                () -> assertEquals(recipe.getDescription(), savedRecipes.get(0).getDescription())
        );
    }

    @Test
    @DisplayName("get all recipes with no recipes")
    @Order(12)
    public void getAllRecipesWithNoRecipes(){
        when(recipeRepository.findAll()).thenReturn(List.of());

        List<RecipeEntity> savedRecipes = recipeService.getAllRecipes();
        assertNotNull(savedRecipes);
        assertEquals(0, savedRecipes.size());
    }

    @Test
    @DisplayName("get all recipes with two recipes")
    @Order(13)
    public void getAllRecipesWithTwoRecipes(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        RecipeEntity recipe2 = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe 2")
                .description("Test Description 2")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findAll()).thenReturn(List.of(recipe, recipe2));

        List<RecipeEntity> savedRecipes = recipeService.getAllRecipes();
        assertNotNull(savedRecipes);
        assertEquals(2, savedRecipes.size());
        assertAll(
                () -> assertNotNull(savedRecipes.get(0).getRecipeId()),
                () -> assertEquals(recipe.getCreatedBy().getUserId(), savedRecipes.get(0).getCreatedBy().getUserId()),
                () -> assertEquals(recipe.getInstructions().size(), savedRecipes.get(0).getInstructions().size()),
                () -> assertEquals(recipe.getInstructions().get(0), savedRecipes.get(0).getInstructions().get(0)),
                () -> assertEquals(recipe.getInstructions().get(1), savedRecipes.get(0).getInstructions().get(1)),
                () -> assertEquals(recipe.getInstructions().get(2), savedRecipes.get(0).getInstructions().get(2)),
                () -> assertEquals(recipe.getDescription(), savedRecipes.get(0).getDescription()),

                () -> assertNotNull(savedRecipes.get(1).getRecipeId()),
                () -> assertEquals(recipe2.getCreatedBy().getUserId(), savedRecipes.get(1).getCreatedBy().getUserId()),
                () -> assertEquals(recipe2.getInstructions().size(), savedRecipes.get(1).getInstructions().size()),
                () -> assertEquals(recipe2.getInstructions().get(0), savedRecipes.get(1).getInstructions().get(0)),
                () -> assertEquals(recipe2.getInstructions().get(1), savedRecipes.get(1).getInstructions().get(1)),
                () -> assertEquals(recipe2.getInstructions().get(2), savedRecipes.get(1).getInstructions().get(2)),
                () -> assertEquals(recipe2.getDescription(), savedRecipes.get(1).getDescription())
        );
    }

    @Test
    @DisplayName("get all recipes by UserId")
    @Order(14)
    public void getAllRecipesByUserId(){
        UUID userId = UUID.randomUUID();
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(userId, "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(recipeRepository.findAllByCreatedBy_UserId(recipe.getCreatedBy().getUserId())).thenReturn(List.of(recipe));

        List<RecipeEntity> savedRecipes = recipeService.getAllRecipesByUserId(recipe.getCreatedBy().getUserId());
        assertNotNull(savedRecipes);
        assertEquals(1, savedRecipes.size());
        assertAll(
                () -> assertNotNull(savedRecipes.get(0).getRecipeId()),
                () -> assertEquals(recipe.getCreatedBy().getUserId(), savedRecipes.get(0).getCreatedBy().getUserId()),
                () -> assertEquals(recipe.getInstructions().size(), savedRecipes.get(0).getInstructions().size()),
                () -> assertEquals(recipe.getInstructions().get(0), savedRecipes.get(0).getInstructions().get(0)),
                () -> assertEquals(recipe.getInstructions().get(1), savedRecipes.get(0).getInstructions().get(1)),
                () -> assertEquals(recipe.getInstructions().get(2), savedRecipes.get(0).getInstructions().get(2)),
                () -> assertEquals(recipe.getDescription(), savedRecipes.get(0).getDescription())
        );
    }

    @Test
    @DisplayName("get all recipes by UserId with no recipes")
    @Order(15)
    public void getAllRecipesByUserIdWithNoRecipes(){
        UUID uuid = UUID.randomUUID();
        when(userRepository.existsById(uuid)).thenReturn(true);
        when(recipeRepository.findAllByCreatedBy_UserId(uuid)).thenReturn(List.of());


        List<RecipeEntity> savedRecipes = recipeService.getAllRecipesByUserId(uuid);
        assertNotNull(savedRecipes);
        assertEquals(0, savedRecipes.size());
    }

    @Test
    @DisplayName("get all recipes by non-existing user")
    @Order(16)
    public void getAllRecipesByNonExistingUser(){
        UUID userId = UUID.randomUUID();
        assertThrows(UserNotFoundException.class, () -> recipeService.getAllRecipesByUserId(userId));
    }

    @Test
    @DisplayName("update recipe")
    @Order(17)
    public void updateRecipe(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        RecipeEntity update = RecipeEntity.builder()
                .recipeId(recipe.getRecipeId())
                .name("Updated Recipe")
                .description("Updated Description")
                .createdBy(recipe.getCreatedBy())
                .instructions(List.of("1.", "2.", "3.", "4."))
                .build();

        when(recipeRepository.findById(update.getRecipeId())).thenReturn(java.util.Optional.of(recipe));
        when(recipeRepository.save(update)).thenReturn(update);


        RecipeEntity savedRecipe = recipeService.updateRecipe(update.getRecipeId(), update);
        assertNotNull(savedRecipe);
        assertAll(
                () -> assertNotNull(savedRecipe.getRecipeId()),
                () -> assertEquals(update.getCreatedBy().getUserId(), savedRecipe.getCreatedBy().getUserId()),
                () -> assertEquals(update.getInstructions().size(), savedRecipe.getInstructions().size()),
                () -> assertEquals(update.getInstructions().get(0), savedRecipe.getInstructions().get(0)),
                () -> assertEquals(update.getInstructions().get(1), savedRecipe.getInstructions().get(1)),
                () -> assertEquals(update.getInstructions().get(2), savedRecipe.getInstructions().get(2)),
                () -> assertEquals(update.getInstructions().get(3), savedRecipe.getInstructions().get(3)),
                () -> assertEquals(update.getDescription(), savedRecipe.getDescription())
        );
    }
    @Test
    @DisplayName("update recipe with non-existing id")
    @Order(18)
    public void updateRecipeWithNonExistingId(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findById(recipe.getRecipeId())).thenReturn(java.util.Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe(recipe.getRecipeId(), recipe));
    }

    @Test
    @DisplayName("update recipe with null id")
    @Order(19)
    public void updateRecipeWithNullId(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.updateRecipe(null, recipe));
    }

    @Test
    @DisplayName("update recipe with null recipe")
    @Order(20)
    public void updateRecipeWithNullRecipe(){
        assertThrows(InvalidRecipeInputException.class, () -> recipeService.updateRecipe(UUID.randomUUID(), null));
    }

    @Test
    @DisplayName("update recipe with empty data")
    @Order(21)
    public void updateRecipeWithEmptyData(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("")
                .description("")
                .createdBy(new UserEntity(UUID.randomUUID(), "Test"))
                .instructions(List.of())
                .build();

        assertThrows(InvalidRecipeInputException.class, () -> recipeService.updateRecipe(recipe.getRecipeId(), recipe));
    }


    @Test
    @DisplayName("delete recipe")
    @Order(22)
    public void deleteRecipe(){
        RecipeEntity recipe = RecipeEntity.builder()
                .recipeId(UUID.randomUUID())
                .name("Test Recipe")
                .description("Test Description")
                .createdBy(new UserEntity(UUID.randomUUID(), "test"))
                .instructions(List.of("1.", "2.", "3."))
                .build();

        when(recipeRepository.findById(recipe.getRecipeId())).thenReturn(java.util.Optional.of(recipe));

        recipeService.deleteRecipe(recipe.getRecipeId());

        verify(recipeRepository, times(1)).delete(recipe);
    }

    @Test
    @DisplayName("delete recipe with non-existing id")
    @Order(23)
    public void deleteRecipeWithNonExistingId(){
        UUID uuid = UUID.randomUUID();
        when(recipeRepository.findById(uuid)).thenReturn(java.util.Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe(uuid));
    }

    @Test
    @DisplayName("delete recipe with null id")
    @Order(24)
    public void deleteRecipeWithNullId(){
        assertThrows(InvalidRecipeInputException.class, () -> recipeService.deleteRecipe(null));
    }

}
