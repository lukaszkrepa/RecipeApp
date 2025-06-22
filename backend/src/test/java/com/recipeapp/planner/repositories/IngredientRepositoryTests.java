package com.recipeapp.planner.repositories;


import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DisplayName("<= Ingredient Repository Tests =>")
public class IngredientRepositoryTests {

    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach()
    void tearDown() {
        ingredientRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Test that a Ingredient can be saved")
    void saveIngredientTest() {
        IngredientEntity ingredient = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity savedIngredient = ingredientRepository.save(ingredient);

        assertAll(
                () -> assertNotNull(savedIngredient, () -> "Ingredient was not saved"),
                () -> Assertions.assertEquals(ingredient.getName(), savedIngredient.getName(),
                        () -> "Expected name to be " + ingredient.getName() + " but was " + savedIngredient.getName() + " instead"),
                () -> Assertions.assertEquals(ingredient.getUnit(), savedIngredient.getUnit(),
                        () -> "Expected unit to be " + ingredient.getUnit() + " but was " + savedIngredient.getUnit() + " instead")
        );

    }

    @Test
    @Order(2)
    @DisplayName("Test that a Ingredient can be found by id")
    void findIngredientByIdTest() {
        IngredientEntity ingredient = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity savedIngredient = ingredientRepository.save(ingredient);

        IngredientEntity foundIngredient = ingredientRepository.findById(savedIngredient.getIngredientId()).orElse(null);

        assertNotNull(foundIngredient);

        assertAll(
                () -> assertEquals(ingredient.getName(), foundIngredient.getName(),
                        () -> "Expected name to be " + ingredient.getName() + " but was " + foundIngredient.getName() + " instead"),
                () -> assertEquals(ingredient.getUnit(), foundIngredient.getUnit(),
                        () -> "Expected unit to be " + ingredient.getUnit() + " but was " + foundIngredient.getUnit() + " instead")
        );
    }

    @Test
    @Order(3)
    @DisplayName("Test that a Ingredient can be found by name")
    void findIngredientByNameTest() {
        IngredientEntity ingredient = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        ingredientRepository.save(ingredient);

        List<IngredientEntity> foundIngredient = ingredientRepository.findAllByName("Tomato");

        assertAll(
                () -> assertNotNull(foundIngredient),
                () -> assertEquals(1, foundIngredient.size(),
                        () -> "Expected to find 1 Ingredient but found " + foundIngredient.size() + " instead"),
                () -> assertEquals(ingredient.getName(), foundIngredient.get(0).getName(),
                        () -> "Expected name to be " + ingredient.getName() + " but was " + foundIngredient.get(0).getName() + " instead"),
                () -> assertEquals(ingredient.getUnit(), foundIngredient.get(0).getUnit(),
                        () -> "Expected unit to be " + ingredient.getUnit() + " but was " + foundIngredient.get(0).getUnit() + " instead")
        );
    }

    @Test
    @Order(4)
    @DisplayName("Test that a Ingredient can be updated")
    void updateIngredientTest() {
        IngredientEntity ingredient = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity savedIngredient = ingredientRepository.save(ingredient);

        savedIngredient.setName("Potato");
        savedIngredient.setUnit(Unit.GRAMS);

        IngredientEntity updatedIngredient = ingredientRepository.save(savedIngredient);

        assertAll(
                () -> assertNotNull(updatedIngredient),
                () -> assertEquals("Potato", updatedIngredient.getName(),
                        () -> "Expected name to be Potato but was " + updatedIngredient.getName() + " instead"),
                () -> assertEquals(Unit.GRAMS, updatedIngredient.getUnit(),
                        () -> "Expected unit to be GRAMS but was " + updatedIngredient.getUnit() + " instead")
        );
    }

    @Test
    @Order(5)
    @DisplayName("Test that a Ingredient can be deleted")
    void deleteIngredientTest() {
        IngredientEntity ingredient = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity savedIngredient = ingredientRepository.save(ingredient);

        ingredientRepository.delete(savedIngredient);

        IngredientEntity foundIngredient = ingredientRepository.findById(savedIngredient.getIngredientId()).orElse(null);

        assertNull(foundIngredient);
    }

    @Test
    @Order(6)
    @DisplayName("Test that multiple Ingredients can be found")
    void findAllIngredientsTest() {
        IngredientEntity ingredient1 = IngredientEntity.builder()
                .name("Tomato")
                .unit(Unit.PCS)
                .build();

        IngredientEntity ingredient2 = IngredientEntity.builder()
                .name("Potato")
                .unit(Unit.GRAMS)
                .build();

        ingredientRepository.save(ingredient1);
        ingredientRepository.save(ingredient2);

        List<IngredientEntity> foundIngredients = ingredientRepository.findAll();

        assertAll(
                () -> assertNotNull(foundIngredients),
                () -> assertEquals(2, foundIngredients.size(),
                        () -> "Expected to find 2 Ingredients but found " + foundIngredients.size() + " instead"),
                () -> assertEquals("Tomato", foundIngredients.get(0).getName(),
                        () -> "Expected name to be Tomato but was " + foundIngredients.get(0).getName() + " instead"),
                () -> assertEquals(Unit.PCS, foundIngredients.get(0).getUnit(),
                        () -> "Expected unit to be PCS but was " + foundIngredients.get(0).getUnit() + " instead"),
                () -> assertEquals("Potato", foundIngredients.get(1).getName(),
                        () -> "Expected name to be Potato but was " + foundIngredients.get(1).getName() + " instead"),
                () -> assertEquals(Unit.GRAMS, foundIngredients.get(1).getUnit(),
                        () -> "Expected unit to be GRAMS but was " + foundIngredients.get(1).getUnit() + " instead")
        );
    }




}
