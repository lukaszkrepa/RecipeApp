package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.UserEntity;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DisplayName("<= RecipeRepositoryTests =>")
@ExtendWith({UserParameterResolver.class, RecipeParameterResolver.class})
class RecipeRepositoryTests {

    private UserEntity user1;

    private RecipeEntity recipe1;
    private RecipeEntity recipe2;
    private RecipeEntity publicRecipe;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp(Map<String, UserEntity> users, Map<String, RecipeEntity> recipes) {
        user1 = userRepository.save(users.get("user1"));

        // Assign correct users to recipes (resolver uses detached UserEntity)
        recipe1 = recipes.get("recipe1");
        recipe1.setCreatedBy(user1);

        recipe2 = recipes.get("recipe2");
        recipe2.setCreatedBy(user1);

        publicRecipe = recipes.get("publicRecipe");
        publicRecipe.setCreatedBy(null);
    }

    @Test
    @Order(1)
    @DisplayName("Test that a recipe can be saved")
    void saveRecipeTest() {
        RecipeEntity saved = recipeRepository.save(recipe1);

        assertAll(
                () -> assertNotNull(saved.getRecipeId(), "Recipe ID should not be null"),
                () -> assertEquals(recipe1.getName(), saved.getName(), "Recipe name mismatch"),
                () -> assertEquals(user1.getUserId(), saved.getCreatedBy().getUserId(), "Recipe owner mismatch")
        );
    }

    @Test
    @Order(2)
    @DisplayName("Test that recipes can be found by userId")
    void findRecipesByUserIdTest() {
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
        recipeRepository.save(publicRecipe); // belongs to no one

        List<RecipeEntity> user1Recipes = recipeRepository.findAllByCreatedBy_UserId(user1.getUserId());

        assertEquals(2, user1Recipes.size(), "Expected 2 recipes for user1");
        assertTrue(user1Recipes.stream().allMatch(r -> r.getCreatedBy().getUserId().equals(user1.getUserId())));
    }

    @Test
    @Order(3)
    @DisplayName("Test that a recipe can be found by its ID")
    void findRecipeByIdTest() {
        RecipeEntity saved = recipeRepository.save(recipe1);
        RecipeEntity found = recipeRepository.findById(saved.getRecipeId()).orElse(null);

        assertNotNull(found, "Recipe not found");
        assertEquals(saved.getName(), found.getName());
    }

    @Test
    @Order(4)
    @DisplayName("Test that a recipe can be updated")
    void updateRecipeTest() {
        RecipeEntity saved = recipeRepository.save(recipe1);
        saved.setName("Updated Title");
        saved.setDescription("Updated Description");

        RecipeEntity updated = recipeRepository.save(saved);

        assertAll(
                () -> assertEquals("Updated Title", updated.getName()),
                () -> assertEquals("Updated Description", updated.getDescription())
        );
    }

    @Test
    @Order(5)
    @DisplayName("Test that a recipe can be deleted")
    void deleteRecipeTest() {
        RecipeEntity saved = recipeRepository.save(recipe1);
        recipeRepository.delete(saved);

        boolean exists = recipeRepository.existsById(saved.getRecipeId());
        assertFalse(exists, "Recipe was not deleted");
    }
}
