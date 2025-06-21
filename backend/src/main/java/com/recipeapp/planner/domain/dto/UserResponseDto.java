package com.recipeapp.planner.domain.dto;

import java.util.UUID;

public record UserResponseDto(UUID userId, String username) {
}
