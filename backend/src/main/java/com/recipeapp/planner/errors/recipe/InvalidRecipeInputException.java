package com.recipeapp.planner.errors.recipe;

public class InvalidRecipeInputException extends RuntimeException{
    public InvalidRecipeInputException(String message) {
        super(message);
    }
}
