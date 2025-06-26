package com.recipeapp.planner.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.recipeapp.planner.domain.entities.RecipeEntity;

import java.util.List;
import java.util.UUID;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RecipeResponseDto(
        UUID recipeId,
        String name,
        String description,
        List<String> instructions,
        UUID createdByUserId
) {
    public static RecipeResponseDto from(RecipeEntity recipe){
        return new RecipeResponseDto(
                recipe.getRecipeId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getCreatedBy() != null ? recipe.getCreatedBy().getUserId() : null
        );
    }

}
