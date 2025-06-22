package com.recipeapp.planner.domain.dto;

import com.recipeapp.planner.domain.enums.Unit;

public record IngredientRequestDto(String name, Unit unit) {
}
