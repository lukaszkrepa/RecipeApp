package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import com.recipeapp.planner.errors.ingredient.IngredientNotFoundException;
import com.recipeapp.planner.errors.ingredient.InvalidIngredientInputException;
import com.recipeapp.planner.repositories.IngredientRepository;
import com.recipeapp.planner.services.IngredientService;

import java.util.List;
import java.util.UUID;

public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientEntity createIngredient(String name, Unit unit) {
        if (name == null){
            throw new InvalidIngredientInputException("Ingredient name cannot be null");
        }
        if (unit == null){
            throw new InvalidIngredientInputException("Ingredient unit cannot be null");
        }
        if (name.isEmpty()){
            throw new InvalidIngredientInputException("Ingredient name cannot be empty");
        }
        return ingredientRepository.save(
                IngredientEntity
                        .builder()
                        .name(name)
                        .unit(unit)
                        .build()
        );

    }

    @Override
    public IngredientEntity getIngredientById(UUID ingredientId) {
        if (ingredientId == null){
            throw new InvalidIngredientInputException("Ingredient id cannot be null");
        }

        return ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " does not exist"));
    }

    @Override
    public List<IngredientEntity> getIngredientsByName(String name) {
        if (name == null){
            throw new InvalidIngredientInputException("Ingredient name cannot be null");
        }
        if (name.isEmpty()){
            throw new InvalidIngredientInputException("Ingredient name cannot be empty");
        }
        List<IngredientEntity> ingredients = ingredientRepository.findAllByName(name);
        if (ingredients.isEmpty()){
            throw new IngredientNotFoundException("Ingredient with name: " + name + " does not exist");
        }
        return ingredients;
    }

    @Override
    public List<IngredientEntity> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @Override
    public IngredientEntity updateIngredient(UUID ingredientId, String newName, Unit newUnit) {
        if (ingredientId == null){
            throw new InvalidIngredientInputException("Ingredient id cannot be null");
        }
        if (newName == null){
            throw new InvalidIngredientInputException("Ingredient name cannot be null");
        }
        if (newUnit == null){
            throw new InvalidIngredientInputException("Ingredient unit cannot be null");
        }
        if (newName.isEmpty()){
            throw new InvalidIngredientInputException("Ingredient name cannot be empty");
        }
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " does not exist"));
        ingredient.setName(newName);
        ingredient.setUnit(newUnit);
        return ingredientRepository.save(ingredient);
    }

    @Override
    public IngredientEntity updateIngredientName(UUID ingredientId, String newName) {
        if (ingredientId == null){
            throw new InvalidIngredientInputException("Ingredient id cannot be null");
        }
        if (newName == null){
            throw new InvalidIngredientInputException("Ingredient name cannot be null");
        }
        if (newName.isEmpty()){
            throw new InvalidIngredientInputException("Ingredient name cannot be empty");
        }
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " does not exist"));
        ingredient.setName(newName);
        return ingredientRepository.save(ingredient);
    }

    @Override
    public IngredientEntity updateIngredientUnit(UUID ingredientId, Unit newUnit) {
        if (ingredientId == null){
            throw new InvalidIngredientInputException("Ingredient id cannot be null");
        }
        if (newUnit == null){
            throw new InvalidIngredientInputException("Ingredient unit cannot be null");
        }
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " does not exist"));
        ingredient.setUnit(newUnit);
        return ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteIngredient(UUID ingredientId) {
        if (ingredientId == null){
            throw new InvalidIngredientInputException("Ingredient id cannot be null");
        }

        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + ingredientId + " does not exist"));
        ingredientRepository.delete(ingredient);
    }
}
