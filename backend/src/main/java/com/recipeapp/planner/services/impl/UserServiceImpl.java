package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserEntity createUser(String name) {
        return null;
    }

    @Override
    public UserEntity getUserById(UUID userId) {
        return null;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return null;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return List.of();
    }

    @Override
    public UserEntity updateUser(UUID userId, String newName) {
        return null;
    }

    @Override
    public void deleteUser(UUID userId) {

    }
}
