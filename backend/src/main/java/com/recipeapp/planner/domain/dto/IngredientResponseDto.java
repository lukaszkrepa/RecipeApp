package com.recipeapp.planner.domain.dto;

import com.recipeapp.planner.domain.enums.Unit;

import java.util.UUID;

public record IngredientResponseDto(UUID ingredientId, String name, Unit unit) {
}
