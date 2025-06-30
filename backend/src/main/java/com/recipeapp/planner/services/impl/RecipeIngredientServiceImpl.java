package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.dto.RecipeIngredientRequestDto;
import com.recipeapp.planner.domain.dto.RecipeIngredientUpdateDto;
import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.RecipeIngredientEntity;
import com.recipeapp.planner.repositories.IngredientRepository;
import com.recipeapp.planner.repositories.RecipeIngredientRepository;
import com.recipeapp.planner.repositories.RecipeRepository;
import com.recipeapp.planner.services.RecipeIngredientService;

import java.util.List;
import java.util.UUID;

public class RecipeIngredientServiceImpl implements RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredientServiceImpl(RecipeIngredientRepository recipeIngredientRepository, RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public RecipeIngredientEntity createRecipeIngredient(RecipeIngredientRequestDto recipeIngredient) {
        RecipeEntity recipe = recipeRepository.findById(recipeIngredient.recipeId()).orElseThrow(() -> new RuntimeException("Recipe not found"));
        IngredientEntity ingredient = ingredientRepository.findById(recipeIngredient.ingredientId()).orElseThrow(() -> new RuntimeException("Ingredient not found"));
        RecipeIngredientEntity recipeIngredientEntity = RecipeIngredientEntity
                .builder()
                .amount(recipeIngredient.amount())
                .build();

        recipe.addIngredientLink(recipeIngredientEntity);
        ingredient.addRecipeLink(recipeIngredientEntity);

        return recipeIngredientRepository.save(recipeIngredientEntity);

    }

    @Override
    public RecipeIngredientEntity updateRecipeIngredient(UUID recipeIngredientId, RecipeIngredientUpdateDto recipeIngredientUpdateDto) {
        return null;
    }

    @Override
    public void deleteRecipeIngredient(UUID recipeIngredientId) {

    }

    @Override
    public RecipeIngredientEntity getRecipeIngredientById(UUID recipeIngredientId) {
        return null;
    }

    @Override
    public List<RecipeIngredientEntity> getRecipeIngredientsByRecipeId(UUID recipeId) {
        return List.of();
    }

    @Override
    public List<RecipeIngredientEntity> getAllRecipeIngredients() {
        return List.of();
    }
}
