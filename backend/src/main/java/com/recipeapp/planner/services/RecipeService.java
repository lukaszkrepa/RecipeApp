package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.dto.RecipeRequestDto;
import com.recipeapp.planner.domain.entities.RecipeEntity;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    RecipeEntity createRecipe(RecipeRequestDto recipe);

    RecipeEntity updateRecipe(UUID recipeId, RecipeRequestDto updatedRecipe);

    void deleteRecipe(UUID recipeId);

    RecipeEntity getRecipeById(UUID recipeId);

    List<RecipeEntity> getAllRecipesByUserId(UUID userId);

    List<RecipeEntity> getAllRecipes();

}
