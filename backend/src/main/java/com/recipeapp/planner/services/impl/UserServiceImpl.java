package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.user.DuplicateUsernameException;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.repositories.UserRepository;
import com.recipeapp.planner.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity createUser(String name) {
        if (name == null) {
            throw new InvalidUserInputException("Username cannot be null");
        }
        if (name.isEmpty()){
            throw new InvalidUserInputException("Username cannot be empty");

        }
        if (userRepository.existsByUsername(name)){
            throw new DuplicateUsernameException("User with username " + name + " already exists");
        }
        UserEntity user = UserEntity.builder()
                .username(name)
                .build();
        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(UUID userId) {
        if (userId == null) {
            throw new InvalidUserInputException("User ID cannot be null");
        }
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " does not exist"));

    }

    @Override
    public UserEntity getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUserInputException("Username cannot be null");
        }
        if (username.isEmpty()) {
            throw new InvalidUserInputException("Username cannot be empty");
        }
        return userRepository.findUserEntitieByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exist"));

    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity updateUser(UUID userId, String newName) {
        if (userId == null) {
            throw new InvalidUserInputException("User ID cannot be null");
        }
        if (newName == null) {
            throw new InvalidUserInputException("Username cannot be null");
        }
        if (newName.isEmpty()) {
            throw new InvalidUserInputException("Username cannot be empty");
        }
        if (userRepository.existsByUsername(newName)) {
            throw new DuplicateUsernameException("User with username " + newName + " already exists");
        }
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " does not exist"));
        user.setUsername(newName);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        if (userId == null) {
            throw new InvalidUserInputException("User ID cannot be null");
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }
}
