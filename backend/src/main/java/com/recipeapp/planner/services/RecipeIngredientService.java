package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.dto.RecipeIngredientRequestDto;
import com.recipeapp.planner.domain.dto.RecipeIngredientUpdateDto;
import com.recipeapp.planner.domain.entities.RecipeIngredientEntity;

import java.util.List;
import java.util.UUID;

public interface RecipeIngredientService {

    RecipeIngredientEntity createRecipeIngredient(RecipeIngredientRequestDto recipeIngredient);

    RecipeIngredientEntity updateRecipeIngredient(UUID recipeIngredientId, RecipeIngredientUpdateDto recipeIngredientUpdateDto);

    void deleteRecipeIngredient(UUID recipeIngredientId);

    RecipeIngredientEntity getRecipeIngredientById(UUID recipeIngredientId);

    List<RecipeIngredientEntity> getRecipeIngredientsByRecipeId(UUID recipeId);

    List<RecipeIngredientEntity> getAllRecipeIngredients();
}
