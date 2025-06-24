package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.services.RecipeService;

import java.util.List;
import java.util.UUID;

public class RecipeServiceImpl implements RecipeService {
    @Override
    public RecipeEntity createRecipe(RecipeEntity recipe) {
        return null;
    }

    @Override
    public RecipeEntity updateRecipe(UUID recipeId, RecipeEntity updatedRecipe) {
        return null;
    }

    @Override
    public void deleteRecipe(UUID recipeId) {

    }

    @Override
    public RecipeEntity getRecipeById(UUID recipeId) {
        return null;
    }

    @Override
    public List<RecipeEntity> getAllRecipesByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<RecipeEntity> getAllRecipes() {
        return List.of();
    }
}
