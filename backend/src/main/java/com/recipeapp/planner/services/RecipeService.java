package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.entities.RecipeEntity;

import java.util.List;
import java.util.UUID;

public interface RecipeService {

    RecipeEntity createRecipe(RecipeEntity recipe);

    RecipeEntity updateRecipe(UUID recipeId, RecipeEntity updatedRecipe);

    void deleteRecipe(UUID recipeId);

    RecipeEntity getRecipeById(UUID recipeId);

    List<RecipeEntity> getAllRecipesByUserId(UUID userId);

    List<RecipeEntity> getAllRecipes();

}
