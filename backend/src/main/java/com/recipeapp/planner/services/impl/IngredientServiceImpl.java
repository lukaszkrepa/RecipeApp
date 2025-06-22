package com.recipeapp.planner.services.impl;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;
import com.recipeapp.planner.services.IngredientService;

import java.util.List;
import java.util.UUID;

public class IngredientServiceImpl implements IngredientService {
    @Override
    public IngredientEntity createIngredient(String name, Unit unit) {
        return null;
    }

    @Override
    public IngredientEntity getIngredientById(UUID ingredientId) {
        return null;
    }

    @Override
    public List<IngredientEntity> getIngredientsByName(String name) {
        return List.of();
    }

    @Override
    public List<IngredientEntity> getAllIngredients() {
        return List.of();
    }

    @Override
    public IngredientEntity updateIngredient(UUID ingredientId, String newName, Unit newUnit) {
        return null;
    }

    @Override
    public IngredientEntity updateIngredientName(UUID ingredientId, String newName) {
        return null;
    }

    @Override
    public IngredientEntity updateIngredientUnit(UUID ingredientId, Unit newUnit) {
        return null;
    }

    @Override
    public void deleteIngredient(UUID ingredientId) {

    }
}
