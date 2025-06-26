package com.recipeapp.planner.domain.dto;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;

import java.util.UUID;

public record IngredientResponseDto(UUID ingredientId, String name, Unit unit) {
    public static IngredientResponseDto from(IngredientEntity ingredientEntity){
        return new IngredientResponseDto(
                ingredientEntity.getIngredientId(),
                ingredientEntity.getName(),
                ingredientEntity.getUnit()
        );
    }
}
