package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.entities.IngredientEntity;
import com.recipeapp.planner.domain.enums.Unit;

import java.util.List;
import java.util.UUID;

public interface IngredientService {

    IngredientEntity createIngredient(String name, Unit unit);

    IngredientEntity getIngredientById(UUID ingredientId);

    List<IngredientEntity> getIngredientsByName(String name);

    List<IngredientEntity> getAllIngredients();

    IngredientEntity updateIngredient(UUID ingredientId, String newName, Unit newUnit);

    IngredientEntity updateIngredientName(UUID ingredientId, String newName);

    IngredientEntity updateIngredientUnit(UUID ingredientId, Unit newUnit);

    void deleteIngredient(UUID ingredientId);

}
