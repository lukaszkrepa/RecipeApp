package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.dto.RecipeIngredientRequestDto;
import com.recipeapp.planner.domain.dto.RecipeIngredientUpdateDto;
import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.RecipeIngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import com.recipeapp.planner.errors.ingredient.IngredientNotFoundException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.recipeIngredient.InvalidRecipeIngredientInputException;
import com.recipeapp.planner.errors.recipeIngredient.RecipeIngredientNotFoundException;
import com.recipeapp.planner.repositories.IngredientRepository;
import com.recipeapp.planner.repositories.RecipeIngredientRepository;
import com.recipeapp.planner.repositories.RecipeRepository;
import com.recipeapp.planner.services.impl.RecipeIngredientServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@DisplayName("<= RecipeIngredientTests =>")
@ExtendWith(MockitoExtension.class)
public class RecipeIngredientTests {


    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    RecipeIngredientRepository recipeIngredientRepository;

    @InjectMocks
    RecipeIngredientServiceImpl recipeIngredientService;

    private UUID recipeId;
    private UUID ingredientId;
    private RecipeEntity recipe;
    private IngredientEntity ingredient;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        ingredientId = UUID.randomUUID();

        recipe = RecipeEntity.builder()
                .recipeId(recipeId)
                .name("Test Recipe")
                .description("desc")
                .instructions(List.of("step1"))
                .build();

