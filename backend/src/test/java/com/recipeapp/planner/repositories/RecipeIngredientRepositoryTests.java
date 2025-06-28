package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.*;
import com.recipeapp.planner.resolvers.IngredientParameterResolver;
import com.recipeapp.planner.resolvers.RecipeParameterResolver;
import com.recipeapp.planner.resolvers.UserParameterResolver;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DisplayName("<= RecipeIngredientRepositoryTests =>")
@ExtendWith({UserParameterResolver.class, RecipeParameterResolver.class, IngredientParameterResolver.class})
class RecipeIngredientRepositoryTests {

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository userRepository;

    private RecipeEntity recipe;
    private IngredientEntity salt;
    private IngredientEntity pepper;

    @BeforeEach
    void setUp(Map<String, UserEntity> users,
               Map<String, RecipeEntity> recipes,
               Map<String, IngredientEntity> ingredients) {

        UserEntity user = userRepository.save(users.get("user1"));

        recipe = recipes.get("recipe1");
        recipe.setCreatedBy(user);
        recipe = recipeRepository.save(recipe);

        salt = ingredientRepository.save(ingredients.get("salt"));
        pepper = ingredientRepository.save(ingredients.get("pepper"));
    }

    @AfterEach
    void tearDown() {
        recipeIngredientRepository.deleteAll();
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Save and retrieve a RecipeIngredientEntity")
    void saveAndRetrieveTest() {
        RecipeIngredientEntity entity = RecipeIngredientEntity.builder()
                .ingredient(salt)
                .recipe(recipe)
                .amount(5.0f)
                .build();

        RecipeIngredientEntity saved = recipeIngredientRepository.save(entity);

        assertAll(
                () -> assertNotNull(saved.getRecipeIngredientId()),
                () -> assertEquals(recipe.getRecipeId(), saved.getRecipe().getRecipeId()),
                () -> assertEquals(salt.getIngredientId(), saved.getIngredient().getIngredientId()),
                () -> assertEquals(5.0f, saved.getAmount())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Delete RecipeIngredientEntity and check non-existence")
    void deleteTest() {
        RecipeIngredientEntity saved = recipeIngredientRepository.save(
                RecipeIngredientEntity.builder()
                        .recipe(recipe)
                        .ingredient(salt)
                        .amount(3.5f)
                        .build()
        );

        UUID id = saved.getRecipeIngredientId();
        recipeIngredientRepository.delete(saved);

        assertFalse(recipeIngredientRepository.existsById(id));
    }

    @Test
    @Order(3)
    @DisplayName("Save multiple ingredients for one recipe")
    void saveMultipleIngredientsForRecipe() {
        RecipeIngredientEntity saltEntity = recipeIngredientRepository.save(
                RecipeIngredientEntity.builder()
                        .recipe(recipe)
                        .ingredient(salt)
                        .amount(2.0f)
                        .build());

        RecipeIngredientEntity pepperEntity = recipeIngredientRepository.save(
                RecipeIngredientEntity.builder()
                        .recipe(recipe)
                        .ingredient(pepper)
                        .amount(1.0f)
                        .build());

        List<RecipeIngredientEntity> result = recipeIngredientRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @Order(4)
    @DisplayName("Cascade delete: Recipe deletes associated RecipeIngredientEntity")
    void cascadeDeleteFromRecipeTest() {
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        IngredientEntity savedSalt = ingredientRepository.save(salt);

        RecipeIngredientEntity link = RecipeIngredientEntity.builder()
                .ingredient(savedSalt)
                .amount(2.0f)
                .build();

        savedRecipe.addIngredientLink(link);

        recipeRepository.save(savedRecipe);

        recipeRepository.delete(savedRecipe);

        assertTrue(recipeIngredientRepository.findAll().isEmpty());
    }



    @Test
    @Order(5)
    @DisplayName("Cascade delete: Ingredient deletes associated RecipeIngredientEntity")
    void cascadeDeleteFromIngredientTest() {
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        IngredientEntity savedPepper = ingredientRepository.save(pepper);

        RecipeIngredientEntity link = RecipeIngredientEntity.builder()
                .recipe(savedRecipe)
                .amount(1.5f)
                .build();

        savedPepper.addRecipeLink(link);

        ingredientRepository.save(savedPepper);

        ingredientRepository.delete(savedPepper);

        assertTrue(recipeIngredientRepository.findAll().isEmpty(),
                "Ingredient usage should be deleted");
    }

    @Test
    @Order(6)
    @DisplayName("Find all ingredients for a recipe by recipe ID")
    void findAllByRecipeIdTest() {
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        IngredientEntity savedSalt = ingredientRepository.save(salt);
        IngredientEntity savedPepper = ingredientRepository.save(pepper);

        RecipeIngredientEntity saltLink = RecipeIngredientEntity.builder()
                .recipe(savedRecipe)
                .ingredient(savedSalt)
                .amount(2.0f)
                .build();

        RecipeIngredientEntity pepperLink = RecipeIngredientEntity.builder()
                .recipe(savedRecipe)
                .ingredient(savedPepper)
                .amount(1.0f)
                .build();

        recipeIngredientRepository.save(saltLink);
        recipeIngredientRepository.save(pepperLink);

        List<RecipeIngredientEntity> foundIngredients = recipeIngredientRepository.findAllByRecipe_RecipeId(savedRecipe.getRecipeId());

        assertAll(
            () -> assertEquals(2, foundIngredients.size()),
            () -> assertTrue(foundIngredients.stream().anyMatch(ri -> ri.getIngredient().equals(savedSalt))),
            () -> assertTrue(foundIngredients.stream().anyMatch(ri -> ri.getIngredient().equals(savedPepper)))
        );
    }

}

