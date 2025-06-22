package com.recipeapp.planner.errors.ingredient;

public class InvalidIngredientInputException extends RuntimeException{
    public InvalidIngredientInputException(String message) {
        super(message);
    }
}