        ingredient = IngredientEntity.builder()
                .ingredientId(ingredientId)
                .name("Sugar")
                .unit(Unit.GRAMS)
                .build();
    }


    @Test
    @DisplayName("create RecipeIngredient with correct data")
    @Order(1)
    public void createRecipeIngredientWithCorrectData(){
        RecipeIngredientRequestDto dto =
                new RecipeIngredientRequestDto(ingredientId, recipeId, 250f);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        when(recipeIngredientRepository.save(any(RecipeIngredientEntity.class))).then(returnsFirstArg());

        RecipeIngredientEntity recipeIngredientEntity = recipeIngredientService.createRecipeIngredient(dto);

        verify(recipeRepository).findById(recipeId);
        verify(ingredientRepository).findById(ingredientId);
        verify(recipeIngredientRepository).save(any(RecipeIngredientEntity.class));

        assertAll(
                () -> assertEquals(recipeIngredientEntity.getRecipe(), recipe),
                () -> assertEquals(recipeIngredientEntity.getIngredient(), ingredient),
                () -> assertEquals(250f, recipeIngredientEntity.getAmount())
        );
        assertEquals(1, recipe.getRecipeIngredients().size());
        assertEquals(recipeIngredientEntity, recipe.getRecipeIngredients().get(0));

        assertEquals(1, ingredient.getRecipeIngredients().size());
        assertEquals(recipeIngredientEntity, ingredient.getRecipeIngredients().get(0));
    }

    @Test
    @DisplayName("create RecipeIngredient with null ingredientId")
    @Order(2)
    public void createRecipeIngredientWithNullIngredientId(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(null, recipeId, 250f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }
    @Test
    @DisplayName("create RecipeIngredient with null recipeId")
    @Order(3)
    public void createRecipeIngredientWithNullRecipeId(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, null, 250f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }
    @Test
    @DisplayName("create RecipeIngredient with non-existing recipeId")
    @Order(4)
    public void createRecipeIngredientWithNonExistingRecipeId(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, recipeId, 250f);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        assertThrows(RecipeNotFoundException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }
    @Test
    @DisplayName("create RecipeIngredient with non-existing ingredientId")
    @Order(5)
    public void createRecipeIngredientWithNonExistingIngredientId(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, recipeId, 250f);
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.empty());
        assertThrows(IngredientNotFoundException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }


    @Test
    @Order(6)
    @DisplayName("create RecipeIngredient with null amount")
    public void createRecipeIngredientWithNullAmount(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, recipeId, null);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }

    @Test
    @Order(7)
    @DisplayName("create RecipeIngredient with negative amount")
    public void createRecipeIngredientWithNegativeAmount(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, recipeId, -1f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }

    @Test
    @Order(8)
    @DisplayName("create RecipeIngredient with zero amount")
    public void createRecipeIngredientWithZeroAmount(){
        RecipeIngredientRequestDto dto = new RecipeIngredientRequestDto(ingredientId, recipeId, 0f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.createRecipeIngredient(dto));
    }

    @Test
    @DisplayName("find RecipeIngredient by id")
    @Order(9)
    public void findRecipeIngredientById(){
        RecipeIngredientEntity recipeIngredient = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();
        recipe.addIngredientLink(recipeIngredient);
        ingredient.addRecipeLink(recipeIngredient);


        when(recipeIngredientRepository.findById(recipeIngredient.getRecipeIngredientId())).thenReturn(Optional.of(recipeIngredient));

        RecipeIngredientEntity foundRecipeIngredient = recipeIngredientService.getRecipeIngredientById(recipeIngredient.getRecipeIngredientId());

        verify(recipeIngredientRepository).findById(recipeIngredient.getRecipeIngredientId());

        assertEquals(recipeIngredient, foundRecipeIngredient);
    }

    @Test
    @DisplayName("find RecipeIngredient by non-existing id")
    @Order(10)
    public void findRecipeIngredientByNonExistingId(){
        when(recipeIngredientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeIngredientService.getRecipeIngredientById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("find RecipeIngredient by null id")
    @Order(11)
    public void findRecipeIngredientByNullId(){
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.getRecipeIngredientById(null));
    }

    @Test
    @DisplayName("delete RecipeIngredient by id")
    @Order(12)
    public void deleteRecipeIngredientById(){
        RecipeIngredientEntity recipeIngredient = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();
        recipe.addIngredientLink(recipeIngredient);
        ingredient.addRecipeLink(recipeIngredient);

        when(recipeIngredientRepository.findById(recipeIngredient.getRecipeIngredientId())).thenReturn(Optional.of(recipeIngredient));

        recipeIngredientService.deleteRecipeIngredient(recipeIngredient.getRecipeIngredientId());

        verify(recipeIngredientRepository).findById(recipeIngredient.getRecipeIngredientId());
        verify(recipeIngredientRepository).delete(recipeIngredient);

        assertEquals(0, recipe.getRecipeIngredients().size());
        assertEquals(0, ingredient.getRecipeIngredients().size());
    }

    @Test
    @DisplayName("delete RecipeIngredient by non-existing id")
    @Order(13)
    public void deleteRecipeIngredientByNonExistingId(){
        when(recipeIngredientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeIngredientService.deleteRecipeIngredient(UUID.randomUUID()));
    }

    @Test
    @DisplayName("delete RecipeIngredient by null id")
    @Order(14)
    public void deleteRecipeIngredientByNullId(){
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.deleteRecipeIngredient(null));
    }

    @Test
    @DisplayName("get all RecipeIngredients")
    @Order(15)
    public void getAllRecipeIngredients(){
        RecipeIngredientEntity recipeIngredient1 = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();
        RecipeIngredientEntity recipeIngredient2 = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();

        recipe.addIngredientLink(recipeIngredient1);
        recipe.addIngredientLink(recipeIngredient2);

        ingredient.addRecipeLink(recipeIngredient1);
        ingredient.addRecipeLink(recipeIngredient2);

        when(recipeIngredientRepository.findAll()).thenReturn(List.of(recipeIngredient1, recipeIngredient2));

        List<RecipeIngredientEntity> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();

        verify(recipeIngredientRepository).findAll();

        assertEquals(2, recipeIngredients.size());
        assertTrue(recipeIngredients.contains(recipeIngredient1));
        assertTrue(recipeIngredients.contains(recipeIngredient2));
    }

    @Test
    @DisplayName("get all RecipeIngredients with no ingredients")
    @Order(16)
    public void getAllRecipeIngredientsWithNoIngredients(){
        when(recipeIngredientRepository.findAll()).thenReturn(List.of());

        List<RecipeIngredientEntity> recipeIngredients = recipeIngredientService.getAllRecipeIngredients();

        verify(recipeIngredientRepository).findAll();

        assertEquals(0, recipeIngredients.size());
    }
    @Test
    @DisplayName("get RecipeIngredients by RecipeId")
    @Order(17)
    public void getRecipeIngredientsByRecipeId(){
        RecipeIngredientEntity recipeIngredient1 = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();
        RecipeIngredientEntity recipeIngredient2 = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();

        recipe.addIngredientLink(recipeIngredient1);
        recipe.addIngredientLink(recipeIngredient2);

        ingredient.addRecipeLink(recipeIngredient1);
        ingredient.addRecipeLink(recipeIngredient2);

        when(recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId)).thenReturn(List.of(recipeIngredient1,recipeIngredient2));

        List<RecipeIngredientEntity> recipeIngredients = recipeIngredientService.getRecipeIngredientsByRecipeId(recipeId);

        verify(recipeIngredientRepository).findAllByRecipe_RecipeId(recipeId);

        assertEquals(2, recipeIngredients.size());
        assertTrue(recipeIngredients.contains(recipeIngredient1));
        assertTrue(recipeIngredients.contains(recipeIngredient2));
    }

    @Test
    @DisplayName("get RecipeIngredients by RecipeId with no ingredients")
    @Order(18)
    public void getRecipeIngredientsByRecipeIdWithNoIngredients(){
        when(recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId)).thenReturn(List.of());

        List<RecipeIngredientEntity> recipeIngredients = recipeIngredientService.getRecipeIngredientsByRecipeId(recipeId);

        verify(recipeIngredientRepository).findAllByRecipe_RecipeId(recipeId);

        assertEquals(0, recipeIngredients.size());
    }

    @Test
    @DisplayName("get RecipeIngredients by non-existing RecipeId")
    @Order(19)
    public void getRecipeIngredientsByNonExistingRecipeId(){
        when(recipeIngredientRepository.findAllByRecipe_RecipeId(recipeId)).thenReturn(List.of());
        assertThrows(RecipeNotFoundException.class, () -> recipeIngredientService.getRecipeIngredientsByRecipeId(recipeId));
    }

    @Test
    @DisplayName("get RecipeIngredients by null RecipeId")
    @Order(20)
    public void getRecipeIngredientsByNullRecipeId(){
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.getRecipeIngredientsByRecipeId(null));
    }

    @Test
    @DisplayName("update RecipeIngredient")
    @Order(21)
    public void updateRecipeIngredient(){
        RecipeIngredientEntity recipeIngredient = RecipeIngredientEntity.builder()
                .recipeIngredientId(UUID.randomUUID())
                .amount(250f)
                .build();
        recipe.addIngredientLink(recipeIngredient);
        ingredient.addRecipeLink(recipeIngredient);

        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(500f);

        when(recipeIngredientRepository.findById(recipeIngredient.getRecipeIngredientId())).thenReturn(Optional.of(recipeIngredient));

        RecipeIngredientEntity updatedRecipeIngredient = recipeIngredientService.updateRecipeIngredient(recipeIngredient.getRecipeIngredientId(), dto);

        verify(recipeIngredientRepository).findById(recipeIngredient.getRecipeIngredientId());
        verify(recipeIngredientRepository).save(recipeIngredient);

        assertEquals(500f, updatedRecipeIngredient.getAmount());
        assertEquals(ingredient, updatedRecipeIngredient.getIngredient());
        assertEquals(recipe, updatedRecipeIngredient.getRecipe());
    }

    @Test
    @DisplayName("update RecipeIngredient with non-existing id")
    @Order(22)
    public void updateRecipeIngredientWithNonExistingId(){
        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(500f);
        when(recipeIngredientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(RecipeIngredientNotFoundException.class, () -> recipeIngredientService.updateRecipeIngredient(UUID.randomUUID(), dto));
    }
    @Test
    @DisplayName("update RecipeIngredient with null id")
    @Order(23)
    public void updateRecipeIngredientWithNullId(){
        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(500f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.updateRecipeIngredient(null, dto));
    }
    @Test
    @DisplayName("update RecipeIngredient with null amount")
    @Order(24)
    public void updateRecipeIngredientWithNullAmount(){
        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(null);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.updateRecipeIngredient(UUID.randomUUID(), dto));
    }
    @Test
    @DisplayName("update RecipeIngredient with negative amount")
    @Order(25)
    public void updateRecipeIngredientWithNegativeAmount(){
        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(-1f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.updateRecipeIngredient(UUID.randomUUID(), dto));
    }
    @Test
    @DisplayName("update RecipeIngredient with zero amount")
    @Order(26)
    public void updateRecipeIngredientWithZeroAmount(){
        RecipeIngredientUpdateDto dto = new RecipeIngredientUpdateDto(0f);
        assertThrows(InvalidRecipeIngredientInputException.class, () -> recipeIngredientService.updateRecipeIngredient(UUID.randomUUID(), dto));
    }
}
