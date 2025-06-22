package com.recipeapp.planner.services;


import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import com.recipeapp.planner.errors.ingredient.IngredientNotFoundException;
import com.recipeapp.planner.errors.ingredient.InvalidIngredientInputException;
import com.recipeapp.planner.repositories.IngredientRepository;
import com.recipeapp.planner.services.impl.IngredientServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@DisplayName("<= IngredientServiceTests =>")
@ExtendWith(MockitoExtension.class)
public class IngredientServiceTests {

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientServiceImpl ingredientService;

    @Test
    @DisplayName("create ingredient with correct data")
    @Order(1)
    public void createIngredientWithCorrectData() {
        when(ingredientRepository.save(any(IngredientEntity.class)))
                .thenAnswer(invocation -> {
                    IngredientEntity ingredient = invocation.getArgument(0);
                    ingredient.setIngredientId(UUID.randomUUID());
                    return ingredient;
                });
        IngredientEntity ingredient = ingredientService.createIngredient("Test Ingredient", Unit.GRAMS);
        assertNotNull(ingredient);
        assertAll(
                () -> assertNotNull(ingredient.getIngredientId()),
                () -> assertEquals("Test Ingredient", ingredient.getName()),
                () -> assertEquals(Unit.GRAMS, ingredient.getUnit())
        );
    }

