package com.recipeapp.planner.domain.dto;

import com.recipeapp.planner.domain.entities.UserEntity;

import java.util.UUID;

public record UserResponseDto(UUID userId, String username) {
    public static UserResponseDto from(UserEntity userEntity){
        return new UserResponseDto(
                userEntity.getUserId(),
                userEntity.getUsername()
        );
    }
}
