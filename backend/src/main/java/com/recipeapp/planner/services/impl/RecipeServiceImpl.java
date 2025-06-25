package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.dto.RecipeRequestDto;
import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.repositories.RecipeRepository;
import com.recipeapp.planner.repositories.UserRepository;
import com.recipeapp.planner.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RecipeEntity createRecipe(RecipeRequestDto recipe) {
        if (recipe == null){
            throw new InvalidRecipeInputException("Recipe cannot be null");
        }
        if (recipe.name() == null){
            throw new InvalidRecipeInputException("Recipe name cannot be null");
        }
        if (recipe.name().isEmpty()){
            throw new InvalidRecipeInputException("Recipe name cannot be empty");
        }
        if (recipe.description() == null){
            throw new InvalidRecipeInputException("Recipe description cannot be null");
        }
        if (recipe.description().isEmpty()){
            throw new InvalidRecipeInputException("Recipe description cannot be empty");
        }
        if (recipe.instructions() == null){
            throw new InvalidRecipeInputException("Recipe instructions cannot be null");
        }
        if (recipe.instructions().isEmpty()){
            throw new InvalidRecipeInputException("Recipe instructions cannot be empty");
        }
        if (recipe.userId() == null){
            throw new InvalidRecipeInputException("User ID cannot be null");
        }
        UserEntity user = userRepository.findById(recipe.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        RecipeEntity entity = RecipeEntity.builder()
                .name(recipe.name())
                .description(recipe.description())
                .instructions(recipe.instructions())
                .createdBy(user)
                .build();

        return recipeRepository.save(entity);
    }

    public RecipeEntity updateRecipe(UUID recipeId, RecipeRequestDto dto) {
        if (recipeId == null) {
            throw new InvalidRecipeInputException("Recipe ID cannot be null");
        }
        if (dto == null) {
            throw new InvalidRecipeInputException("Update data cannot be null");
        }

        boolean noChanges = (dto.name() == null || dto.name().isBlank()) &&
                (dto.description() == null || dto.description().isBlank()) &&
                (dto.instructions() == null || dto.instructions().isEmpty());

        if (noChanges) {
            throw new InvalidRecipeInputException("At least one field must be provided for update");
        }

        RecipeEntity savedRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe with ID: " + recipeId + " does not exist"));

        if (dto.name() != null && !dto.name().isBlank()) {
            savedRecipe.setName(dto.name());
        }

        if (dto.description() != null && !dto.description().isBlank()) {
            savedRecipe.setDescription(dto.description());
        }

        if (dto.instructions() != null && !dto.instructions().isEmpty()) {
            savedRecipe.setInstructions(dto.instructions());
        }

        return recipeRepository.save(savedRecipe);
    }

    @Override
    public void deleteRecipe(UUID recipeId) {
        if (recipeId == null){
            throw new InvalidRecipeInputException("Recipe ID cannot be null");
        }
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe with ID: " + recipeId + " does not exist"));
        recipeRepository.delete(recipe);

    }

    @Override
    public RecipeEntity getRecipeById(UUID recipeId) {
        if (recipeId == null){
            throw new InvalidRecipeInputException("Recipe ID cannot be null");
        }
        RecipeEntity recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe with ID: " + recipeId + " does not exist"));
        return recipe;
    }

    @Override
    public List<RecipeEntity> getAllRecipesByUserId(UUID userId) {
        if (userId == null){
            throw new InvalidRecipeInputException("User ID cannot be null");
        }
        if (!userRepository.existsById(userId)){
            throw new UserNotFoundException("User with ID: " + userId + " does not exist");
        }
        return recipeRepository.findAllByCreatedBy_UserId(userId);
    }

    @Override
    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }
}
