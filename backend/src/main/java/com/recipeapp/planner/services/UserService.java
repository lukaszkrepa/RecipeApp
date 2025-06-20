package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.entities.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserEntity createUser(String name);

    UserEntity getUserById(UUID userId);

    UserEntity getUserByUsername(String username);

    List<UserEntity> getAllUsers();

    UserEntity updateUser(UUID userId, String newName);

    void deleteUser(UUID userId);

}
