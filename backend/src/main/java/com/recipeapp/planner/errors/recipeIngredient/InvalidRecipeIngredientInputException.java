package com.recipeapp.planner.errors.recipeIngredient;

public class InvalidRecipeIngredientInputException extends RuntimeException{
    public InvalidRecipeIngredientInputException(String message) {
        super(message);
    }
}
