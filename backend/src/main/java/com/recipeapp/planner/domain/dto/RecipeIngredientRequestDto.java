package com.recipeapp.planner.domain.dto;

import java.util.UUID;

public record RecipeIngredientRequestDto(UUID ingredientId, UUID recipeId, Float amount) {
}
