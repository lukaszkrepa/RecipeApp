package com.recipeapp.planner.errors;

import com.recipeapp.planner.api.ApiResponse;
import com.recipeapp.planner.errors.ingredient.IngredientNotFoundException;
import com.recipeapp.planner.errors.ingredient.InvalidIngredientInputException;
import com.recipeapp.planner.errors.recipe.InvalidRecipeInputException;
import com.recipeapp.planner.errors.recipe.RecipeNotFoundException;
import com.recipeapp.planner.errors.user.DuplicateUsernameException;
import com.recipeapp.planner.errors.user.InvalidUserInputException;
import com.recipeapp.planner.errors.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ApiResponse.error("404", "User not found", ex.getMessage()));
    }

    @ExceptionHandler(InvalidUserInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidUserInput(InvalidUserInputException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateUsername(DuplicateUsernameException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "User with this username already exists", ex.getMessage()));
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleIngredientNotFound(IngredientNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ApiResponse.error("404", "Ingredient not found", ex.getMessage()));
    }
    @ExceptionHandler(InvalidIngredientInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidIngredientInput(InvalidIngredientInputException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(InvalidRecipeInputException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRecipeInput(InvalidRecipeInputException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecipeNotFound(RecipeNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(ApiResponse.error("404", "Recipe not found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("400", "Invalid input", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.error("500", "Internal server error", ex.getMessage()));
    }

}
