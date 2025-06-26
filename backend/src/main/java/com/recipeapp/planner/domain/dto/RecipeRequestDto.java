package com.recipeapp.planner.domain.dto;

import java.util.List;
import java.util.UUID;

public record RecipeRequestDto(String name, String description, List<String> instructions, UUID userId) {
}
