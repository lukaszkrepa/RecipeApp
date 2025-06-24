package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.RecipeEntity;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import com.recipeapp.planner.repositories.RecipeRepository;
import com.recipeapp.planner.repositories.UserRepository;
import com.recipeapp.planner.services.RecipeService;

import java.util.List;
import java.util.UUID;

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RecipeEntity createRecipe(RecipeEntity recipe) {
        if (recipe == null){
            throw new InvalidRecipeInputException("Recipe cannot be null");
        }
        if (recipe.getName() == null){
            throw new InvalidRecipeInputException("Recipe name cannot be null");
        }
        if (recipe.getName().isEmpty()){
            throw new InvalidRecipeInputException("Recipe name cannot be empty");
        }
        if (recipe.getDescription() == null){
            throw new InvalidRecipeInputException("Recipe description cannot be null");
        }
        if (recipe.getDescription().isEmpty()){
            throw new InvalidRecipeInputException("Recipe description cannot be empty");
        }
        if (recipe.getInstructions() == null){
            throw new InvalidRecipeInputException("Recipe instructions cannot be null");
        }
        if (recipe.getInstructions().isEmpty()){
            throw new InvalidRecipeInputException("Recipe instructions cannot be empty");
        }
        return recipeRepository.save(recipe);
    }

@Override
    public RecipeEntity updateRecipe(UUID recipeId, RecipeEntity updatedRecipe) {
        if (recipeId == null){
            throw new InvalidRecipeInputException("Recipe ID cannot be null");
        }
        if (updatedRecipe == null){
            throw new InvalidRecipeInputException("Recipe cannot be null");
        }

        // Check if all update fields are empty
        if ((updatedRecipe.getName() == null || updatedRecipe.getName().isEmpty()) &&
            (updatedRecipe.getDescription() == null || updatedRecipe.getDescription().isEmpty()) &&
            (updatedRecipe.getInstructions() == null || updatedRecipe.getInstructions().isEmpty())) {
            throw new InvalidRecipeInputException("At least one field must be provided for update");
        }

        RecipeEntity savedRecipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException("Recipe with ID: " + recipeId + " does not exist"));

        if (updatedRecipe.getName() != null && !updatedRecipe.getName().isEmpty()) {
            savedRecipe.setName(updatedRecipe.getName());
        }

        if (updatedRecipe.getDescription() != null && !updatedRecipe.getDescription().isEmpty()) {
            savedRecipe.setDescription(updatedRecipe.getDescription());
        }

        if (updatedRecipe.getInstructions() != null && !updatedRecipe.getInstructions().isEmpty()) {
            savedRecipe.setInstructions(updatedRecipe.getInstructions());
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
