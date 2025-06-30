package com.recipeapp.planner.errors.recipeIngredient;

public class RecipeIngredientNotFoundException extends RuntimeException{
    public RecipeIngredientNotFoundException(String message) {
        super(message);
    }
}