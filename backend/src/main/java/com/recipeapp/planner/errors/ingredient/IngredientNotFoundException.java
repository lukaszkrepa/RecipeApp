package com.recipeapp.planner.errors.ingredient;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(String message) {
        super(message);
    }
}