    @Test
    @DisplayName("create ingredient with null name")
    @Order(2)
    public void createIngredientWithNullName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.createIngredient(null, Unit.GRAMS));
    }

    @Test
    @DisplayName("create ingredient with empty name")
    @Order(3)
    public void createIngredientWithEmptyName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.createIngredient("", Unit.GRAMS));
    }

    @Test
    @DisplayName("create ingredient with null unit")
    @Order(4)
    public void createIngredientWithNullUnit() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.createIngredient("Test Ingredient", null));
    }

    @Test
    @DisplayName("get ingredient by id")
    @Order(5)
    public void getIngredientById() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity ingredient = IngredientEntity
                .builder()
                .unit(Unit.GRAMS)
                .name("Test Ingredient")
                .ingredientId(ingredientId)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(java.util.Optional.of(ingredient));
        IngredientEntity result = ingredientService.getIngredientById(ingredientId);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(ingredientId, result.getIngredientId()),
                () -> assertEquals("Test Ingredient", result.getName()),
                () -> assertEquals(Unit.GRAMS, result.getUnit())
        );
    }

    @Test
    @DisplayName("get ingredient by id with null id")
    @Order(6)
    public void getIngredientByIdWithNullId() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.getIngredientById(null));
    }

    @Test
    @DisplayName("get ingredient with non-existent id")
    @Order(7)
    public void getIngredientWithNonExistentId() {
        UUID nonExistentId = UUID.randomUUID();
        when(ingredientRepository.findById(nonExistentId)).thenReturn(java.util.Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredientById(nonExistentId));
    }

    @Test
    @DisplayName("get all ingredients")
    @Order(8)
    public void getAllIngredients() {
        when(ingredientRepository.findAll()).thenReturn(java.util.List.of(
                IngredientEntity.builder().name("Ingredient 1").unit(Unit.GRAMS).build(),
                IngredientEntity.builder().name("Ingredient 2").unit(Unit.GRAMS).build()
        ));
        assertEquals(2, ingredientService.getAllIngredients().size());
    }

    @Test
    @DisplayName("get ingredients by name")
    @Order(9)
    public void getIngredientsByName() {
        String name = "Test Ingredient";
        when(ingredientRepository.findAllByName(name)).thenReturn(java.util.List.of(
                IngredientEntity.builder().name(name).unit(Unit.GRAMS).build(),
                IngredientEntity.builder().name(name).unit(Unit.ML).build()
        ));
        assertEquals(2, ingredientService.getIngredientsByName(name).size());
    }

    @Test
    @DisplayName("get ingredients by name with null name")
    @Order(10)
    public void getIngredientsByNameWithNullName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.getIngredientsByName(null));
    }

    @Test
    @DisplayName("get ingredients by name with empty name")
    @Order(11)
    public void getIngredientsByNameWithEmptyName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.getIngredientsByName(""));
    }

    @Test
    @DisplayName("get ingredients by non-existing name")
    @Order(12)
    public void getIngredientsByNonExistingName() {
        String nonExistingName = "Non Existing Ingredient";
        when(ingredientRepository.findAllByName(nonExistingName)).thenReturn(java.util.List.of());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredientsByName(nonExistingName));
    }

    @Test
    @DisplayName("update ingredient name and unit")
    @Order(13)
    public void updateIngredient() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity existingIngredient = IngredientEntity
                .builder()
                .unit(Unit.GRAMS)
                .name("Test Ingredient")
                .ingredientId(ingredientId)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(java.util.Optional.of(existingIngredient));
        when(ingredientRepository.save(any(IngredientEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IngredientEntity updatedIngredient = ingredientService.updateIngredient(ingredientId, "Updated Ingredient", Unit.ML);

        assertNotNull(updatedIngredient);
        assertAll(
                () -> assertEquals(ingredientId, updatedIngredient.getIngredientId()),
                () -> assertEquals("Updated Ingredient", updatedIngredient.getName()),
                () -> assertEquals(Unit.ML, updatedIngredient.getUnit())
        );
    }

    @Test
    @DisplayName("update ingredient with null id")
    @Order(14)
    public void updateIngredientWithNullId() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredient(null, "Updated Ingredient", Unit.ML));
    }
    @Test
    @DisplayName("update ingredient with null name")
    @Order(15)
    public void updateIngredientWithNullName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredient(UUID.randomUUID(), null, Unit.ML));
    }
    @Test
    @DisplayName("update ingredient with empty name")
    @Order(16)
    public void updateIngredientWithEmptyName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredient(UUID.randomUUID(), "", Unit.ML));
    }
    @Test
    @DisplayName("update ingredient with null unit")
    @Order(17)
    public void updateIngredientWithNullUnit() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredient(UUID.randomUUID(), "Updated Ingredient", null));
    }
    @Test
    @DisplayName("update ingredient with non-existing id")
    @Order(18)
    public void updateIngredientWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(ingredientRepository.findById(nonExistingId)).thenReturn(java.util.Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredient(nonExistingId, "Updated Ingredient", Unit.ML));
    }
    @Test
    @DisplayName("update ingredient name")
    @Order(19)
    public void updateIngredientName() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity existingIngredient = IngredientEntity
                .builder()
                .unit(Unit.GRAMS)
                .name("Test Ingredient")
                .ingredientId(ingredientId)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(java.util.Optional.of(existingIngredient));
        when(ingredientRepository.save(any(IngredientEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IngredientEntity updatedIngredient = ingredientService.updateIngredientName(ingredientId, "Updated Ingredient");

        assertNotNull(updatedIngredient);
        assertAll(
                () -> assertEquals(ingredientId, updatedIngredient.getIngredientId()),
                () -> assertEquals("Updated Ingredient", updatedIngredient.getName()),
                () -> assertEquals(Unit.GRAMS, updatedIngredient.getUnit())
        );
    }

    @Test
    @DisplayName("update ingredient name with null id")
    @Order(20)
    public void updateIngredientNameWithNullId() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredientName(null, "Updated Ingredient"));
    }

    @Test
    @DisplayName("update ingredient name with null name")
    @Order(21)
    public void updateIngredientNameWithNullName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredientName(UUID.randomUUID(), null));
    }
    @Test
    @DisplayName("update ingredient name with empty name")
    @Order(22)
    public void updateIngredientNameWithEmptyName() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredientName(UUID.randomUUID(), ""));
    }
    @Test
    @DisplayName("update ingredient name with non-existing id")
    @Order(23)
    public void updateIngredientNameWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(ingredientRepository.findById(nonExistingId)).thenReturn(java.util.Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredientName(nonExistingId, "Updated Ingredient"));
    }

    @Test
    @DisplayName("update ingredient unit")
    @Order(24)
    public void updateIngredientUnit() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity existingIngredient = IngredientEntity
                .builder()
                .unit(Unit.GRAMS)
                .name("Test Ingredient")
                .ingredientId(ingredientId)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(java.util.Optional.of(existingIngredient));
        when(ingredientRepository.save(any(IngredientEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IngredientEntity updatedIngredient = ingredientService.updateIngredientUnit(ingredientId, Unit.ML);

        assertNotNull(updatedIngredient);
        assertAll(
                () -> assertEquals(ingredientId, updatedIngredient.getIngredientId()),
                () -> assertEquals("Test Ingredient", updatedIngredient.getName()),
                () -> assertEquals(Unit.ML, updatedIngredient.getUnit())
        );
    }
    @Test
    @DisplayName("update ingredient unit with null id")
    @Order(25)
    public void updateIngredientUnitWithNullId() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredientUnit(null, Unit.ML));
    }
    @Test
    @DisplayName("update ingredient unit with null unit")
    @Order(26)
    public void updateIngredientUnitWithNullUnit() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.updateIngredientUnit(UUID.randomUUID(), null));
    }
    @Test
    @DisplayName("update ingredient unit with non-existing id")
    @Order(27)
    public void updateIngredientUnitWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(ingredientRepository.findById(nonExistingId)).thenReturn(java.util.Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredientUnit(nonExistingId, Unit.ML));
    }
    @Test
    @DisplayName("delete ingredient")
    @Order(28)
    public void deleteIngredient() {
        UUID ingredientId = UUID.randomUUID();
        IngredientEntity existingIngredient = IngredientEntity
                .builder()
                .unit(Unit.GRAMS)
                .name("Test Ingredient")
                .ingredientId(ingredientId)
                .build();

        when(ingredientRepository.findById(ingredientId)).thenReturn(java.util.Optional.of(existingIngredient));

        ingredientService.deleteIngredient(ingredientId);

        verify(ingredientRepository, times(1)).delete(existingIngredient);
    }

    @Test
    @DisplayName("delete ingredient with non-existing id")
    @Order(29)
    public void deleteIngredientWithNonExistingId() {
        UUID nonExistingId = UUID.randomUUID();
        when(ingredientRepository.existsById(nonExistingId)).thenReturn(false);
        assertThrows(IngredientNotFoundException.class, () -> ingredientService.deleteIngredient(nonExistingId));
    }

    @Test
    @DisplayName("delete ingredient with null id")
    @Order(30)
    public void deleteIngredientWithNullId() {
        assertThrows(InvalidIngredientInputException.class, () -> ingredientService.deleteIngredient(null));
    }


}
