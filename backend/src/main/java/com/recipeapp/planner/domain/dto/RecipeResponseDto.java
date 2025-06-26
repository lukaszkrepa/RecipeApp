package com.recipeapp.planner.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;
@JsonIgnoreProperties(ignoreUnknown = true)
public record RecipeResponseDto(
        UUID recipeId,
        String name,
        String description,
        List<String> instructions,
        UUID createdByUserId
) {}